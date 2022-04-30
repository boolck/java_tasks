package com.boolck.tasks.eventbus;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * Custom Event Type that is used to passing in event bus
 */
public class CustomEvent implements Event {

    private final String digest;
    private final long eventTime;

    public CustomEvent(String digest, long eventTime){
        this.digest = digest;
        this.eventTime = eventTime;
    }

    @Override
    public String getDigest() {
        return this.digest;
    }

    @Override
    public long eventTime() {
        return this.eventTime;
    }

    @Override
    public long processingTime() {
        return System.currentTimeMillis();
    }

    public static Predicate<CustomEvent> isEventOlderThan(long age) {
        return p -> p.eventTime() > age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomEvent that = (CustomEvent) o;
        return eventTime == that.eventTime && digest.equals(that.digest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(digest, eventTime);
    }
}
