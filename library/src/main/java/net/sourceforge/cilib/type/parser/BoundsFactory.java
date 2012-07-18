/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.type.parser;

import net.sourceforge.cilib.type.types.*;
import com.google.common.collect.Maps;
import java.util.Map;

/**
 * This class represents the bounds information for all Numeric types in
 * CIlib. This implemetnation is done by using the Flyweight design pattern.
 *
 */
public class BoundsFactory {
    private final Map<Integer, Bounds> bounds = Maps.newHashMap();

    BoundsFactory() {
    }

    /**
     * Create a new {@code Bounds} object, or alternatively return a precreated instance.
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
