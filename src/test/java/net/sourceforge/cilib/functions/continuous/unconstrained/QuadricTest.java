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

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

/**
 *
 * @author Edwin Peer
 * @author Bennie Leonard
 */
public class QuadricTest {
    private static final double EPSILON = 1.0E-6;
    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new Quadric();
    }

    /** Test of the evaluate method of  the {@link Quadric} class */
    @Test
    public void testEvaluate() {
        function.setDomain("R(-100, 100)^3");

        Vector x = new Vector();

        //test global minimum
        x.add(Real.valueOf(0.0));
        x.add(Real.valueOf(0.0));
        x.add(Real.valueOf(0.0));
        Assert.assertEquals(0.0, function.evaluate(x), EPSILON);

        x.setReal(0, 1.0);
        x.setReal(1, 2.0);
        x.setReal(2, 3.0);
        Assert.assertEquals(46.0, function.evaluate(x), EPSILON);
    }

    /** Test of the getMinimum method of  the {@link Quadric} class */
    @Test
    public void minimum() {
        Assert.assertEquals(0.0, function.getMinimum(), EPSILON);
    }
}
