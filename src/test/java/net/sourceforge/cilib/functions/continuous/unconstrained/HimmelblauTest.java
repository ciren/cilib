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
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * Test the validity of the implmentation of the {@link Himmelblau} function.
 * @author Bennie Leonard
 */
public class HimmelblauTest {
    private static final double EPSILON = 1.0E-6;
    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new Himmelblau();
    }

    /**
     * Test of evaluate method, on the given problem domain.
     */
    @Test
    public void testEvaluate() {
        function.setDomain("R(-6, 6)^2");

        Vector x = new Vector();

        //test the four global minima
        x.append(new Real(3.0));
        x.append(new Real(2.0));
        Assert.assertEquals(0.0, function.evaluate(x), EPSILON);

        x.setReal(0, -2.805118);
        x.setReal(1, 3.131312);
        Assert.assertEquals(0.0, function.evaluate(x), EPSILON);

        x.setReal(0, -3.779301);
        x.setReal(1, -3.283185);
        Assert.assertEquals(0.0, function.evaluate(x), EPSILON);

        x.setReal(0, 3.584428);
        x.setReal(1, -1.848126);
        Assert.assertEquals(0.0, function.evaluate(x), EPSILON);

        //test one other point
        x.setReal(0, 2.0);
        x.setReal(1, 2.0);
        Assert.assertEquals(26.0, function.evaluate(x), EPSILON);
    }

    /**
     * These the minimum of the function. It should be noted that
     * this test is based on the assumption that the domain of the
     * function is {@code R(-6, 6)^2}, therefore the minimum value
     * expected is {@code 0.0}.
     */
    @Test
    public void minimum() {
        Assert.assertEquals(0.0, function.getMinimum(), EPSILON);
    }
}
