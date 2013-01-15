/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.distancemeasure;

import java.util.Collection;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.StructuredType;

/**
 * Definition of how to determine the distance between two objects.
 *
 */
public interface DistanceMeasure {

    /**
     * Determine the distance between the two provided {@link StructuredType}
     * instances.
     * @param x The first object from which the calculation is to be performed.
     * @param y The second object from which the calculation is to be performed.
     * @return The distance between the provided instances.
     */
    double distance(Collection<? extends Numeric> x, Collection<? extends Numeric> y);

}
