/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.initialisation;

import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MaskedInitialisationStrategyTest {

    @Test
    public void initialise() {
        Vector vector = Vector.of(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        Individual individual = new Individual();
        individual.getProperties().put(EntityType.CANDIDATE_SOLUTION, Vector.copyOf(vector));

        MaskedInitialisationStrategy<Individual> initialisationStrategy = new MaskedInitialisationStrategy<Individual>(
            Vector.of(Double.NEGATIVE_INFINITY, Double.NaN, Double.NEGATIVE_INFINITY, Double.NaN));
        initialisationStrategy.initialise(EntityType.CANDIDATE_SOLUTION, individual);

        Vector chromosome = (Vector) individual.getCandidateSolution();

        assertTrue(chromosome.doubleValueOf(0) == Double.NEGATIVE_INFINITY);
        assertFalse(Double.isNaN(chromosome.doubleValueOf(1)));
        assertTrue(chromosome.doubleValueOf(2) == Double.NEGATIVE_INFINITY);
        assertFalse(Double.isNaN(chromosome.doubleValueOf(3)));
    }
}
