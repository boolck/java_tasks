package com.boolck.tasks.uniquerandom;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

//http://www.radwin.org/michael/2015/01/13/unique-random-numbers-technical-interview-question/
//https://leetcode.com/discuss/interview-question/619524/Google-or-Onsite-or-Random-Generator
public class UniqueRandomGenerator {


    public static void main(String... args){
        UniqueRandomGenerator gen = new UniqueRandomGenerator();
        gen.getUniqueWithArraySwap(10,20).forEach(System.out::println);
    }

    //O(unbounded)
    public List<Integer> getUniqueWithBitSet(int min, int max){
        int size = getRangeSize(min, max);
        BitSet unique = new BitSet(size);
        List<Integer> result = new ArrayList<>(size);
        while (result.size() < size) {
            int randomIndex = ThreadLocalRandom.current().nextInt(size);
            if (!unique.get(randomIndex)) {
                unique.set(randomIndex);
                result.add(min + randomIndex);
            }
        }
        return result;
    }

    //O(2n) = O(n)
    public List<Integer> getUniqueWithShuffle(int min, int max){
        int size = getRangeSize(min, max);
        List<Integer> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(min + i);
        }
        Collections.shuffle(list);
        return list;
    }

    //O(n)
    public List<Integer> getUniqueWithArraySwap(int min,int max){
        int size = getRangeSize(min, max);
        int[] values = new int[size];
        List<Integer> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            values[i] = min+i;
        }
        int lastIndex=size-1;
        while (lastIndex>=0) {
            int randomIndex = ThreadLocalRandom.current().nextInt(lastIndex + 1);
            result.add(values[randomIndex]);
            values[randomIndex] = values[lastIndex--];
        }
        return result;
    }

    private int getRangeSize(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("Minimum must not exceed maximum");
        }
        return Math.addExact(Math.subtractExact(max, min), 1);
    }
}
