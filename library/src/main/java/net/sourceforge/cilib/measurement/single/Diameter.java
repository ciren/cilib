/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.visitor.DiameterVisitor;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Real;

/**
 * Calculates the swarm diameter as the maximum euclidean distance between any
 * two particles.
 *
 *
 *
 */
public class Diameter implements Measurement<Real> {
    private static final long serialVersionUID = 5136996282460480831L;

    /**
     * {@inheritDoc}
     */
    @Override
    public Diameter getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        SinglePopulationBasedAlgorithm popAlg = (SinglePopulationBasedAlgorithm) algorithm;
        fj.data.List<Entity> topology = popAlg.getTopology();
        return Real.valueOf(new DiameterVisitor().f(topology));
    }

}
