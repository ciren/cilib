/*
 * ChebyshevDistanceMeasure.java
 *
 * Created on June 18, 2007, 08:21
 *
 * Copyright (C) 2003 - 2007
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.util;

import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.cilib.type.types.Vector;

/**
 * Chebyshev Distance is a special case of the {@link net.sourceforge.cilib.util.MinkowskiMetric Minkowski Metric} with 'alpha' := infinity.
 * It calculates the distance between to vectors as the largest coordinate difference between both vectors.
 * @author Olusegun Olorunda
 */
public class ChebyshevDistanceMeasure extends MinkowskiMetric {

	public ChebyshevDistanceMeasure() {
		// alpha cannot be directly instantiated to infinity :-)
	}

	public double distance(Vector x, Vector y) {
		/*
		 * TODO: Consider re-implementing for different sized vectors, especially as everything is
		 * equivalent relative to infinity
		 */
		if (x.getDimension() != y.getDimension())
			throw new IllegalArgumentException("Cannot calculate Chebyshev Metric for vectors of different dimensions");

		double maxDistance = 0.0;
		for (int i = 0; i < x.getDimension(); ++i) {
			double distance = Math.abs(x.getReal(i) - y.getReal(i));
			if (distance > maxDistance)
				maxDistance = distance;
		}

		return maxDistance;
	}

	public double distance(Collection<? extends Number> x, Collection<? extends Number> y) {
		/*
		 * TODO: Consider re-implementing for different sized collections, especially as everything is
		 * equivalent relative to infinity
		 */
		if (x.size() != y.size())
			throw new IllegalArgumentException("Cannot calculate Chebyshev Metric for vectors of different dimensions");

		Iterator<? extends Number> i = x.iterator();
		Iterator<? extends Number> j = y.iterator();
		double maxDistance = 0.0;

		for (; i.hasNext() && j.hasNext();) {
			double distance = Math.abs(i.next().doubleValue() - j.next().doubleValue());
			if (distance > maxDistance)
				maxDistance = distance;
		}

		return maxDistance;
	}

	public void setAlpha(int a) {
		throw new IllegalArgumentException("The 'alpha' parameter of the Chebyshev Distance Measure cannot be set directly");
	}
}
