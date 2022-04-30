package com.boolck.tasks.optimizer;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DefaultScenarioOptimizerTest {

    @Test
    public void whenScenarioIsGood_ThenNoOptimization(){
        Scenario scnerio = new ImmutableScenario.Builder("U1").bumps(List.of(1.0,2.0,3.0)).frequency(5).build();
        ScenarioOptimizer optimizer = new DefaultScenarioOptimizer();
        Collection<Scenario> optimizedResult = optimizer.optimize(Collections.singletonList(scnerio));
        assertEquals(List.of(scnerio),optimizedResult);
    }

    @Test
    public void whenScenarioIsOnDiffAsset_ThenNoOptimization(){
        Scenario scenarioAssetOne = new ImmutableScenario.Builder("U1").bumps(List.of(0.1,0.2,0.3,0.4,0.5)).frequency(2).build();
        Scenario scenarioAssetTwo = new ImmutableScenario.Builder("U2").bumps(List.of(0.1,0.2,0.3,0.4,0.6)).frequency(2).build();
        ScenarioOptimizer optimizer = new DefaultScenarioOptimizer();
        List<Scenario> originalScenario = List.of(scenarioAssetOne, scenarioAssetTwo);
        Collection<Scenario> optimizedResult = optimizer.optimize(originalScenario);
        assertEquals(originalScenario,optimizedResult);
    }

    @Test
    public void whenCostOnSameFreqIsMore_ThenNoOptimization(){
        Scenario scenarioAssetOne_1 = new ImmutableScenario.Builder("U1").bumps(List.of(0.1,0.2,0.3,0.4,0.5)).frequency(2).build();
        Scenario scenarioAssetOne_2 = new ImmutableScenario.Builder("U1").bumps(List.of(0.5,0.6,0.7,0.8,0.9)).frequency(4).build();
        ScenarioOptimizer optimizer = new DefaultScenarioOptimizer();
        List<Scenario> originalScnerio = List.of(scenarioAssetOne_1, scenarioAssetOne_2);
        Collection<Scenario> optimizedResult = optimizer.optimize(originalScnerio);
        assertEquals(originalScnerio,optimizedResult);
    }


    @Test
    public void whenTwoScenariosAreOptimized_ThenResultIsCorrect(){
        ImmutableScenario.Builder builder = new ImmutableScenario.Builder("A1");
        Scenario scenarioAssetOne_1 = builder.bumps(List.of(0.1,0.2,0.3,0.4,0.5)).frequency(2).build();
        Scenario scenarioAssetOne_2 = builder.bumps(List.of(0.1,0.2,0.3,0.4,0.6)).frequency(2).build();
        ScenarioOptimizer optimizer = new DefaultScenarioOptimizer();
        List<Scenario> originalScnerio = List.of(scenarioAssetOne_2, scenarioAssetOne_1);
        Collection<Scenario> optimizedResult = optimizer.optimize(originalScnerio);
        Scenario shouldBeOptimized = builder.bumps(List.of(0.1,0.2,0.3,0.4,0.5,0.6)).frequency(2).build();
        assertEquals(List.of(shouldBeOptimized),optimizedResult);
    }

    @Test
    public void whenDifferentScenariosAreOptimized_ThenResultIsCorrect(){
        Scenario scenarioAssetOne_1 = new ImmutableScenario.Builder("U1").bumps(List.of(0.1,0.2,0.3,0.4,0.5)).frequency(2).build();
        Scenario scenarioAssetOne_2 = new ImmutableScenario.Builder("U1").bumps(List.of(0.1,0.2,0.3,0.4,0.6)).frequency(4).build();
        Scenario scenarioAssetTwo_1 = new ImmutableScenario.Builder("U2").bumps(List.of(2.0,3.0,4.0)).frequency(10).build();
        ScenarioOptimizer optimizer = new DefaultScenarioOptimizer();
        List<Scenario> originalScnerio = List.of(scenarioAssetOne_1, scenarioAssetOne_2,scenarioAssetTwo_1);
        Collection<Scenario> optimizedResult = optimizer.optimize(originalScnerio);
        Scenario shouldBeOptimizedA1 = new ImmutableScenario.Builder("U1").bumps(List.of(0.1,0.2,0.3,0.4,0.5,0.6)).frequency(4).build();
        assertEquals(List.of(shouldBeOptimizedA1,scenarioAssetTwo_1),optimizedResult);
    }
}