package com.kb.challenge.cache;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

public class LRUCache<K,V> implements Cache<K,V>{

    private final Map<K,V> map;
    private final Queue<K> queue;

    private final Lock readLock ;
    private final Lock writeLock ;

    private final int size;

    protected LRUCache(final int size){
        this.size = size;
        map = new ConcurrentHashMap<>(size);
        queue = new ConcurrentLinkedQueue<>();
        final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        readLock = readWriteLock.readLock();
        writeLock = readWriteLock.writeLock();
    }

    @Override
    public V get(K key){
        readLock.lock();
        try {
            V value = map.get(key);
            if(value != null){
                if (queue.remove(key)) {
                    queue.add(key);
                }
            }
            return value;
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public V get(K key, Function<K, V> mappingFunction) {
        V value = this.get(key);
        writeLock.lock();
        try {
            if (value == null) {
                value = mappingFunction.apply(key);
                if (value != null) {
                    this.put(key, value);
                }
            }
        } finally{
            writeLock.unlock();
        }
        return value;
    }

    @Override
    public void remove(K key){
        writeLock.lock();
        try {
            if(map.containsKey(key)){
                map.remove(key);
                queue.remove(key);
            }
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void put(K key, V value){
        writeLock.lock();
        try {
            if(map.containsKey(key)){
                queue.remove(key);
            }
            if(queue.size() >= size){
                K queueKey= queue.poll();
                map.remove(queueKey);
            }
            queue.add(key);
            map.put(key, value);

        } finally{
            writeLock.unlock();
        }
    }
}
