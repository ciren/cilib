/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.util.Cloneable;


/**
 * All measurements must implement this interface. The measurement must return
 * the value of what it is measuring given the algorithm that it is measuring.
 *
 * @param <E> The return {@code Type}.
 */
public interface Measurement<E extends Type> extends Cloneable {

    /**
     * {@inheritDoc}
     */
    @Override
    Measurement<E> getClone();

    /**
     * Gets the value of the measurement. The representation of the measurement will be based
     * on the domain string defined.
     *
     * @param algorithm The algorithm to obtain the measurement from.
     * @return The <tt>Type</tt> representing the value of the measurement.
     */
    E getValue(Algorithm algorithm);
}
