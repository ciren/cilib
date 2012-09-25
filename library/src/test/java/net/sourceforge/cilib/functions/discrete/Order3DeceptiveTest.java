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
package net.sourceforge.cilib.functions.discrete;

import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.type.types.container.Vector;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class Order3DeceptiveTest {

    private Order3Deceptive func = new Order3Deceptive();

    @Test
    public void testFunction() {
        assertEquals(0.9, func.apply(Vector.of(0,0,0)), Maths.EPSILON);
        assertEquals(0.6, func.apply(Vector.of(1,0,0)), Maths.EPSILON);
        assertEquals(0.3, func.apply(Vector.of(1,1,0)), Maths.EPSILON);
        assertEquals(1.0, func.apply(Vector.of(1,1,1)), Maths.EPSILON);

        assertEquals(0.9, func.apply(Vector.of(0,0,0)), Maths.EPSILON);
        assertEquals(0.6, func.apply(Vector.of(0,1,0)), Maths.EPSILON);
        assertEquals(0.3, func.apply(Vector.of(0,1,1)), Maths.EPSILON);

        assertEquals(1.8, func.apply(Vector.of(0,0,0,0,0,0)), Maths.EPSILON);
        assertEquals(1.2, func.apply(Vector.of(1,0,0,1,0,0)), Maths.EPSILON);
        assertEquals(0.6, func.apply(Vector.of(1,1,0,1,1,0)), Maths.EPSILON);
        assertEquals(2.0, func.apply(Vector.of(1,1,1,1,1,1)), Maths.EPSILON);
    }

}
