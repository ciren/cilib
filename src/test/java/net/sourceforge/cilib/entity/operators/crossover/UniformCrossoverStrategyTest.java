/**
 * Copyright (C) 2003 - 2009
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
package net.sourceforge.cilib.entity.operators.crossover;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;
import net.sourceforge.cilib.util.calculator.FitnessCalculator;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author gpampara
 */
public class UniformCrossoverStrategyTest {

    @Test
    public void offspringCreation() {
        Individual i1 = new Individual();
        Individual i2 = new Individual();

        i1.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vectors.create(0.0, 1.0, 2.0, 3.0, 4.0));
        i2.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vectors.create(5.0, 6.0, 7.0, 8.0, 9.0));

        i1.setFitnessCalculator(new MockFitnessCalculator());
        i2.setFitnessCalculator(new MockFitnessCalculator());

        List<Entity> parents = new ArrayList<Entity>();
        parents.add(i1);
        parents.add(i2);

        UniformCrossoverStrategy crossoverStrategy = new UniformCrossoverStrategy();
        crossoverStrategy.setCrossoverProbability(new ConstantControlParameter(1.0));
        List<Entity> children = crossoverStrategy.crossover(parents);

        Vector child1 = (Vector) children.get(0).getCandidateSolution();
        Vector child2 = (Vector) children.get(1).getCandidateSolution();
        Vector parent1 = (Vector) i1.getCandidateSolution();
        Vector parent2 = (Vector) i2.getCandidateSolution();

        Assert.assertEquals(2, children.size());
        for (int i = 0; i < i1.getDimension(); i++) {
            Assert.assertNotSame(child1.get(i), parent1.get(i));
            Assert.assertNotSame(child1.get(i), parent2.get(i));
            Assert.assertNotSame(child2.get(i), parent1.get(i));
            Assert.assertNotSame(child2.get(i), parent2.get(i));
        }
    }

    /**
     * This kind of thing would be awesome to just imject with Guice.
     */
    private class MockFitnessCalculator implements FitnessCalculator<Individual> {
        private static final long serialVersionUID = -7744000392817056355L;

        @Override
        public FitnessCalculator<Individual> getClone() {
            return this;
        }

        @Override
        public Fitness getFitness(Individual entity) {
            return InferiorFitness.instance();
        }
    }

}