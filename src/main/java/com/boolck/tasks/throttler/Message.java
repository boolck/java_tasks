package com.boolck.tasks.throttler;

/**
 * Message class to keep value and processing time
 */
public class Message {
    private final long eventTimeInmilliseconds;
    private final String value; // Can be custom type

    public Message(long eventTimeInmilliseconds, String value) {
        this.eventTimeInmilliseconds = eventTimeInmilliseconds;
        this.value = value;
    }

    public String toString(){
        return String.format("Message: %s inserted at: %d ms",value, eventTimeInmilliseconds);
    }

    public long getEventTimeInmilliseconds(){
        return this.eventTimeInmilliseconds;
    }

    public String getValue(){
        return this.value;
    }
}
