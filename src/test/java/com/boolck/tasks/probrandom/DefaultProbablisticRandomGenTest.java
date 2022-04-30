package com.boolck.tasks.probrandom;


import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DefaultProbablisticRandomGenTest {

    ProbabilisticRandomGen instance ;

    @Test
    public void testSingleNumber(){
        ProbabilisticRandomGen.NumAndProbability one = new ProbabilisticRandomGen.NumAndProbability(3,1.0f);
        List<ProbabilisticRandomGen.NumAndProbability> list = new ArrayList<>(List.of(one));
        instance = new DefaultProbabilisticRandomGen(list);
        assertEquals(instance.nextFromSample(),3);
    }

    @Test
    public void testThreeNumbers(){
        ProbabilisticRandomGen.NumAndProbability one = new ProbabilisticRandomGen.NumAndProbability(1,0.2f);
        ProbabilisticRandomGen.NumAndProbability two = new ProbabilisticRandomGen.NumAndProbability(2,0.3f);
        ProbabilisticRandomGen.NumAndProbability three = new ProbabilisticRandomGen.NumAndProbability(3,0.5f);
        List<ProbabilisticRandomGen.NumAndProbability> list = new ArrayList<>(List.of(one,two,three));
        instance = new DefaultProbabilisticRandomGen(list);
        Map<Integer,Integer> frequencyResult = new HashMap<>(3);

        for(int i=0;i<100000;i++){
            int key = instance.nextFromSample();
            frequencyResult.put(key,frequencyResult.getOrDefault(key,0)+1);
        }

        int freqFirst = (int)Math.round(frequencyResult.get(1)/10000.0);
        int freqSecond = (int)Math.round(frequencyResult.get(2)/10000.0);
        int freqThird = (int)Math.round(frequencyResult.get(3)/10000.0);

        assertEquals(Optional.of(2),Optional.of(freqFirst));
        assertEquals(Optional.of(3),Optional.of(freqSecond));
        assertEquals(Optional.of(5),Optional.of(freqThird));
    }
}
