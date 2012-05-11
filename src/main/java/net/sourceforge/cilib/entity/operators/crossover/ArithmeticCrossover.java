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
package net.sourceforge.cilib.entity.operators.crossover;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.List;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.RandomControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 */
public class ArithmeticCrossover extends CrossoverStrategy {
    
    private ControlParameter lambda;
    
    public ArithmeticCrossover() {
        this.lambda = new RandomControlParameter(new UniformDistribution());
    }
    
    public ArithmeticCrossover(ArithmeticCrossover copy) {
        super(copy);
        this.lambda = copy.lambda.getClone();
    }

    @Override
    public CrossoverStrategy getClone() {
        return new ArithmeticCrossover(this);
    }

    @Override
    public List<? extends Entity> crossover(List<? extends Entity> parentCollection) {
        Preconditions.checkArgument(parentCollection.size() >= 2, "There must be at least two parents to perform arithmetic crossover.");
        
        List<Entity> offspring = Lists.newLinkedList();

        Entity o1 = parentCollection.get(0).getClone();
        Entity o2 = parentCollection.get(1).getClone();
        Vector o1Vec = (Vector) o1.getCandidateSolution();
        Vector o2Vec = (Vector) o2.getCandidateSolution();
        double value = lambda.getParameter();

        o1.setCandidateSolution(o1Vec.multiply(value).plus(o2Vec.multiply(1.0 - value)));
        o2.setCandidateSolution(o2Vec.multiply(value).plus(o1Vec.multiply(1.0 - value)));

        offspring.add(o1);
        offspring.add(o2);
        
        return offspring;
    }

    public ControlParameter getLambda() {
        return lambda;
    }

    public void setLambda(ControlParameter lambda) {
        this.lambda = lambda;
    }
}
