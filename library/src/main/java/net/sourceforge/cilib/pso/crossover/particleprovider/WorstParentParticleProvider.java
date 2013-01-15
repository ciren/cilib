/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.crossover.particleprovider;

import java.util.List;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.util.selection.Samples;
import net.sourceforge.cilib.util.selection.recipes.ElitistSelector;

/**
 * This ParticleProvider selects a particle's worst parent to be replaced.
 */
public class WorstParentParticleProvider extends ParticleProvider {
    @Override
    public Particle f(List<Particle> parents, Particle offspring) {
        return new ElitistSelector<Particle>().on(parents).select(Samples.last()).get(0);
    }
}
