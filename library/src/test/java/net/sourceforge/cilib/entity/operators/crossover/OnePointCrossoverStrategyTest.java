/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.operators.crossover;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Property;
import net.sourceforge.cilib.entity.operators.CrossoverOperator;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.calculator.FitnessCalculator;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;

/**
 *
 */
public class OnePointCrossoverStrategyTest {

    @Test
    public void offspringCreation() {
        Individual i1 = new Individual();
        Individual i2 = new Individual();

        i1.put(Property.CANDIDATE_SOLUTION, Vector.of(0.0, 1.0, 2.0, 3.0, 4.0));
        i2.put(Property.CANDIDATE_SOLUTION, Vector.of(5.0, 6.0, 7.0, 8.0, 9.0));

        i1.setFitnessCalculator(new MockFitnessCalculator());
        i2.setFitnessCalculator(new MockFitnessCalculator());

        List<Individual> parents = Lists.newArrayList(i1, i2);

        CrossoverOperator crossoverStrategy = new CrossoverOperator();
        crossoverStrategy.setCrossoverStrategy(new OnePointCrossoverStrategy());
        crossoverStrategy.setCrossoverProbability(ConstantControlParameter.of(1.0));
        List<Individual> children = crossoverStrategy.crossover(fj.data.List.iterableList(parents));

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
