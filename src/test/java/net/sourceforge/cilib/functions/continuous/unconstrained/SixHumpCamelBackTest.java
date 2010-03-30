/**
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
public class SixHumpCamelBackTest {
    private static final double EPSILON = 1.0E-4;
    private ContinuousFunction function;

    @Before
    public void instantiate()
    {
        this.function = new SixHumpCamelBack();
    }

    /** Test of the evaluate method of the {@link SixHumpCamelBack} class */
    @Test
    public void testEvaluate() {
        function.setDomain("R(-3,3),R(-2,2)");

        Vector x = new Vector();

        //test the two global minima
        x.add(new Real(-0.0898));
        x.add(new Real(0.7126));
        Assert.assertEquals(-1.0316, function.evaluate(x), EPSILON);

        x.setReal(0, 0.0898);
        x.setReal(1, -0.7126);
        Assert.assertEquals(-1.0316, function.evaluate(x), EPSILON);
    }

    /** Test of the getMinimum method of the {@link SixHumpCamelBack} class */
    @Test
    public void minimum() {
        Assert.assertEquals(-1.0316, function.getMinimum(), EPSILON);
    }
}
