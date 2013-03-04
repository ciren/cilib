/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.type.parser;

import com.google.common.collect.Maps;
import java.util.Map;
import net.sourceforge.cilib.type.types.*;

/**
 * This class represents the bounds information for all Numeric types in
 * CIlib. This implementation is done by using the Flyweight design pattern.
 *
 */
public class BoundsFactory {
    private final Map<Integer, Bounds> bounds = Maps.newHashMap();

    BoundsFactory() {
    }

    /**
     * Create a new {@code Bounds} object, or alternatively return a pre-created instance.
     * @param bounds The bounds object to base the creation off.
     * @return The common {@code Bounds} instance.
     */
    public Bounds create(Bounds bounds) {
        return create(bounds.getLowerBound(), bounds.getUpperBound());
    }

    /**
     * Create a new {@code Bounds} instance. The created instance is based
     * on the provided {@code lowerBound} and {@code upperBound} values.
     * @param lowerBound The lower bound of the bound definition.
     * @param upperBound The upper bound of the bound definition.
     * @return The newly created or previously created {@code Bounds} instance.
     */
    public Bounds create(double lowerBound, double upperBound) {
        int key = Double.valueOf(lowerBound).hashCode() + Double.valueOf(upperBound).hashCode();

        if (!bounds.containsKey(key)) {
            Bounds bound = new Bounds(lowerBound, upperBound);
            bounds.put(key, bound);
        }

        return bounds.get(key);
    }
}
