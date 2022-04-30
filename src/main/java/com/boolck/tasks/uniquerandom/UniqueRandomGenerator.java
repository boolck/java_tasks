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
        //gen.getUniqueWithBitSet(10,20);
        //gen.genUniqueWithShuffle(10,20);
        gen.getUniqueWithArraySwap(10,20);
    }

    //O(unbounded)
    private void getUniqueWithBitSet(int min, int max){
        int size = max-min+1;
        BitSet unique = new BitSet();
        int numbersAvailable=size;
        while (numbersAvailable>0) {
            int randInt = ThreadLocalRandom.current().nextInt(min, max);
            if (!unique.get(randInt)) {
                unique.set(randInt);
                numbersAvailable--;
               System.out.println(randInt);
            }
        }
    }

    //O(2n) = O(n)
    private void genUniqueWithShuffle(int min, int max){
        List<Integer> list = new ArrayList<>(max-min+1);
        for (int i = min; i <= max; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        System.out.println(list);
    }

    //O(n)
    private void getUniqueWithArraySwap(int min,int max){
            int size = max-min+1;
            int[] arr = new int[size];
            for (int i = 0; i < size; i++) {
                arr[i] = min+i;
            }
            //initialize it to last index
            int lastIndex=size-1;
            while (lastIndex>=0) {
                int randIndex = ThreadLocalRandom.current().nextInt(0, size);
                int retval = arr[randIndex];
                arr[randIndex] = arr[lastIndex--];
                System.out.println(retval);
                }
            }
        }


