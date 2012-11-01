/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic.detectionstrategies;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;

public class PeriodicDetectionStrategy<E extends PopulationBasedAlgorithm> extends EnvironmentChangeDetectionStrategy<E> {
    private static final long serialVersionUID = 4079212153655661164L;

    public PeriodicDetectionStrategy() {
        // super() is automatically called
    }

    public PeriodicDetectionStrategy(EnvironmentChangeDetectionStrategy<E> rhs) {
        super(rhs);
    }

    @Override
    public PeriodicDetectionStrategy<E> getClone() {
        return new PeriodicDetectionStrategy<E>(this);
    }

    @Override
    public boolean detect(PopulationBasedAlgorithm algorithm) {
        if (algorithm.getIterations() != 0 && algorithm.getIterations() % interval == 0) {
            return true;
        }
        return false;
    }
}
