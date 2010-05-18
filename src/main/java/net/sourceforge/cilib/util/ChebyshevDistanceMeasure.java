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
import java.util.Iterator;

import net.sourceforge.cilib.type.types.Numeric;

/**
 * Chebyshev Distance is a special case of the
 * {@link net.sourceforge.cilib.util.MinkowskiMetric Minkowski Metric} with 'alpha' := infinity. It
 * calculates the distance between to vectors as the largest coordinate difference between both
 * vectors.
 * @author Olusegun Olorunda
 */
public class ChebyshevDistanceMeasure extends MinkowskiMetric {

    /**
     * Create an instance of the {@linkplain ChebyshevDistanceMeasure}.
     */
    public ChebyshevDistanceMeasure() {
        // alpha cannot be directly instantiated to infinity :-)
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double distance(Collection<? extends Numeric> x, Collection<? extends Numeric> y) {
        /*
         * TODO: Consider re-implementing for different sized vectors, especially as everything is
         * equivalent relative to infinity
         */
        if (x.size() != y.size()) {
            throw new IllegalArgumentException("Cannot calculate Chebyshev Metric for vectors of different dimensions");
        }

        Iterator<? extends Numeric> xIterator = x.iterator();
        Iterator<? extends Numeric> yIterator = y.iterator();

        double maxDistance = 0.0;
        for (int i = 0; i < x.size(); ++i) {
            Numeric xElement = (Numeric) xIterator.next();
            Numeric yElement = (Numeric) yIterator.next();

            double distance = Math.abs(xElement.doubleValue() - yElement.doubleValue());
            if (distance > maxDistance) {
                maxDistance = distance;
            }
        }

        return maxDistance;
    }

    /**
     * {@inheritDoc}
     */
    public void setAlpha(int a) {
        throw new IllegalArgumentException("The 'alpha' parameter of the Chebyshev Distance Measure cannot be set directly");
    }
}
