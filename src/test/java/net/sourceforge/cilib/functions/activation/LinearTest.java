/*
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
package net.sourceforge.cilib.functions.activation;

import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for the linear activation function;
 * @author andrich
 */
public class LinearTest {

    @Test
    public void evaluate() {
        Linear linear = new Linear();
        Assert.assertEquals(0.0, linear.apply(0.0), Maths.EPSILON);
        Assert.assertEquals(0.0, linear.apply(Real.valueOf(0.0)).doubleValue(), Maths.EPSILON);

        Assert.assertEquals(0.5, linear.apply(0.5), Maths.EPSILON);
        Assert.assertEquals(0.5, linear.apply(Real.valueOf(0.5)).doubleValue(), Maths.EPSILON);

        Assert.assertEquals(Double.MAX_VALUE, linear.apply(Double.MAX_VALUE), Maths.EPSILON);
        Assert.assertEquals(Double.MIN_VALUE, linear.apply(Real.valueOf(Double.MIN_VALUE)).doubleValue(), Maths.EPSILON);
        Assert.assertEquals(-Double.MIN_VALUE, linear.apply(Real.valueOf(-Double.MIN_VALUE)).doubleValue(), Maths.EPSILON);
        Assert.assertEquals(-Double.MAX_VALUE, linear.apply(Real.valueOf(-Double.MAX_VALUE)).doubleValue(), Maths.EPSILON);
    }

    @Test
    public void gradient() {
        Linear linear = new Linear();
        Assert.assertEquals(1.0, linear.getGradient(0.0), Maths.EPSILON);
        Assert.assertEquals(1.0, linear.getGradient(Vector.of(0.0)).get(0).doubleValue(), Maths.EPSILON);

        Assert.assertEquals(1.0, linear.getGradient(0.5), Maths.EPSILON);
        Assert.assertEquals(1.0, linear.getGradient(Vector.of(0.5)).get(0).doubleValue(), Maths.EPSILON);

        Assert.assertEquals(1.0, linear.getGradient(Double.MAX_VALUE), Maths.EPSILON);
        Assert.assertEquals(1.0, linear.getGradient(Double.MIN_VALUE), Maths.EPSILON);
        Assert.assertEquals(1.0, linear.getGradient(-Double.MIN_VALUE), Maths.EPSILON);
        Assert.assertEquals(1.0, linear.getGradient(-Double.MAX_VALUE), Maths.EPSILON);
    }

    @Test
    public void activeRange() {
        Linear linear = new Linear();
        Assert.assertEquals(Double.MAX_VALUE, linear.getUpperActiveRange(), Maths.EPSILON);
        Assert.assertEquals(-Double.MAX_VALUE, linear.getLowerActiveRange(), Maths.EPSILON);
    }
}
