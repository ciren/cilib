/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.crossover.particleprovider;

import fj.F2;
import java.util.List;
import net.sourceforge.cilib.pso.particle.Particle;

/**
 * These classes are used in CrossoverSelection to determine which candidate
 * particle can be replaced.
 */
public abstract class ParticleProvider extends F2<List<Particle>, Particle, Particle> {
    @Override
    public abstract Particle f(List<Particle> parents, Particle offspring);
}
