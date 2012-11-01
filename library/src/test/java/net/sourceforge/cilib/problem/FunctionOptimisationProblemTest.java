/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem;

import static org.junit.Assert.assertSame;

import java.util.Random;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.continuous.unconstrained.Ackley;
import net.sourceforge.cilib.functions.continuous.unconstrained.Spherical;

import org.junit.Before;
import org.junit.Test;

/**
 *
 */
public class FunctionOptimisationProblemTest {

    private static Random random;
    private static double[] x;
    private static ContinuousFunction function;
    private static FunctionOptimisationProblem problem;

    public FunctionOptimisationProblemTest() {

    }

    @Before
    public void setUp() {
        function = new Spherical();
        problem = new FunctionOptimisationProblem();
        problem.setDomain("R^5");
        problem.setFunction(function);
        random = new Random();
        x = new double[5];
        for (int i = 0; i < x.length; ++i) {
            x[i] = random.nextDouble();
        }
    }



    /** Test of getFunction method, of class za.ac.up.cs.ailib.Functions.FunctionOptimisationProblem. */
    @Test
    public void testGetFunction() {
        assertSame(function, problem.getFunction());
    }

    /** Test of setFunction method, of class za.ac.up.cs.ailib.Functions.FunctionOptimisationProblem. */
    @Test
    public void testSetFunction() {
        ContinuousFunction f = new Ackley();
        problem.setFunction(f);
        assertSame(f, problem.getFunction());
    }

}
