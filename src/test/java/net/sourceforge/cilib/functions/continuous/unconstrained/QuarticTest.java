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
 *
 * @author Bennie Leonard
 */
public class QuarticTest {
    private static final double EPSILON = 1.0E-6;
    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new Quartic();
    }

    /** Test of the evaluation method of the {@link Quartic} class */
    @Test
    public void evaluationTest() {
        function.setDomain("R(-1.28, 1.28)^3");

        Vector x = new Vector();

        //test the global minimum
        x.add(new Real(0.0));
        x.add(new Real(0.0));
        x.add(new Real(0.0));
        Assert.assertEquals(0.0, function.apply(x), EPSILON);

        //test another point
        x.setReal(0, 2.0);
        x.setReal(1, 2.0);
        x.setReal(2, 2.0);
        Assert.assertEquals(96.0, function.apply(x), EPSILON);
    }

    @Test
    public void minimum() {
        Assert.assertEquals(0.0, function.getMinimum(), EPSILON);
    }
}
