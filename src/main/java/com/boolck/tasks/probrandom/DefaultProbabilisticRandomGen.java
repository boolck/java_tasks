package com.boolck.tasks.probrandom;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Implementation of  ProbabilisticRandomGen -
 * using a fixed size array and populating the number by % probability in array
 *
 *  for example num=5, probability=0.2 will fill 20% of array elements with number 5
 *  and getNext will use a random number to get index and return the element
 */
public class DefaultProbabilisticRandomGen implements ProbabilisticRandomGen {

    private final List<NumAndProbability> numAndProbabilities;
    private int[] frequencyArray;

    public DefaultProbabilisticRandomGen(List<NumAndProbability> numAndProbabilities) {
        this.numAndProbabilities = numAndProbabilities;
        this.fillFrequencyArray();
    }

    /**
     * creates a fixed length array frequency array and populates probability percentage points
     */
    private void fillFrequencyArray() {
        this.frequencyArray = new int[100];
        int arrayIndex = 0;
        for (NumAndProbability runningNum : numAndProbabilities) {
            int runningIndex = 0;
            int upperBound = (int)(runningNum.getProbabilityOfSample() * 100);
            while (runningIndex++ < upperBound && arrayIndex < frequencyArray.length) {
                frequencyArray[arrayIndex++] = runningNum.getNumber();
            }
        }
    }

    /**
     * @return random number from the array
     * as the array is densely populated we get a random number index from 0 to range
     */
    @Override
    public int nextFromSample() {
        int randomIndex = ThreadLocalRandom.current().nextInt(0, frequencyArray.length);
        return frequencyArray[randomIndex];
    }


}
