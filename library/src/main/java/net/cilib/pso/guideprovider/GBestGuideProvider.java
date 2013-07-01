/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.pso.guideprovider;

import net.cilib.algorithm.AbstractAlgorithm;
import net.cilib.entity.Topologies;
import net.cilib.entity.comparator.SocialBestFitnessComparator;
import net.cilib.pso.PSO;
import net.cilib.pso.particle.Particle;
import net.cilib.type.types.container.StructuredType;

public class GBestGuideProvider implements GuideProvider {

    public GBestGuideProvider getClone() {
        return this;
    }

    public StructuredType get(Particle particle) {
        PSO pso = (PSO) AbstractAlgorithm.get();
        fj.data.List<Particle> topology = pso.getTopology();
        Particle gbest = Topologies.getBestEntity(topology, new SocialBestFitnessComparator());

        return gbest.getBestPosition();
    }

}
