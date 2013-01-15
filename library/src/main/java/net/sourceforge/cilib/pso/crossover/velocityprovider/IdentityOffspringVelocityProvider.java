/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.crossover.velocityprovider;

import java.util.List;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.StructuredType;

/**
 * This OffspringVelocityProvider sets an offspring's velocity to its parent's
 * velocity.
 */
public class IdentityOffspringVelocityProvider extends OffspringVelocityProvider {
    @Override
    public StructuredType f(List<Particle> parent, Particle offspring) {
        return offspring.getVelocity();
    }
}
