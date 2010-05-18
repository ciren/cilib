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
import net.sourceforge.cilib.math.Maths;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the validity of the implmentation of the {@link GoldsteinPrice} function.
 * @author Bennie Leonard
 */
public class GoldsteinPriceTest {

    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new GoldsteinPrice();
    }

    /**
     * Test of evaluate method.
     */
    @Test
    public void testEvaluate() {
        function.setDomain("R(-2, 2)^2");

        Vector x = new Vector();

        x.append(new Real(0.0));
        x.append(new Real(-1.0));
        Assert.assertEquals(3.0, function.apply(x), Maths.EPSILON);

        x.setReal(0, 2.0);
        x.setReal(1, 2.0);
        Assert.assertEquals(76728.0, function.apply(x), Maths.EPSILON);
    }

    /**
     * These the minimum of the function. It should be noted that
     * this test is based on the assumption that the domain of the
     * function is {@code R(-2, 2)^2}, therefore the minimum value
     * expected is {@code 3.0}.
     */
    @Test
    public void minimum() {
        Assert.assertEquals(3.0, function.getMinimum(), Maths.EPSILON);
    }
}
