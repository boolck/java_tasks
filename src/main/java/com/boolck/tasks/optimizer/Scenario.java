package com.boolck.tasks.optimizer;

import java.util.List;

/**
 * Interface representing a scenario.
 * <p/>
 * Implementation <strong>MUST</strong> be immutable.
 *
 */
public interface Scenario {

    /**
     * List of bumps
     * 
     * @return not null, not empty
     */
    List<Double> getRelativeBumps();

    /**
     * Identifier of underlying asset
     * 
     * @return not null
     */
    String getUnderlyingAsset();

    /**
     * Frequency in number of invocations per minute
     * 
     * @return
     */
    int getFrequency();

    /**
     * Calculate the cost of this scenario
     * 
     * @return
     */
    default int calculateCost() {
        return getRelativeBumps().size() * getFrequency();
    }
}
