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
 */
public class SphericalTest {

    private Spherical function = new Spherical();

    @Before
    public void instantiate() {
        this.function = new Spherical();
    }

    /** Test of evaluate method, of class za.ac.up.cs.ailib.Functions.Spherical. */
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(1.0, 2.0, 3.0);

        assertEquals(14.0, function.apply(x), 0.0);
    }

    @Test
    public void testGradient() {
        Vector x = Vector.of(1.0, 2.0, 3.0);

        assertEquals(Vector.of(2.0, 4.0, 6.0), function.getGradient(x));
    }
}
