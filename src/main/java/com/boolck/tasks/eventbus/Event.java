package com.boolck.tasks.eventbus;

/**
 * Event that indicates message digest (message value for simplication), eventTime and processing time in milliseconds
 */
public interface Event {
    String getDigest();
    long eventTime();
    long processingTime();
}
