/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.crossover.velocityprovider;

import fj.F2;
import java.util.List;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.StructuredType;

/**
 * These classes determine what an offspring's velocity becomes after creation.
 */
public abstract class OffspringVelocityProvider extends F2<List<Particle>, Particle, StructuredType> {
    @Override
    public abstract StructuredType f(List<Particle> parent, Particle offspring);
}
