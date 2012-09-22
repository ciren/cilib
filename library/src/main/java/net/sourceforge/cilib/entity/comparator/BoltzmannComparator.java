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
package net.sourceforge.cilib.entity.comparator;

import java.io.Serializable;
import java.util.Comparator;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.LinearlyVaryingControlParameter;
import net.sourceforge.cilib.controlparameter.UpdateOnIterationControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFunction;
import net.sourceforge.cilib.math.random.UniformDistribution;

public class BoltzmannComparator<E extends Entity> implements Comparator<E>, Serializable {
    
    private ProbabilityDistributionFunction distribution;
    private ControlParameter tempSchedule;
    
    public BoltzmannComparator() {
        this.distribution = new UniformDistribution();

        LinearlyVaryingControlParameter cp = new LinearlyVaryingControlParameter();
        cp.setInitialValue(100);
        cp.setFinalValue(1);

        UpdateOnIterationControlParameter outerCP = new UpdateOnIterationControlParameter();
        outerCP.setDelegate(cp);
        
        this.tempSchedule = outerCP;
    }

    @Override
    public int compare(E t, E t1) {
        int sign = -t.compareTo(t1);
        double diff = Math.abs(t.getFitness().getValue() - t1.getFitness().getValue());
        double probability = 1 / (1 + Math.exp(sign * diff / tempSchedule.getParameter()));

        if (distribution.getRandomNumber() > probability) {
            return -1;
        }
        
        return 1;
    }

    public ProbabilityDistributionFunction getDistribution() {
        return distribution;
    }

    public void setDistribution(ProbabilityDistributionFunction distribution) {
        this.distribution = distribution;
    }

    public ControlParameter getTempSchedule() {
        return tempSchedule;
    }

    public void setTempSchedule(ControlParameter tempSchedule) {
        this.tempSchedule = tempSchedule;
    }
}
