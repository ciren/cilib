/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic.detectionstrategies;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.HasNeighbourhood;
import net.sourceforge.cilib.algorithm.population.HasTopology;

public class PeriodicDetectionStrategy extends EnvironmentChangeDetectionStrategy {
    private static final long serialVersionUID = 4079212153655661164L;

    public PeriodicDetectionStrategy() {
        // super() is automatically called
    }

    public PeriodicDetectionStrategy(EnvironmentChangeDetectionStrategy rhs) {
        super(rhs);
    }

    @Override
    public PeriodicDetectionStrategy getClone() {
        return new PeriodicDetectionStrategy(this);
    }

    @Override
    public <A extends HasTopology & Algorithm & HasNeighbourhood> boolean detect(A algorithm) {
        if (algorithm.getIterations() != 0 && algorithm.getIterations() % interval == 0) {
            return true;
        }
        return false;
    }
}
