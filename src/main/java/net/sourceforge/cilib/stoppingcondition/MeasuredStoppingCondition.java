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
package net.sourceforge.cilib.stoppingcondition;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.measurement.generic.Iterations;
import net.sourceforge.cilib.type.types.Numeric;

public class MeasuredStoppingCondition implements StoppingCondition<Algorithm> {
    
    private CompletionCalculator predicate;
    private double target;
    private Measurement<? extends Numeric> measurement;
    
    public MeasuredStoppingCondition() {
        this(new Iterations(), new Maximum(), 1000);
    }
    
    public MeasuredStoppingCondition(Measurement measurement, CompletionCalculator predicate, double target) {
        this.measurement = measurement;
        this.predicate = predicate;
        this.target = target;
    }

    @Override
    public double getPercentageCompleted(Algorithm algorithm) {
        return predicate.getPercentage(measurement.getValue(algorithm).doubleValue(), target);
    }

    @Override
    public boolean apply(Algorithm algorithm) {
        return predicate.apply(measurement.getValue(algorithm).doubleValue(), target);
    }

    public double getTarget() {
        return target;
    }

    public CompletionCalculator getPredicate() {
        return predicate;
    }

    public Measurement<? extends Numeric> getMeasurement() {
        return measurement;
    }

    public void setTarget(double target) {
        this.target = target;
    }

    public void setPredicate(CompletionCalculator predicate) {
        this.predicate = predicate;
    }

    public void setMeasurement(Measurement<? extends Numeric> measurement) {
        this.measurement = measurement;
    }
}
