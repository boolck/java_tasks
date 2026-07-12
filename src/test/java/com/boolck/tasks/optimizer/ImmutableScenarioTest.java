package com.boolck.tasks.optimizer;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ImmutableScenarioTest {

    @Test
    public void whenBuilt_ThenSourceBumpsCannotModifyScenario() {
        List<Double> bumps = new ArrayList<>(List.of(0.1, 0.2));
        Scenario scenario = new ImmutableScenario.Builder("U1").bumps(bumps).frequency(3).build();

        bumps.add(0.3);

        assertEquals(List.of(0.1, 0.2), scenario.getRelativeBumps());
        assertThrows(UnsupportedOperationException.class,
                () -> scenario.getRelativeBumps().add(0.3));
    }

    @Test
    public void whenBumpOrderDiffers_ThenScenariosRemainEqualWithSameHashCode() {
        Scenario first = new ImmutableScenario.Builder("U1")
                .bumps(List.of(0.1, 0.2))
                .frequency(3)
                .build();
        Scenario second = new ImmutableScenario.Builder("U1")
                .bumps(List.of(0.2, 0.1))
                .frequency(3)
                .build();

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    public void whenCostCalculated_ThenBumpsAreMultipliedByFrequency() {
        Scenario scenario = new ImmutableScenario.Builder("U1")
                .bumps(List.of(0.1, 0.2, 0.3))
                .frequency(4)
                .build();

        assertEquals(12, scenario.calculateCost());
    }
}
