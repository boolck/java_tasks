package com.boolck.tasks.eventbus;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Implementation of EventBus interface
 * key idea is to maintain a map (ConcurrentHashMap) to keep an event type with their subscribers
 * when a new event is observed, all subscribers are fetched from map and notified
 */
public class DefaultEventBus implements EventBus{

    private final Map<Class<?>, Set<ConsumerWithFilter>> filteredSubscribers;

    public DefaultEventBus() {
        this.filteredSubscribers = new ConcurrentHashMap<>();
    }

    private static boolean NO_FILTER(Object x) {
        return Boolean.TRUE;
    }

    /**
     * step1. fetches the event specific subscribers from map
     * step2. validates the subscriber's filter is eiligble to receive the event
     * step3. pushes the event to subscriber's consumer accordingly
     */
    @Override
    public void publishEvent(Object event) throws EventBusException{
        validateEventType(event);

        Class<?> eventType = event.getClass();

        for(Map.Entry<Class<?>,Set<ConsumerWithFilter>> entry : filteredSubscribers.entrySet()){
            if(eventType.isAssignableFrom(entry.getKey())){
                for(ConsumerWithFilter filteredConsumer : entry.getValue()){
                    if(filteredConsumer.getFilter().test(event)){
                        filteredConsumer.getConsumer().accept(event);
                    }
                }
            }
        }
    }

    /**
     * adds the subscriber for specific event types
     * this method is overloaded to support no filtering (default passed)
     */
    @Override
    public <T> void addSubscriber(Class<? extends T> eventType,
                              Consumer<T> subscriber) throws EventBusException{

        addSubscriberForFilteredEvents(eventType,subscriber, DefaultEventBus::NO_FILTER);
    }

    /**
     * removes  subscriber. for event to be received subsriber has to be added again
     */
    @Override
    public <T> void removeSubscriber(Consumer<T> subscriber) throws EventBusException{
        validateSubscriber(subscriber);

        for (Set<ConsumerWithFilter> eventSubscribers : filteredSubscribers.values()) {
            eventSubscribers.removeIf(currConsumer -> currConsumer.getConsumer().equals(subscriber));
        }
    }


    /**
     * supports server side filtering for events
     * a predicate from subcriber is used to check event is eligible to be pushed
     */
    @Override
    public <T> void addSubscriberForFilteredEvents(Class<? extends T> eventType,
                                               Consumer<T> subscriber,
                                               Predicate<T> filterPredicate) throws EventBusException{
       validateEventType(eventType);
       validateSubscriber(subscriber);
       if(filterPredicate==null){
           filterPredicate = DefaultEventBus::NO_FILTER;
       }
        filteredSubscribers.putIfAbsent(eventType,new CopyOnWriteArraySet<>());
        filteredSubscribers.get(eventType).add(new ConsumerWithFilter<T>(subscriber, filterPredicate));
    }

    private void validateEventType(Object event) throws EventBusException{
        if(event==null){
            throw new EventBusException("Event cannot be null");
        }
    }

    private void validateSubscriber(Consumer subscriber) throws EventBusException{
        if(subscriber==null){
            throw new EventBusException("Subscriber cannot be null");
        }
    }

}
