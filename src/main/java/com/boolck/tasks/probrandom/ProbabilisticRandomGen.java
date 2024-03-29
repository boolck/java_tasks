package com.boolck.tasks.probrandom;

/**
 * Implement the below interface so that we can pass in a List<NumAndProbability> -
 * which denotes a set of numbers and the probability that they should be drawn when we call nextFromSample().
 */

public interface ProbabilisticRandomGen {

    int nextFromSample();

    class NumAndProbability {
        private final int number;
        private final float probabilityOfSample;

        public NumAndProbability(int number, float probabilityOfSample) {
            this.number = number;
            this.probabilityOfSample = probabilityOfSample;
        }

        public int getNumber() {
            return number;
        }

        public float getProbabilityOfSample() {
            return probabilityOfSample;
        }
    }

}
