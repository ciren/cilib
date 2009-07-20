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

import static org.junit.Assert.assertEquals;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;

/**
 *
 * @author Andries Engelbrecht
 */

public class BraninTest {
    private ContinuousFunction function;

    public BraninTest() {

    }

    @Before
    public void instantiate() {
        this.function = new Branin();
    }

    /** Test of evaluate method, of class za.ac.u.cs.ailib.Functions.Branin. */
    @Test
    public void testEvaluate() {
        function.setDomain("R(-5,10),R(0,15)");

        Vector x = new Vector();
        x.append(new Real(1.0));
        x.append(new Real(2.0));

        assertEquals(21.62763539206238, function.evaluate(x), 0.00000000000001);

        x.setReal(0, -Math.PI);
        x.setReal(1, 12.275);
        assertEquals(0.397887, function.evaluate(x), 0.0000009);
    }

    @Test
    public void minimum() {
        Assert.assertThat(function.getMinimum().doubleValue(), is(0.397887));
    }
}
