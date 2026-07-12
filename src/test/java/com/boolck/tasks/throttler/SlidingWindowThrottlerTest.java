package com.boolck.tasks.throttler;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SlidingWindowThrottlerTest {

    private Throttler testInstance;

    @Test
    public void testSingleMessageProceed(){
        this.testInstance = new SlidingWindowThrottler(5,60000);
        Throttler.ThrottleResult result =
                testInstance.shouldProceed(new Message(System.currentTimeMillis(),"message 1"));
        assertEquals( Throttler.ThrottleResult.PROCEED, result);
    }

    @Test
    public void testSingleMessageProceedInNewWindow() throws InterruptedException {
        this.testInstance = new SlidingWindowThrottler(1,10);
        testInstance.shouldProceed(new Message(System.currentTimeMillis(),"message 1"));
        Thread.sleep(50);
        Throttler.ThrottleResult result =
                testInstance.shouldProceed(new Message(System.currentTimeMillis(),"message 2"));
        assertEquals(Throttler.ThrottleResult.PROCEED, result);
    }

    @Test
    public void testSingleMessageWindowFull() {
        this.testInstance = new SlidingWindowThrottler(2,20000);

        long now = System.currentTimeMillis();

        testInstance.shouldProceed(new Message(now,"message 1"));
        testInstance.shouldProceed(new Message(now,"message 2"));

        Throttler.ThrottleResult result =
                testInstance.shouldProceed(new Message(now,"message 3"));

        assertEquals(Throttler.ThrottleResult.DO_NOT_PROCEED, result);
    }

    @Test
    public void testNotificationWhenCanProceed() throws InterruptedException {
        this.testInstance = new SlidingWindowThrottler(2,200);

        long now = System.currentTimeMillis();

        testInstance.shouldProceed(new Message(now,"message 1"));
        testInstance.shouldProceed(new Message(now,"message 2"));

        Message messageHeld = new Message(now,"message 3");
        Throttler.ThrottleResult result =
                testInstance.shouldProceed(messageHeld);
        assertEquals(Throttler.ThrottleResult.DO_NOT_PROCEED, result);

        Thread.sleep(50);
        assertTrue(testInstance.notifyWhenCanProceed(messageHeld));

    }

    @Test
    public void testExpiredMessagesDoNotConsumeWindowCapacity() {
        this.testInstance = new SlidingWindowThrottler(1, 100);
        long now = System.currentTimeMillis();
        testInstance.shouldProceed(new Message(now - 1000, "expired"));

        Throttler.ThrottleResult result =
                testInstance.shouldProceed(new Message(now, "current"));

        assertEquals(Throttler.ThrottleResult.PROCEED, result);
    }

    @Test
    public void testAttemptToProceedReportsWhetherCapacityIsAvailable() {
        this.testInstance = new SlidingWindowThrottler(1, 60000);
        Message first = new Message(System.currentTimeMillis(), "first");
        Message second = new Message(System.currentTimeMillis(), "second");

        assertTrue(testInstance.notifyAndAttemptToProceedNow(first, true));
        assertFalse(testInstance.notifyAndAttemptToProceedNow(second, true));
    }

    @Test
    public void testMessageAccessorsAndStringRepresentation() {
        Message message = new Message(123L, "value");

        assertEquals(123L, message.getEventTimeInmilliseconds());
        assertEquals("value", message.getValue());
        assertEquals("Message: value inserted at: 123 ms", message.toString());
    }
}
