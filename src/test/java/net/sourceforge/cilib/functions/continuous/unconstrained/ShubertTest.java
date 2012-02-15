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
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author engel
 */
public class ShubertTest {
 
    private ContinuousFunction function;

    @Before
    public void instantiate() {
        this.function = new Shubert();
    }

    /** Test for Shubert Function*/
    @Test
    public void testEvaluate() {
        Vector x = Vector.of(0,0);
        
        assertEquals(19.875836249, function.apply(x), 0.000000009);

        x.setReal(0, -8.0);
        x.setReal(1, -8.0);
        assertEquals(7.507985827, function.apply(x), 0.000000009);
        
        x.setReal(0, -7.2);
        x.setReal(1, -7.7);
        assertEquals(-157.071676802, function.apply(x), 0.000000009);
    }   
}
