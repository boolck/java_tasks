package com.boolck.tasks.optimizer;

import java.util.*;

/**
 * Immutable implementation of Scnerio interface
 * Uses a builder to let accessors create a new instance
 */
public final class ImmutableScenario implements Scenario{

    private final String underlyingAsset;
    private final List<Double> bumps;
    private final int frequency;

    private ImmutableScenario(String underlyingAsset, List<Double> bumps, int frequency){
        this.underlyingAsset = underlyingAsset;
        this.bumps = Collections.unmodifiableList(new LinkedList<>(bumps));
        this.frequency = frequency;
    }

    /**
     * @return same list because we are creating unmodifiable list in constructor
     */
    @Override
    public List<Double> getRelativeBumps() {
        return this.bumps;
    }

    @Override
    public String getUnderlyingAsset() {
        return this.underlyingAsset;
    }

    @Override
    public int getFrequency() {
        return this.frequency;
    }

    @Override
    public String toString() {
        return "ImmutableScenario{" +
                "underlyingAsset='" + underlyingAsset + '\'' +
                ", bumps=" + bumps +
                ", frequency=" + frequency +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableScenario that = (ImmutableScenario) o;
        return frequency == that.frequency
                && underlyingAsset.equals(that.underlyingAsset)
                && new HashSet<>(bumps).equals(new HashSet<>(that.bumps));
    }

    @Override
    public int hashCode() {
        return Objects.hash(underlyingAsset, bumps, frequency);
    }

    public static class Builder {

        private final String asset;
        private List<Double> bumps;
        private int frequency;

        public Builder(String asset){
            this.asset = asset;
        }

        public Builder bumps(List<Double> bumps) {
            Objects.requireNonNull(bumps);
            this.bumps = bumps;
            return this;
        }

        public Builder frequency(int frequency) {
            this.frequency = frequency;
            return this;
        }

        public ImmutableScenario build() {
            return new ImmutableScenario(asset, bumps, frequency);
        }
    }
}
