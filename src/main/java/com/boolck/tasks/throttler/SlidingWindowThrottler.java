package com.boolck.tasks.throttler;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Sliding window based implementation of Throttler
 * it takes 2 inputs to construct :
 *  window size = max number of elements in  window and
 *  windowValidityInMilliseconds=max TTL of window
 *
 *  It uses a LinkedBlockingQueue to keep messages (which blocks if queue is popped if empty or inserted if full)
 *  and a discarded queue if message cannot be processed now. That queue is periodically checked by timer task for eligibility
 */
public class SlidingWindowThrottler implements Throttler{
    private final int windowSize;
    private final long windowValidityInMilliseconds;
    private final Queue<Message> currentMessages;
    private final Queue<Message> discardedMessages;

    public SlidingWindowThrottler(int windowSize, long windowValidityInMilliseconds){
        this.windowSize = windowSize;
        this.windowValidityInMilliseconds = windowValidityInMilliseconds;
        this.currentMessages = new LinkedBlockingDeque<>(windowSize);
        this.discardedMessages = new ConcurrentLinkedQueue<>();
        this.setupTimerForMessageNotification();
    }

    /**
     * @param message to be processed
     * @return  PROCEED if  message can be processed in this window within max limit and TTL, DO_NOT_PROCEED otherwise
     * manages the message expired after checking the current time against message queue
     */
    @Override
    public ThrottleResult shouldProceed(Message message) {
        long now = System.currentTimeMillis();

        while (!currentMessages.isEmpty() &&
                currentMessages.peek().getEventTimeInmilliseconds() + windowValidityInMilliseconds <= now){
            currentMessages.remove();
        }

        if (currentMessages.size() < windowSize) {
            currentMessages.add(message);
            return ThrottleResult.PROCEED;
        } else {
            discardedMessages.add(message);
            return ThrottleResult.DO_NOT_PROCEED;
        }
    }

    /**
     * @param message to be processed
     * @return called from message handler when message is eligible to be processed in current window
     */
    @Override
    public boolean notifyWhenCanProceed(Message message) {
        System.out.println("Message can be proceeded now "+message);
        return notifyAndAttemptToProceedNow(message,false);
    }

    /**
     * @param message to be processed
     * @param attemptToProcess if true message is attempted to be processed in current window
     * @return called from message handler when message is eligible to be processed in current window
     */
    @Override
    public boolean notifyAndAttemptToProceedNow(Message message, boolean attemptToProcess) {
        if(attemptToProcess){
            ThrottleResult throttleResult = shouldProceed(message);
            if(throttleResult ==ThrottleResult.PROCEED){
                System.out.println("Message processed now "+message.getValue());
                return true;
            }
            else{
                System.out.println("Message cannot be processed now, will be attempted later "+message.getValue());
                return false;
            }
        }
        return true;
    }

    /**
     * setup of timer task for checking  if discarded messages are eligible to be processed in current window
     */
    private void setupTimerForMessageNotification() {
        TimerTask task = new TimerTask() {
            public void run() {
                long now = System.currentTimeMillis();
                for(Message message : discardedMessages){
                    if(message.getEventTimeInmilliseconds()+ windowValidityInMilliseconds <=now){
                        notifyWhenCanProceed(message);
                    }
                }
            }
        };
        Timer timer = new Timer("ProceedChecker Timer");
        timer.schedule(task, windowValidityInMilliseconds);
    }
}
