package com.boolck.tasks.uniquerandom;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UniqueRandomGeneratorTest {

    private final UniqueRandomGenerator generator = new UniqueRandomGenerator();

    @Test
    public void testBitSetGeneratorReturnsEntireRangeOnce() {
        assertContainsEntireRange(generator.getUniqueWithBitSet(10, 20), 10, 20);
    }

    @Test
    public void testShuffleGeneratorReturnsEntireRangeOnce() {
        assertContainsEntireRange(generator.getUniqueWithShuffle(10, 20), 10, 20);
    }

    @Test
    public void testArraySwapGeneratorReturnsEntireRangeOnce() {
        assertContainsEntireRange(generator.getUniqueWithArraySwap(10, 20), 10, 20);
    }

    @Test
    public void testSingleValueRange() {
        assertEquals(List.of(5), generator.getUniqueWithBitSet(5, 5));
        assertEquals(List.of(5), generator.getUniqueWithShuffle(5, 5));
        assertEquals(List.of(5), generator.getUniqueWithArraySwap(5, 5));
        assertEquals(List.of(Integer.MAX_VALUE),
                generator.getUniqueWithShuffle(Integer.MAX_VALUE, Integer.MAX_VALUE));
    }

    @Test
    public void testInvalidRangeIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> generator.getUniqueWithBitSet(2, 1));
        assertThrows(IllegalArgumentException.class,
                () -> generator.getUniqueWithShuffle(2, 1));
        assertThrows(IllegalArgumentException.class,
                () -> generator.getUniqueWithArraySwap(2, 1));
    }

    private void assertContainsEntireRange(List<Integer> values, int min, int max) {
        Set<Integer> expected = new HashSet<>();
        for (int value = min; value <= max; value++) {
            expected.add(value);
        }

        assertEquals(max - min + 1, values.size());
        assertEquals(expected, new HashSet<>(values));
    }
}
