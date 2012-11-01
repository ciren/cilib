/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */

package net.sourceforge.cilib.type.types;

/**
 * Interface to define bounds. These bounds are generally / currently defined
 * to be a single upper and lower bound. There may be situations whereby this
 * functionality may need to be extended to an arbitrary amount of bounds. This
 * is still very much in debate.
 *
 */
public interface BoundedType {

    Bounds getBounds();

}
