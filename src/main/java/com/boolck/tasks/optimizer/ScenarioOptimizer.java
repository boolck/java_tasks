package com.boolck.tasks.optimizer;

import java.util.Collection;

/**
 * <p>
 * The optimizer takes in a collection of scenarios (original) and attempts to determine a more optimal collection of scenarios (optimal).
 * </p>
 * The optimal must:
 * <ul>
 * <li>have the same or lower cost than the original for each underlying asset</li>
 * <li>have the same set of bumps as the original for each underlying asset</li>
 * <li>have the same or higher frequency than the original, for each bump and underlying asset</li>
 * </ul>
 *
 * The following table shows the optimal solutions from given inputs as examples.
 * <table border="1" cellpadding="2" cellspacing="0">
 * <thead>
 * <tr>
 * <th>Original</th>
 * <th>Optimal</th>
 * <th>Note</th>
 * </tr>
 * </thead> <tbody>
 * <tr>
 * <td>
 * S1: U1 - bumps=0.1,0.2,0.3,0.4,0.5 freq=2<br>
 * S2: U1 - bumps=0.1,0.2,0.3,0.4,0.6 freq=2<br>
 * Cost = 5*2 + 5*2 = 20</td>
 * <td>U1 - bumps=0.1,0.2,0.3,0.4,0.5,0.6 freq=2<br>
 * Cost = 6*2 = 12</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>
 * S1: U1 - bumps=0.1,0.2,0.3,0.4,0.5 freq=2<br>
 * S2: U1 - bumps=0.1,0.2,0.3,0.4,0.6 freq=4<br>
 * Cost = 5*2 + 5*4 = 30</td>
 * <td>U1 - bumps=0.1,0.2,0.3,0.4,0.5,0.6 freq=4<br>
 * Cost = 6*4 = 24</td>
 * <td>Frequency becomes 4 to fulfill requirement of S2</td>
 * </tr>
 * <tr>
 * <td>S1: U1 - bumps=0.1,0.2,0.3,0.4,0.5 freq=2<br>
 * S2: U1 - bumps=0.5,0.6,0.7,0.8,0.9 freq=4<br>
 * Cost = 5*2 + 5*4 = 30</td>
 * <td>U1 - bumps=0.1,0.2,0.3,0.4,0.5 freq=2<br>
 * U1 - bumps=0.5,0.6,0.7,0.8,0.9 freq=4<br>
 * Cost = 5*2 + 5*4 = 30</td>
 * <td>Input is optimal as combined scenario is more costly, eg<br>
 * <br>
 * U1 - bumps=0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9 freq=4<br>
 * Cost = 9*4 = 36</td>
 * </tr>
 * <tr>
 * <td>S1: U1 - bumps=0.1,0.2,0.3,0.4,0.5 freq=2<br>
 * S2: U2 - bumps=0.1,0.2,0.3,0.4,0.6 freq=2<br>
 * Cost = 5*2 + 5*2 = 20</td>
 * <td>U1 - bumps=0.1,0.2,0.3,0.4,0.5 freq=2<br>
 * U2 - bumps=0.1,0.2,0.3,0.4,0.6 freq=2<br>
 * Cost = 5*2 + 5*2 = 20</td>
 * <td>Scenarios from different underlying assets cannot be combined</td>
 * </tr>
 * </tbody>
 * </table>
 * <p>
 * On the flip side, scenario with bumps 0.1, 0.2, 0.3, 0.4 and frequency 5 can <b><i>NOT</i></b> be an optimal to scenario with bump 0.1 and 0.2, frequency 10 due to the decrease in frequency.
 * Likewise, it can <b><i>NOT</i></b> be an optimal to scenario with bump 0.1, 0.2, 0.3, 0.4, 0.5, frequency 5 due to missing of bump 0.5.
 * </p>
 */
public interface ScenarioOptimizer {

    /**
     * Determine the optimal from the input
     * 
     * @param originalScenarios
     *            collection of scenarios with different underlying assets, frequencies and bumps
     * @return optimal of the original, can be the same as original if original has been the optimal solution already
     */
    Collection<Scenario> optimize(Collection<Scenario> originalScenarios);
}
