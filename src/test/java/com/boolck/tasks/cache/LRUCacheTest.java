package com.boolck.tasks.cache;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class LRUCacheTest {

    private Cache<String, Integer> cache;

    @BeforeEach
    public  void setup() {
        cache = CacheProvider.getInstance().getLRUCache(2);
    }

    @Test
    public void testcacheStartsEmpty() {
        assertNull(cache.get("1"));
    }

    @Test
    public void testSetBelowCapacity() {
        cache.put("1", 1);
        assertEquals(1,cache.get("1"));
        assertNull(cache.get("2"));
        cache.put("2", 4);
        assertEquals(1,cache.get("1"));
        assertEquals(4,cache.get("2"));
    }

    @Test
    public void testCapacityReachedOldestRemoved() {
        cache.put("1", 10);
        assertEquals(10,cache.get("1"));
        cache.put("2", 4);
        cache.put("3", 9);
        assertNull(cache.get("1"));
        assertEquals(4,cache.get("2"));
        assertEquals(9,cache.get("3"));
    }

    @Test
    public void testGetRenewsEntry() {
        cache.put("1", 1);
        cache.put("2", 4);
        assertEquals(1,cache.get("1"));
        cache.put("3", 9);
        assertEquals(1,cache.get("1"));
        assertNull(cache.get("2"));
        assertEquals(9,cache.get("3"));
    }

    @Test
    public void testMappingFunction() {
        assertNull(cache.get("100"));
        cache.get("100", String::length);
        assertEquals(3,cache.get("100"));
        assertEquals(3,cache.get("100", x->x.length()+1));
    }

    @Test
    public void testRemoveDeletesEntry() {
        cache.put("1", 1);

        cache.remove("1");

        assertNull(cache.get("1"));
    }

    @Test
    public void testUpdatingEntryRenewsIt() {
        cache.put("1", 1);
        cache.put("2", 2);

        cache.put("1", 10);
        cache.put("3", 3);

        assertEquals(10, cache.get("1"));
        assertNull(cache.get("2"));
        assertEquals(3, cache.get("3"));
    }

    @Test
    public void testNullMappingResultIsNotCached() {
        AtomicInteger invocationCount = new AtomicInteger();

        assertNull(cache.get("missing", key -> {
            invocationCount.incrementAndGet();
            return null;
        }));
        assertNull(cache.get("missing", key -> {
            invocationCount.incrementAndGet();
            return null;
        }));

        assertEquals(2, invocationCount.get());
    }

    @Test
    public void testCacheProviderIsSingleton() {
        assertSame(CacheProvider.getInstance(), CacheProvider.getInstance());
    }
}
