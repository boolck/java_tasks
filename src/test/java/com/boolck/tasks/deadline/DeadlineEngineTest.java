package com.boolck.tasks.deadline;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DeadlineEngineTest {

    private  DeadlineEngine engine;

    @BeforeEach
    public void setup() {
        engine = new DeadlineEngineImpl();
    }

    @Test
    public void testSchedule(){
       long id = engine.schedule(100);
       assertEquals(1L,id);
       assertEquals(1,engine.size());
       engine.schedule(200);
       assertEquals(2,engine.size());
       engine.schedule(200);
       assertEquals(3,engine.size());
    }

    @Test
    public void testRemove(){
        long id = engine.schedule(100);
        assertEquals(1,engine.size());
        assertTrue(engine.cancel(id));
        assertEquals(0,engine.size());
        assertFalse(engine.cancel(2L));
        assertFalse(engine.cancel(1L));
        long id1_1 = engine.schedule(100);
        assertEquals(1,engine.size());
        assertTrue(engine.cancel(id1_1));
        engine.schedule(100);
        assertEquals(1,engine.size());
    }

    @Test
    public void testPoll(){
        engine.schedule(100);
        engine.schedule(200);
        engine.poll(50,System.out::println,2);
        assertEquals(2,engine.size());
        engine.poll(50,System.out::println,2);
        engine.poll(150,System.out::println,2);
        assertEquals(1,engine.size());
        engine.poll(200,System.out::println,2);
        assertEquals(0,engine.size());
    }

    @Test
    public void testPollWithMaxLessThanSize(){
        engine.schedule(100);
        engine.schedule(200);
        engine.poll(250,System.out::println,1);
        assertEquals(1,engine.size());
    }

    @Test
    public void testPollReturnsCountAndExpiredRequestIds() {
        long laterId = engine.schedule(200);
        long earlierId = engine.schedule(100);
        List<Long> expiredIds = new ArrayList<>();

        int expired = engine.poll(150, expiredIds::add, 10);

        assertEquals(1, expired);
        assertEquals(List.of(earlierId), expiredIds);
        assertEquals(1, engine.size());
        assertTrue(engine.cancel(laterId));
    }

    @Test
    public void testZeroMaxPollLeavesExpiredDeadlinesScheduled() {
        engine.schedule(100);

        assertEquals(0, engine.poll(100, id -> fail("Handler must not be called"), 0));
        assertEquals(1, engine.size());
    }

    @Test
    public void testDeadlineOrderingDoesNotOverflow() {
        long laterId = engine.schedule(Long.MAX_VALUE);
        long earlierId = engine.schedule(0);
        List<Long> expiredIds = new ArrayList<>();

        assertEquals(1, engine.poll(0, expiredIds::add, 10));
        assertEquals(List.of(earlierId), expiredIds);
        assertTrue(engine.cancel(laterId));
    }
}
