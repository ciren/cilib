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
package net.sourceforge.cilib.measurement.single;

import java.util.ArrayList;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Real;

/**
 * This measurement functions as a wrapper for Measurement<Real>. It collects
 * the wrapped measurement at each iteration of the algorithm. It then returns
 * the average of the measurements taken so far.
 * 
 * This wrapper ensures that new measurements are only taken when the algorithm
 * has performed a new iteration. However it does not ensure that measurements
 * are taken at each iteration; it only takes new measurements when it is called.
 * This implies that the measurement resolution must be set to 1 for it to function
 * correctly.
 */
public class CollectiveMeanOfMeasurement implements Measurement<Real> {
	private Measurement<Real> measurement;
    private ArrayList<Real> results;
	private int iterations;

	public CollectiveMeanOfMeasurement() {
		measurement = new Fitness();
		results = new ArrayList<Real>();
		iterations = -1;
	}

	public CollectiveMeanOfMeasurement(CollectiveMeanOfMeasurement rhs) {
		measurement = rhs.measurement.getClone();
		iterations = rhs.iterations;
		results = new ArrayList<Real>();
		for (Real element : rhs.results)
			results.add(element.getClone());
	}

	@Override
	public CollectiveMeanOfMeasurement getClone() {
		return new CollectiveMeanOfMeasurement(this);
	}
	
    /**
     * {@inheritDoc}
     */
    @Override
    public Real getValue(Algorithm algorithm) {
		//ensure that new measurements are only taken when new iterations occured
		if (iterations < algorithm.getIterations()) {
        	iterations = algorithm.getIterations();
			results.add(measurement.getValue(algorithm));
		}
		
		double total = 0;
		for (Real result : results) {
			total += result.doubleValue();
		}
		
        return Real.valueOf(total / results.size());
    }
	
	public Measurement<Real> getMeasurement() {
		return measurement;
	}
	
	public void setMeasurement(Measurement<Real> measurement) {
		this.measurement = measurement;
	}
}
