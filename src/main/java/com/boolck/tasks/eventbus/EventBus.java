package com.boolck.tasks.eventbus;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * EventBus interface providing methods for implementation
 */
public interface EventBus {

    void publishEvent(Object event) throws EventBusException;

    <T> void addSubscriber(Class<? extends T> eventType, Consumer<T> subscriber) throws EventBusException;

    <T> void addSubscriberForFilteredEvents(Class<? extends T> eventType, Consumer<T> subscriber, Predicate<T> filterPredicate) throws EventBusException;

    <T> void removeSubscriber(Consumer<T> subscriber) throws EventBusException;

}

