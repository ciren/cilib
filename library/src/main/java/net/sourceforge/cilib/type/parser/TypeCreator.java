/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type.parser;

import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Type;

/**
 * Interface defining the manner in which <tt>Type</tt> object is to be created
 * in a standard fashion.
 *
 * The creation of the Type can be done using specified bounds or with no bounds.
 *
 */
interface TypeCreator {

    /**
     * Create the type in a standard fashion. If the type has the concept of bounds,
     * the maximum and minimum values for the bounds are used to bound the created
     * object.
     *
     * @return The newly created <tt>Type</tt>
     */
    Type create();

    /**
     * Create the type with the specified value.
     * @param value The value for the {@code Type}.
     * @return The created {@code Type} with the provided value.
     */
    Type create(double value);


    /**
     * Create the type using the bounds <tt>lower</tt> and <tt>upper</tt>.
     *
     * @param bounds The {@code Bounds} for the type.
     * @return The newly created <tt>Type</tt> object using the specified bounds
     */
    Type create(Bounds bounds);

}
