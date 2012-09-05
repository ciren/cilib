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
