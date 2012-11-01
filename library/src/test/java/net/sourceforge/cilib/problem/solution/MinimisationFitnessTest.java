/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.solution;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 *
 */
public class MinimisationFitnessTest {

    @Test
    public void lessThan() {
        Fitness oneFitness = new MinimisationFitness(1.0);
        Fitness twoFitness = new MinimisationFitness(2.0);
        assertEquals(twoFitness.compareTo(oneFitness), -1);
    }

    @Test
    public void moreThan() {
        Fitness oneFitness = new MinimisationFitness(1.0);
        Fitness twoFitness = new MinimisationFitness(2.0);
        assertEquals(oneFitness.compareTo(twoFitness), 1);
    }

    @Test
    public void equality() {
        Fitness oneFitness = new MinimisationFitness(1.0);
        Fitness twoFitness = new MinimisationFitness(2.0);

        assertEquals(oneFitness.compareTo(oneFitness), 0);
        assertEquals(twoFitness.compareTo(twoFitness), 0);
    }

    @Test
    public void inferior() {
        Fitness oneFitness = new MinimisationFitness(1.0);
        Fitness inferior = InferiorFitness.instance();

        assertEquals(inferior.compareTo(oneFitness), -1);
        assertEquals(oneFitness.compareTo(inferior), 1);
    }

}
