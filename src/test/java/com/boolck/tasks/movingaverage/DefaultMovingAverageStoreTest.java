package com.boolck.tasks.movingaverage;


import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    public void whenCustomWindowIsFull_ThenOldestSampleIsRemoved() {
        MovingAverageStore store = new DefaultMovingAverageStore(3);
        store.addSample("test", 1);
        store.addSample("test", 2);
        store.addSample("test", 3);
        store.addSample("test", 7);

        assertEquals(4.0, store.getMovingAverage("test"), EPSILON);
    }

    @Test
    public void whenAllAveragesRequested_ThenSnapshotCannotModifyStore() {
        MovingAverageStore store = new DefaultMovingAverageStore();
        store.addSample("first", 10);
        store.addSample("second", 20);

        Map<String, Double> snapshot = store.getMovingAverages();
        snapshot.put("first", 100.0);

        assertEquals(Map.of("first", 10.0, "second", 20.0), store.getMovingAverages());
    }

    @Test
    public void whenProducerIsNull_ThenOperationsAreRejected() {
        MovingAverageStore store = new DefaultMovingAverageStore();

        assertThrows(NullPointerException.class, () -> store.addSample(null, 10));
        assertThrows(NullPointerException.class, () -> store.getMovingAverage(null));
    }
}
