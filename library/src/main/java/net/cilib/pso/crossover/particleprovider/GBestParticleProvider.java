/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.pso.crossover.particleprovider;

import fj.data.List;
import net.cilib.algorithm.AbstractAlgorithm;
import net.cilib.algorithm.population.SinglePopulationBasedAlgorithm;

import net.cilib.entity.Topologies;
import net.cilib.entity.comparator.SocialBestFitnessComparator;
import net.cilib.pso.particle.Particle;

public class GBestParticleProvider extends ParticleProvider {

    @Override
    public Particle f(List<Particle> parents, Particle offspring) {
        return Topologies.getBestEntity((List<Particle>) ((SinglePopulationBasedAlgorithm) AbstractAlgorithm.get()).getTopology(), new SocialBestFitnessComparator());
    }

}
