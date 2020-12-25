package com.kb.challenge.cache;

public  class CacheProvider {

    private static final CacheProvider _INSTANCE = new CacheProvider();

    public Cache getLRUCache(final int size){
        Cache lruCache = new LRUCache(size);
        return lruCache;
    }

    public static CacheProvider getInstance(){
        return _INSTANCE;
    }

}
