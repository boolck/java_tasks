package com.boolck.tasks.eventbus;


import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class DefaultEventBusTest {


    @Test
    public void whenPublishEvent_ThenSubscribersCalled() throws EventBusException{
        EventBus bus = new DefaultEventBus();
        List<String> strEventList = new LinkedList<>();
        bus.addSubscriber(String.class, strEventList::add);

        bus.publishEvent("Event A");
        bus.publishEvent("Event B");

        assertEquals(Arrays.asList("Event A", "Event B"), strEventList);
    }

    @Test
    public void whenUnscribed_ThenNoEventCaptured() throws EventBusException{
        EventBus bus = new DefaultEventBus();
        List<String> strEventList = new LinkedList<>();
        Consumer<String> subscriber = strEventList::add;

        bus.addSubscriber(String.class, subscriber);
        bus.publishEvent("Event A");

        bus.removeSubscriber(subscriber);
        bus.publishEvent("Event B");

        assertEquals(Collections.singletonList("Event A"), strEventList);
    }

    @Test
    public void whenDiffEventTypeSubscribed_ThenResultIsExpected() throws EventBusException{
        EventBus bus = new DefaultEventBus();

        List<String> strEventList = new LinkedList<>();
        bus.addSubscriber(String.class, strEventList::add);

        List<Integer> intEventList = new LinkedList<>();
        bus.addSubscriber(Integer.class, intEventList::add);

        bus.publishEvent("Event A");
        bus.publishEvent("Event B");
        bus.publishEvent(10);
        bus.publishEvent(20);

        assertEquals(Arrays.asList(10,20), intEventList);
        assertEquals(Arrays.asList("Event A","Event B"), strEventList);
    }

    @Test
    public void whenCustomEventTypeSubscribed_ThenResultIsExpected() throws EventBusException{
        EventBus bus = new DefaultEventBus();

        List<CustomEvent> eventList = new LinkedList<>();
        bus.addSubscriber(CustomEvent.class, eventList::add);

        Event eventX = new CustomEvent("Event X",1000);
        Event eventY = new CustomEvent("Event Y",5000);
        bus.publishEvent(eventX);
        bus.publishEvent(eventY);

        assertEquals(Arrays.asList(eventX,eventY), eventList);
    }

    @Test
    public void whenEventTypeFiltered_OnlyFilteredEventCaptured() throws EventBusException{
        EventBus bus = new DefaultEventBus();

        List<CustomEvent> eventList = new LinkedList<>();
        Predicate<CustomEvent> eventOlderThan = CustomEvent.isEventOlderThan(2000L);

        bus.addSubscriberForFilteredEvents(CustomEvent.class,eventList::add,eventOlderThan);

        Event eventX = new CustomEvent("Event X",1000);
        Event eventY = new CustomEvent("Event Y",5000);
        Event eventZ = new CustomEvent("Event Z",8000);
        bus.publishEvent(eventX);
        bus.publishEvent(eventY);
        bus.publishEvent(eventZ);

        assertEquals(Arrays.asList(eventY,eventZ), eventList);
    }
}
