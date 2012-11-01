/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic.detectionstrategies;


import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;

/**
 * Detection strategy that always return true. For environment that are constantly changing.
 */
public class AlwaysTrueDetectionStrategy<E extends PopulationBasedAlgorithm> extends EnvironmentChangeDetectionStrategy<E> {

    /**
     *
     */
    private static final long serialVersionUID = 4357366261946609149L;

    public AlwaysTrueDetectionStrategy() {
    }

    public AlwaysTrueDetectionStrategy(AlwaysTrueDetectionStrategy<E> copy) {
    }

    @Override
    public AlwaysTrueDetectionStrategy<E> clone() {
        return new AlwaysTrueDetectionStrategy<E>(this);
    }


    /**
     *
     * @param algorithm PSO algorithm that operates in a dynamic environment
     * @return true
     */
    public boolean detect(PopulationBasedAlgorithm algorithm) {
        return true;
    }

    public AlwaysTrueDetectionStrategy getClone(){
        return new AlwaysTrueDetectionStrategy();
    }



}


