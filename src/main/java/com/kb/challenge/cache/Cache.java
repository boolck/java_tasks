package com.kb.challenge.cache;


import java.util.function.Function;

public interface Cache<K, V> {
    V get(K key);

    V get(K key, Function<K,V> mappingFunction);

    void put(K key, V value);

    void remove(K key);
}

