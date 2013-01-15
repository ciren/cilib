/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.velocityprovider;


import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Cloneable;

/**
 *
 */
public interface VelocityProvider extends Cloneable {

    /**
     * Clone the <tt>VelocityProvider</tt> object.
     * @return A cloned <tt>VelocityProvider</tt>
     */
    @Override
    VelocityProvider getClone();

    /**
     * Perform the velocity update operation on the specified <tt>Particle</tt>.
     * @param particle The <tt>Particle</tt> to apply the operation on.
     */
    Vector get(Particle particle);
}
