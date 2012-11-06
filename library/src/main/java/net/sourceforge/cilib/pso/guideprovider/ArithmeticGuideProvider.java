/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.guideprovider;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.comparator.SocialBestFitnessComparator;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.type.types.container.Vector;

public class ArithmeticGuideProvider implements GuideProvider {

    public GuideProvider getClone() {
        return this;
    }

    public StructuredType get(Particle particle) {
        PSO pso = (PSO) AbstractAlgorithm.get();
        Topology<Particle> topology = pso.getTopology();
        Particle gbestParticle = Topologies.getBestEntity(topology, new SocialBestFitnessComparator());
        Vector gbest = (Vector) gbestParticle.getBestPosition();
        Vector pbest = (Vector) particle.getBestPosition();
        UniformDistribution uniform = new UniformDistribution();
        double r = uniform.getRandomNumber();

        return pbest.multiply(r).plus(gbest.multiply(1.0 - r));

    }

}
