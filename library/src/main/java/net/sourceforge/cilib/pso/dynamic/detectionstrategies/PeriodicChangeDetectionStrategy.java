/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic.detectionstrategies;


//import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.HasNeighbourhood;
import net.sourceforge.cilib.algorithm.population.HasTopology;

/**
 * Detection strategy that returns true periodically after a known number of iteration.
 * For environment where change frequency is known.
 *
 */
public class PeriodicChangeDetectionStrategy extends EnvironmentChangeDetectionStrategy {

    /**
     *
     */
    private static final long serialVersionUID = -5220604087545891206L;

    private int period;

    public PeriodicChangeDetectionStrategy() {
    }

    public PeriodicChangeDetectionStrategy(PeriodicChangeDetectionStrategy copy) {
    }

    @Override
    public PeriodicChangeDetectionStrategy clone() {
        return new PeriodicChangeDetectionStrategy(this);
    }


    /**
     *
     * @param algorithm PSO algorithm that operates in a dynamic environment
     * @return true
     */
    public <A extends HasTopology & Algorithm & HasNeighbourhood> boolean detect(A algorithm) {
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


