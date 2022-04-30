package com.boolck.tasks.conflatingqueue;

import java.util.Objects;

public class KeyValueImpl<K,V> implements KeyValue<K, V>{

    K key ;
    V value;

    public KeyValueImpl(K key, V value){
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyValueImpl<?, ?> keyValue = (KeyValueImpl<?, ?>) o;
        return Objects.equals(key, keyValue.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
