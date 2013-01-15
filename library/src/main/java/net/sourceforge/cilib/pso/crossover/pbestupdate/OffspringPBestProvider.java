/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.crossover.pbestupdate;

import fj.F2;
import java.util.List;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.StructuredType;

/**
 * These classes set an offspring's pBest after it is created.
 */
public abstract class OffspringPBestProvider extends F2<List<Particle>, Particle, StructuredType> {
    @Override
    public abstract StructuredType f(List<Particle> parent, Particle offspring);
}
