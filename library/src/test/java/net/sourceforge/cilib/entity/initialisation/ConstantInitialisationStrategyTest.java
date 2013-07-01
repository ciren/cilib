/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.entity.initialisation;

import net.cilib.entity.initialisation.ConstantInitialisationStrategy;
import net.cilib.ec.Individual;
import net.cilib.entity.EntityType;
import net.cilib.math.Maths;
import net.cilib.type.types.Real;
import net.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

/**
 *
 */
public class ConstantInitialisationStrategyTest {

    @Test
    public void testGetClone() {
        ConstantInitialisationStrategy strategy = new ConstantInitialisationStrategy(1.0);
        ConstantInitialisationStrategy clone = strategy.getClone();

        Assert.assertNotSame(strategy, clone);
        Assert.assertEquals(strategy.getConstant(), clone.getConstant(), Maths.EPSILON);
    }

    @Test
    public void initialise() {
        Vector vector = Vector.of(1.0, 1.0, 1.0);
        Individual individual = new Individual();
        individual.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.copyOf(vector));

        ConstantInitialisationStrategy<Individual> initialisationStrategy = new ConstantInitialisationStrategy<Individual>();
        initialisationStrategy.initialise(EntityType.CANDIDATE_SOLUTION, individual);

        Vector chromosome = (Vector) individual.getCandidateSolution();

        for (int i = 0; i < vector.size(); i++) {
            Assert.assertThat(vector.doubleValueOf(i), is(not(chromosome.doubleValueOf(i))));
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void invalidInitialise() {
        Individual individual = new Individual();
        individual.getProperties().put(EntityType.CANDIDATE_SOLUTION, Real.valueOf(0.0));

        ConstantInitialisationStrategy<Individual> initialisationStrategy = new ConstantInitialisationStrategy<Individual>();

        initialisationStrategy.initialise(EntityType.CANDIDATE_SOLUTION, individual);
    }
}
