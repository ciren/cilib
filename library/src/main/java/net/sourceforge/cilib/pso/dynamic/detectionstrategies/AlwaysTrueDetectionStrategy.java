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

/**
 * Detection strategy that always return true. For environment that are constantly changing.
 */
public class AlwaysTrueDetectionStrategy extends EnvironmentChangeDetectionStrategy {

    /**
     *
     */
    private static final long serialVersionUID = 4357366261946609149L;

    public AlwaysTrueDetectionStrategy() {
    }

    public AlwaysTrueDetectionStrategy(AlwaysTrueDetectionStrategy copy) {
    }

    @Override
    public AlwaysTrueDetectionStrategy clone() {
        return new AlwaysTrueDetectionStrategy(this);
    }


    /**
     *
     * @param algorithm PSO algorithm that operates in a dynamic environment
     * @return true
     */
    @Override
    public <A extends HasTopology & Algorithm & HasNeighbourhood> boolean detect(A algorithm) {
        return true;
    }

    public AlwaysTrueDetectionStrategy getClone(){
        return new AlwaysTrueDetectionStrategy();
    }
}


