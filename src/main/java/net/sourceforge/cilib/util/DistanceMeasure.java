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
package net.sourceforge.cilib.util;

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
