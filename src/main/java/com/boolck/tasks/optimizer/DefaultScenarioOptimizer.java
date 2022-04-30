package com.boolck.tasks.optimizer;

import java.util.*;

/**
 * Default implementation of Scenario Optimizer
 * Uses HashMap & HashSet to check the unique bumps and max frequency to calculate optimized cost.
 */
public class DefaultScenarioOptimizer implements ScenarioOptimizer{

    /**
     * @param originalScenarios collection of scenarios with different underlying assets, frequencies and bumps
     * @return optimized scenarios
     * Step 1. constructs a map of all scenarios against asset name.
     * Step 2. If map size matches the original scenarios, we have no common asset so return the original
     * Step 3. Keep checking all scnenarios of same asset and optimize if cost is less then original return the optimized
     */
    @Override
    public Collection<Scenario> optimize(Collection<Scenario> originalScenarios) {

        Map<String, List<Scenario>> map = new HashMap<>();
        for(Scenario currentScenario : originalScenarios){
            map.putIfAbsent(currentScenario.getUnderlyingAsset(),new LinkedList<>());
            map.get(currentScenario.getUnderlyingAsset()).add(currentScenario);
        }

        if(map.size()==originalScenarios.size()){
            return originalScenarios;
        }

        Collection<Scenario> optimizedScenarios = new LinkedList<>();
        for(Map.Entry<String,List<Scenario>> entry : map.entrySet()){
            List<Scenario> optimizedScenario = this.getOptimizeScenarioForSameAssest(entry.getKey(),entry.getValue());
            optimizedScenarios.addAll(optimizedScenario);
        }
        return optimizedScenarios;
    }

    /**
     * @param asset for which optimization should be checked
     * @param originalScenarioForAnAsset all scnerios for the asset
     * @return optimized scenarios
     * Step 1. Maintains a main set of all bumps & maximum frequency
     * Step 2. If the final cost of unique bumps * max frequency is less than original cost, return the optimized scenario
     * total cost is maintained as long to handle integer overflow
     */
    private List<Scenario> getOptimizeScenarioForSameAssest(String asset, List<Scenario> originalScenarioForAnAsset) {
        Set<Double> bucketSetOfAllFrequencies = new HashSet<>();
        int maxFrequency = Integer.MIN_VALUE;
        long originalCost = 0L;
        for(Scenario currentScenario : originalScenarioForAnAsset){
            bucketSetOfAllFrequencies.addAll(currentScenario.getRelativeBumps());
            maxFrequency = Math.max(maxFrequency,currentScenario.getFrequency());
            originalCost += currentScenario.calculateCost();
        }

        if(originalCost <= (long)bucketSetOfAllFrequencies.size() * maxFrequency){
            return originalScenarioForAnAsset;
        }
        else{
            Scenario optimized = new ImmutableScenario.Builder(asset).
                    bumps(new LinkedList<>(bucketSetOfAllFrequencies)).
                    frequency(maxFrequency).
                    build();
            return List.of(optimized);
        }
    }
}
