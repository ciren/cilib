/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.pso.guideprovider;

import fj.data.List;
import net.cilib.algorithm.AbstractAlgorithm;
import net.cilib.pso.particle.Particle;
import net.cilib.entity.Topologies;
import net.cilib.entity.comparator.RelaxedNonDominatedFitnessComparator;
import net.cilib.pso.PSO;
import net.cilib.type.types.container.StructuredType;

/**
 *
 */
public class RelaxedNonDominatedGuideProvider implements GuideProvider {

    @Override
    public RelaxedNonDominatedGuideProvider getClone() {
        return this;
    }

    @Override
    public StructuredType get(Particle particle) {
        PSO pso = (PSO) AbstractAlgorithm.get();
        List<Particle> topology = pso.getTopology();
        Particle gbest = Topologies.getBestEntity(topology, new RelaxedNonDominatedFitnessComparator());

        return gbest.getBestPosition();
    }

}
