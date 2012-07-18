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
package net.sourceforge.cilib.entity.operators.crossover.real;

import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.List;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.RandomControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.type.types.container.Vector;

public class RootProbabilityCrossoverStrategy implements CrossoverStrategy {
    
    private ControlParameter lambda;
    
    public RootProbabilityCrossoverStrategy() {
        this.lambda = new RandomControlParameter(new UniformDistribution());
    }
    
    public RootProbabilityCrossoverStrategy(RootProbabilityCrossoverStrategy copy) {
        this.lambda = copy.lambda.getClone();
    }

    @Override
    public RootProbabilityCrossoverStrategy getClone() {
        return new RootProbabilityCrossoverStrategy(this);
    }

    @Override
    public <E extends Entity> List<E> crossover(List<E> parentCollection) {
        Preconditions.checkArgument(parentCollection.size() == 2, "RootProbabilityCrossoverStrategy requires 2 parents.");

        E o1 = (E) parentCollection.get(0).getClone();
        E o2 = (E) parentCollection.get(1).getClone();
        
        Vector o1Vec = (Vector) o1.getCandidateSolution();
        Vector o2Vec = (Vector) o2.getCandidateSolution();
        
        double value = Math.sqrt(lambda.getParameter());

        o1.setCandidateSolution(o1Vec.multiply(value).plus(o2Vec.multiply(1.0 - value)));
        o2.setCandidateSolution(o2Vec.multiply(value).plus(o1Vec.multiply(1.0 - value)));

        return Arrays.asList(o1, o2);
    }

    public ControlParameter getLambda() {
        return lambda;
    }

    public void setLambda(ControlParameter lambda) {
        this.lambda = lambda;
    }

    @Override
    public int getNumberOfParents() {
        return 2;
    }
}
