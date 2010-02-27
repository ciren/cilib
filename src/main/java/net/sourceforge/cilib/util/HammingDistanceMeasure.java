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

import com.google.common.base.Preconditions;

import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.cilib.type.types.Numeric;

/**
 * Calculates the Hamming distance between two points. As an example, the Hamming distance between the two vectors<br/>
 * [1, 2, 3, 4, 5] and<br/>
 * [1, 5, 3, 2, 4]<br/>
 * is 3.<br/>
 * In other words, it counts the number of elements that are different.
 *
 * @author Theuns Cloete
 */
public class HammingDistanceMeasure implements DistanceMeasure {
    /**
     * {@inheritDoc}
     */
    @Override
    public double distance(Collection<? extends Numeric> x, Collection<? extends Numeric> y) {
        Preconditions.checkState(x.size() == y.size(), "Cannot calculate Hamming Distance for vectors of different dimensions: " + x.size() + " != " + y.size());
        Iterator<? extends Numeric> xIterator = x.iterator();
        Iterator<? extends Numeric> yIterator = y.iterator();
        int distance = 0;

        while (xIterator.hasNext() && yIterator.hasNext()) {
            double xElement = xIterator.next().doubleValue();
            double yElement = yIterator.next().doubleValue();

            if (xElement != yElement) {
                ++distance;
            }
        }
        return distance;
    }
}
