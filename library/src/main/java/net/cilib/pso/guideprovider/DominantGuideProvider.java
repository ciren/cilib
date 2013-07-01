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
import net.cilib.entity.comparator.DominantFitnessComparator;
import net.cilib.pso.PSO;
import net.cilib.type.types.container.StructuredType;

/**
 *
 */
public class DominantGuideProvider implements GuideProvider {

    @Override
    public DominantGuideProvider getClone() {
        return this;
    }

    @Override
    public StructuredType get(Particle particle) {
        PSO pso = (PSO) AbstractAlgorithm.get();
        List<Particle> topology = pso.getTopology();
        Particle gbest = Topologies.getBestEntity(topology, new DominantFitnessComparator());

        return gbest.getBestPosition();
    }

}
