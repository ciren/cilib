/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.guideprovider;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.comparator.NonDominatedFitnessComparator;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.container.StructuredType;

/**
 *
 */
public class NonDominatedGuideProvider implements GuideProvider {

    @Override
    public NonDominatedGuideProvider getClone() {
        return this;
    }

    @Override
    public StructuredType get(Particle particle) {
        PSO pso = (PSO) AbstractAlgorithm.get();
        Topology<Particle> topology = pso.getTopology();
        Particle gbest = Topologies.getBestEntity(topology, new NonDominatedFitnessComparator());

        return gbest.getBestPosition();
    }

}
