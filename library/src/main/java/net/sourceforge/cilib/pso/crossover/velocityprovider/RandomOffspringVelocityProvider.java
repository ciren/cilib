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
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This OffspringVelocityProvider sets an offspring's velocity to a random value
 * within the search space.
 */
public class RandomOffspringVelocityProvider extends OffspringVelocityProvider {
    @Override
    public StructuredType f(List<Particle> parent, Particle offspring) {
        return Vector.newBuilder().copyOf(offspring.getVelocity()).buildRandom();
    }
}
