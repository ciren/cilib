/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.crossover.particleprovider;

import java.util.List;
import net.sourceforge.cilib.pso.particle.Particle;

/**
 * This ParticleProvider selects a particle's nBest to be replaced.
 */
public class NBestParticleProvider extends ParticleProvider {
    @Override
    public Particle f(List<Particle> parents, Particle offspring) {
        return offspring.getNeighbourhoodBest();
    }
}
