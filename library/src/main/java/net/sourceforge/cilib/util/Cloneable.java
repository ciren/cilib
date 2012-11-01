/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util;

import java.io.Serializable;

/**
 * <p>
 * This interface provides a publicly available {@code getClone()} method that
 * can be used to obtain a cloned version of the instance on which the
 * method was invoked.
 * </p>
 * <p>
 * This interface was added in order to bypass the current issues with
 * {@code clone()} as provided with the JDK. The protected status of
 * {@code Object.clone()} prevented some operations that were needed for CILib.
 * </p>
 * <p>
 * Another consideration was the problems associated with {@code Object.clone()}
 * as discussed by Joshua Bloch.
 * </p>
 *
 * @see "Item 11 of the 2<sup>nd</sup> edition of <emp>Effective Java</emp> by Joshua Bloch."
 */
public interface Cloneable extends Serializable {

    /**
     * Create a cloned copy of the current object and return it. In general
     * the created copy will be a deep copy of the provided instance. As
     * a result this operation an be quite expensive if used incorrectly.
     * @return An exact clone of the current object instance.
     * @see Object#clone()
     */
    Object getClone();

}
