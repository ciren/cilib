/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */

package net.cilib.measurement.single;

import net.cilib.algorithm.Algorithm;
import net.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.cilib.entity.Entity;
import net.cilib.measurement.Measurement;
import net.cilib.type.types.Real;

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
        SinglePopulationBasedAlgorithm pba = (SinglePopulationBasedAlgorithm) algorithm;

        double collectiveFitness = 0.0;
        fj.data.List<Entity> local = pba.getTopology();
        for (Entity e : local) {
            collectiveFitness += e.getFitness().getValue();
        }

        return Real.valueOf(collectiveFitness);
    }

}
