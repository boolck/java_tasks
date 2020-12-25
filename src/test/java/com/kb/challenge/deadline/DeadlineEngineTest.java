package com.kb.challenge.deadline;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}
