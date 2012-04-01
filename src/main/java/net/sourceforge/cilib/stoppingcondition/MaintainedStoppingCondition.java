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
import net.sourceforge.cilib.measurement.single.diversity.Diversity;

public class MaintainedStoppingCondition implements StoppingCondition<Algorithm> {
    
    private int consecutiveIterations;
    private int count;
    private StoppingCondition<Algorithm> condition;
    private double percentage;
    
    public MaintainedStoppingCondition() {
        this(new MeasuredStoppingCondition(new Diversity(), new Minimum(), 1.0), 10);
    }
    
    public MaintainedStoppingCondition(StoppingCondition condition, int consecutiveIterations) {
        this.consecutiveIterations = consecutiveIterations;
        this.condition = condition;
        this.count = 0;
        this.percentage = 0.0;
    }

    @Override
    public double getPercentageCompleted(Algorithm algorithm) {
        percentage = Math.max(percentage, count / (double) consecutiveIterations);
        return percentage;
    }

    @Override
    public boolean apply(Algorithm algorithm) {
        count = condition.apply(algorithm) ? count + 1 : 0;
        return count >= consecutiveIterations;
    }

    public StoppingCondition<Algorithm> getCondition() {
        return condition;
    }

    public int getConsecutiveIterations() {
        return consecutiveIterations;
    }

    public void setConsecutiveIterations(int consecutiveIterations) {
        this.consecutiveIterations = consecutiveIterations;
    }

    public void setCondition(StoppingCondition<Algorithm> condition) {
        this.condition = condition;
    }
}
