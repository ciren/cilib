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

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.functions.continuous.unconstrained.Spherical;
import net.sourceforge.cilib.type.types.container.Vector;
import org.junit.Test;
import static org.junit.Assert.*;

public class ShiftedFunctionDecoratorTest {

    @Test
    public void testApply() {
        Spherical s = new Spherical();
        ShiftedFunctionDecorator d = new ShiftedFunctionDecorator();
        d.setHorizontalShift(ConstantControlParameter.of(0));
        d.setVerticalShift(ConstantControlParameter.of(1));
        d.setFunction(s);
        
        assertEquals(d.apply(Vector.of(0.0, 0.0)), 1.0, 0.0);
        
        d.setHorizontalShift(ConstantControlParameter.of(5));
        
        assertEquals(d.apply(Vector.of(5.0, 5.0)), 1.0, 0.0);
        
        d.setVerticalShift(ConstantControlParameter.of(-1));
        
        assertEquals(d.apply(Vector.of(5.0, 5.0)), -1.0, 0.0);
    }
}
