package com.boolck.tasks.conflatingqueue;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ConflatingQueueImpl<K, V> implements ConflatingQueue<K, V> {

    private final BlockingQueue<KeyValue<K, V>> queue;
    private final Map<K, KeyValue<K, V>> latestMap;
    private final Lock lock = new ReentrantLock();

    public ConflatingQueueImpl() {
        this.queue = new LinkedBlockingQueue<>();
        this.latestMap = new ConcurrentHashMap<>();
    }

    @Override
    public boolean offer(KeyValue<K, V> keyValue) {
        Objects.requireNonNull(keyValue);
        boolean isSuccess = true;
        lock.lock();
        try{
            if(!this.latestMap.containsKey(keyValue.getKey())){
                isSuccess = this.queue.add(keyValue);
            }
            if(isSuccess){
                this.latestMap.put(keyValue.getKey(), keyValue);
            }
        }
        finally{
            lock.unlock();
        }
        return isSuccess ;
    }

    @Override
    public KeyValue<K, V> take() throws InterruptedException{
        lock.lock();
        try{
            KeyValue<K,V> polled =  this.queue.take();
            return this.latestMap.get(polled.getKey());
        }
        finally{
            lock.unlock();
        }
    }

    public boolean isEmpty() {
        return this.queue.isEmpty();
    }
}
