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

import static org.junit.Assert.assertEquals;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author leo
 *
 */

public class DeJongF4Test {
    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new DeJongF4();
    }

    /** Test of evaluate method, of class cilib.functions.unconstrained.DeJongF4. */
    @Test
    public void testEvaluate() {
        function.setDomain("R(-1.28, 1.28)^2");

        Vector x = new Vector();
        x.append(new Real(0));
        x.append(new Real(0));

        assertEquals(0.0, function.evaluate(x), 0.0);

        x.setReal(0, 1.0);
        x.setReal(1, 2.0);

        assertEquals(33, function.evaluate(x), 0.0000000001);
    }

    @Test
    public void minimum() {
        assertEquals(0.0, function.getMinimum().doubleValue(), 0.0);
    }
}
