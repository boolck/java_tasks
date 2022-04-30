package com.boolck.tasks.movingaverage;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DefaultMovingAverageStoreTest {
    private final static double EPSILON = 0.00001;

    @Test
    public void whenNoEntry_ThenResultIsNan(){
        MovingAverageStore store = new DefaultMovingAverageStore();
        assertEquals(Double.NaN,store.getMovingAverage("test"),EPSILON);
    }

    @Test
    public void whenSingleEntry_ThenResultIsValid(){
        MovingAverageStore store = new DefaultMovingAverageStore();
        store.addSample("test",10.0);
        assertEquals(10.0,store.getMovingAverage("test"),EPSILON);
    }

    @Test
    public void whenMoreThan1Entry_ThenResultIsValid(){
        MovingAverageStore store = new DefaultMovingAverageStore();
        store.addSample("test",10.0);
        store.addSample("test",11.0);
        store.addSample("test",12.0);
        assertEquals(11.0,store.getMovingAverage("test"),EPSILON);
    }

    @Test
    public void whenMoreThan1Producer_ThenResultIsValid(){
        MovingAverageStore store = new DefaultMovingAverageStore();
        store.addSample("test1",10.0);
        store.addSample("test1",11.0);
        store.addSample("test1",12.0);

        store.addSample("test2",22.0);
        store.addSample("test2",24.0);
        assertEquals(11.0,store.getMovingAverage("test1"),EPSILON);
        assertEquals(23.0,store.getMovingAverage("test2"),EPSILON);
    }

    @Test
    public void whenMoreThan100Size_ThenResultIsValid(){
        MovingAverageStore store = new DefaultMovingAverageStore();
        for(int i=1;i<=100;i++){
            store.addSample("test",i);
        }
        assertEquals(50.5,store.getMovingAverage("test"),EPSILON);
        store.addSample("test",51);
        assertEquals(51.0,store.getMovingAverage("test"),EPSILON);
    }
}
