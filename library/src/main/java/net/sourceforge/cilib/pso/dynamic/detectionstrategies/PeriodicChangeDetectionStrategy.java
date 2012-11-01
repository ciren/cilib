/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic.detectionstrategies;


import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
//import net.sourceforge.cilib.pso.PSO;

/**
 * Detection strategy that returns true periodically after a known number of iteration.
 * For environment where change frequency is known.
 *
 */
public class PeriodicChangeDetectionStrategy<E extends PopulationBasedAlgorithm> extends EnvironmentChangeDetectionStrategy<E> {

    /**
     *
     */
    private static final long serialVersionUID = -5220604087545891206L;

    private int period;

    public PeriodicChangeDetectionStrategy() {
    }

    public PeriodicChangeDetectionStrategy(PeriodicChangeDetectionStrategy<E> copy) {
    }

    @Override
    public PeriodicChangeDetectionStrategy<E> clone() {
        return new PeriodicChangeDetectionStrategy<E>(this);
    }


    /**
     *
     * @param algorithm PSO algorithm that operates in a dynamic environment
     * @return true
     */
    public boolean detect(PopulationBasedAlgorithm algorithm) {
        if (algorithm.getIterations()%period == 0)
            return true;
        return false;
    }

    public AlwaysTrueDetectionStrategy getClone(){
        return new AlwaysTrueDetectionStrategy();
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }



}


