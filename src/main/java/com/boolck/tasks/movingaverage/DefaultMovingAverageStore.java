package com.boolck.tasks.movingaverage;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * MovingAverage implementation based on queue
 * each producer maintains a pair of current sum and queue of elements in current window
 * when new element arrives, the sum and queue is updated accordingly (if size is reached) to reflect the new average
 * uses a write lock to support thread safety for multiple access to method
 * if there is only 1 thread per producer we can remove writeLock and use ConcurrentHashMap and ConcurrentLinkedDequeue
 */
public class DefaultMovingAverageStore implements MovingAverageStore {

    private final Map<String,Double> movingAverageMap = new HashMap<>();
    private final Map<String,Map.Entry<Double, Queue<Double>>> producerQueueMap = new HashMap<>();
    private final ReadWriteLock lock  = new ReentrantReadWriteLock();
    private final Lock writeLock = lock.writeLock();
    private final Lock readLock = lock.readLock();
    private final int maxSize;

    public DefaultMovingAverageStore(){
        this(100);
    }

    public DefaultMovingAverageStore(int maxSize){
        this.maxSize = maxSize;
    }

    /**
     * @param producer, not null
     * @param value to be added
     * when a new value is inserted, existing queue is fetched that has the current sum and list of elements
     *  if the size has reached limit, first element is popped to make space for new element to be inserted
     *  the current sum is updated accordingly
     *  call to method is encapsulated by write lock to ensure 1 thread can access the addition.
     *  if there is 1 subscriber per producer, we can remove writeLock and instead use ConcurrentHashMap and ConcurrentLinkedDequeue
     *
     */
    @Override
    public void addSample(String producer, double value) {
        Objects.requireNonNull(producer);
        try {
            writeLock.lock();
            producerQueueMap.putIfAbsent(producer, Map.entry(0.0, new ArrayDeque<>()));
            double currentSum = producerQueueMap.get(producer).getKey();
            Queue<Double> currentNumbers = producerQueueMap.get(producer).getValue();
            if (currentNumbers.size() >= maxSize) {
                currentSum -= currentNumbers.remove();
            }

            currentSum += value;
            currentNumbers.add(value);
            producerQueueMap.put(producer, Map.entry(currentSum, currentNumbers));
            double newAverage = currentSum /currentNumbers.size();
            movingAverageMap.put(producer, newAverage);
        }
        finally{
            writeLock.unlock();
        }
    }

    /**
     * @param producer for which moving average is required
     * @return movingAverage for this producer
     * call is encapsulated using readLock to ensure write and read lock can happen simultaneously
     */
    @Override
    public double getMovingAverage(String producer) {
        Objects.requireNonNull(producer);
        try {
            readLock.lock();
            if(!movingAverageMap.containsKey(producer)){
                return Double.NaN;
            }
            return movingAverageMap.get(producer);
        } finally {
            readLock.unlock();
        }
    }

    /**
     * @return the map of all producers and their current moving average
     * creates a new map and return to ensure caller cannot modify the map.
     */
    @Override
    public Map<String, Double> getMovingAverages() {
        return new HashMap<>(movingAverageMap);
    }
}
