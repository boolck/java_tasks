package com.boolck.tasks.throttler;


/**
 * A Throttler allows a client to restrict how many times something can happen in a given time period
 * (for example we may not want to send more than a number of quotes to an exchange in a specific time period).
 */
public interface Throttler {
    // check if we can proceed (poll)
    ThrottleResult shouldProceed(Message message);
    // subscribe to be told when we can proceed (Push)
    boolean notifyWhenCanProceed(Message message);
    // subscribe to be told when we can proceed (Push) and attempt to push
    boolean notifyAndAttemptToProceedNow(Message message, boolean attemptToProcess);

    enum ThrottleResult {
        PROCEED,
        DO_NOT_PROCEED
    }

}

