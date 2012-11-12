/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */

package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Real;

/**
 *
 */
public class CollectiveFitness implements Measurement<Real> {
    private static final long serialVersionUID = 6171032748690594619L;

    @Override
    public Measurement getClone() {
        return this;
    }

    @Override
    public Real getValue(Algorithm algorithm) {
        PopulationBasedAlgorithm pba = (PopulationBasedAlgorithm) algorithm;

        double collectiveFitness = 0.0;
        for (Entity e : pba.getTopology()) {
            collectiveFitness += e.getFitness().getValue();
        }

        return Real.valueOf(collectiveFitness);
    }

}
