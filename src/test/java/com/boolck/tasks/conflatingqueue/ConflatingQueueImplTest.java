package com.boolck.tasks.conflatingqueue;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConflatingQueueImplTest {

    @Test
    public void whenItemsHaveDifferentKeys_ThenTheyAreTakenInFifoOrder() throws InterruptedException {
        ConflatingQueue<String, Integer> queue = new ConflatingQueueImpl<>();
        queue.offer(new KeyValueImpl<>("BTCUSD", 7000));
        queue.offer(new KeyValueImpl<>("ETHUSD", 250));

        assertKeyValue(queue.take(), "BTCUSD", 7000);
        assertKeyValue(queue.take(), "ETHUSD", 250);
        assertTrue(queue.isEmpty());
    }

    @Test
    public void whenKeyIsOfferedAgain_ThenLatestValueKeepsOriginalPosition() throws InterruptedException {
        ConflatingQueue<String, Integer> queue = new ConflatingQueueImpl<>();
        queue.offer(new KeyValueImpl<>("BTCUSD", 7000));
        queue.offer(new KeyValueImpl<>("ETHUSD", 250));
        queue.offer(new KeyValueImpl<>("BTCUSD", 7002));

        assertKeyValue(queue.take(), "BTCUSD", 7002);
        assertKeyValue(queue.take(), "ETHUSD", 250);
    }

    @Test
    public void whenTakenKeyIsOfferedAgain_ThenItIsQueuedAgain() throws InterruptedException {
        ConflatingQueue<String, Integer> queue = new ConflatingQueueImpl<>();
        queue.offer(new KeyValueImpl<>("BTCUSD", 7000));

        assertKeyValue(queue.take(), "BTCUSD", 7000);
        assertTrue(queue.isEmpty());

        queue.offer(new KeyValueImpl<>("BTCUSD", 7001));

        assertFalse(queue.isEmpty());
        assertKeyValue(queue.take(), "BTCUSD", 7001);
    }

    @Test
    public void whenTakeWaitsOnEmptyQueue_ThenOfferUnblocksIt() throws Exception {
        ConflatingQueue<String, Integer> queue = new ConflatingQueueImpl<>();
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            Future<KeyValue<String, Integer>> taken = executor.submit(queue::take);
            assertThrows(TimeoutException.class, () -> taken.get(50, TimeUnit.MILLISECONDS));

            Future<Boolean> offered = executor.submit(
                    () -> queue.offer(new KeyValueImpl<>("BTCUSD", 7000)));

            assertTrue(offered.get(1, TimeUnit.SECONDS));
            assertKeyValue(taken.get(1, TimeUnit.SECONDS), "BTCUSD", 7000);
        } finally {
            executor.shutdownNow();
        }
    }

    @Test
    public void whenNullIsOffered_ThenItIsRejected() {
        ConflatingQueue<String, Integer> queue = new ConflatingQueueImpl<>();

        assertThrows(NullPointerException.class, () -> queue.offer(null));
    }

    private void assertKeyValue(KeyValue<String, Integer> keyValue, String key, int value) {
        assertEquals(key, keyValue.getKey());
        assertEquals(value, keyValue.getValue());
    }
}
