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

import org.junit.Test;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 * @author Edwin Peer
 */
public class RosenbrockTest {

    public RosenbrockTest() {

    }

    /** Test of evaluate method, of class za.ac.up.cs.ailib.Functions.Rosenbrock. */
    @Test
    public void testEvaluate() {
        ContinuousFunction function = new Rosenbrock();
        function.setDomain("R(-2.048, 2.048)^3");

        Vector x = new Vector();
        x.append(new Real(1.0));
        x.append(new Real(2.0));
        x.append(new Real(3.0));

        Vector y = new Vector();
        y.append(new Real(3.0));
        y.append(new Real(2.0));
        y.append(new Real(1.0));

        assertEquals(201.0, function.evaluate(x), 0.0);
        assertEquals(5805.0, function.evaluate(y), 0.0);

        function = new Rosenbrock();
        function.setDomain("R(-2.048, 2.048)^4");
        Vector z = new Vector();
        z.append(new Real(1.0));
        z.append(new Real(2.0));
        z.append(new Real(3.0));
        z.append(new Real(4.0));
        assertEquals(2705.0, function.evaluate(z), 0.0);
    }


}
