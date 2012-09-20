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
package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.type.types.container.Vector;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class Order5DeceptiveTest {
    
    private Order5Deceptive func = new Order5Deceptive();
    
    @Test
    public void testFunction() {      
        assertEquals(4.0, func.apply(Vector.of(0,0,0,0,0)), Maths.EPSILON);        
        assertEquals(3.0, func.apply(Vector.of(0,0,0,0,1)), Maths.EPSILON);        
        assertEquals(2.0, func.apply(Vector.of(0,0,0,1,1)), Maths.EPSILON);        
        assertEquals(1.0, func.apply(Vector.of(0,0,1,1,1)), Maths.EPSILON);        
        assertEquals(3.5, func.apply(Vector.of(1,1,1,1,1)), Maths.EPSILON);        
        
        assertEquals(0.0, func.apply(Vector.of(1,0,0,0,1)), Maths.EPSILON);        
        assertEquals(0.0, func.apply(Vector.of(1,0,1,0,1)), Maths.EPSILON);        
        assertEquals(0.0, func.apply(Vector.of(1,1,0,0,1)), Maths.EPSILON);        
        assertEquals(0.0, func.apply(Vector.of(1,0,0,1,1)), Maths.EPSILON);        
    }
    
}
