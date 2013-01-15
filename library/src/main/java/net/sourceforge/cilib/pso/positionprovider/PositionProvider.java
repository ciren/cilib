/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.positionprovider;

import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Cloneable;


/**
 * TODO: Complete this javadoc.
 *
 */
public interface PositionProvider extends Cloneable {

    /**
     * Clone the strategy by creating a new object with the same contents and values
     * as the current object.
     *
     * @return A clone of the current <tt>PositionProvider</tt>
     */
    PositionProvider getClone();

    /**
     * Update the position of the <tt>Particle</tt>.
     *
     * @param particle The <tt>Particle</tt> to perform the position update on.
     */
    Vector get(Particle particle);

}
