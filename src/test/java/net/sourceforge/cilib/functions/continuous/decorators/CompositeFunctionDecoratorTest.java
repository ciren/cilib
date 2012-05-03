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
package net.sourceforge.cilib.functions.continuous.decorators;

import net.sourceforge.cilib.functions.continuous.unconstrained.Spherical;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.type.types.container.Vector;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CompositeFunctionDecoratorTest {

    @Test
    public void testEvaluate() {
        CompositeFunctionDecorator comp = new CompositeFunctionDecorator();
        Spherical function = new Spherical();
        
        comp.setInnerFunction(function);
        comp.setOuterFunction(function);

        assertEquals(0.0, comp.apply(Vector.of(0.0, 0.0)), Maths.EPSILON);
        assertEquals(4.0, comp.apply(Vector.of(1.0, 1.0)), Maths.EPSILON);
        assertEquals(196.0, comp.apply(Vector.of(1.0, 2.0, 3.0)), Maths.EPSILON);
    }
}
