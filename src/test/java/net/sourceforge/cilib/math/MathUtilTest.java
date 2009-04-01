/**
 * Copyright (C) 2003 - 2008
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

package net.sourceforge.cilib.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * 
 * @author Gary Pampara
 */
public class MathUtilTest {
    
    @Test(expected = IllegalArgumentException.class)
    public void invalidFactorialParameter() {
        MathUtil.factorial(-1.0);
    }
    
    @Test
    public void testFactorial() {
        assertEquals(1.0, MathUtil.factorial(0.0), Double.MIN_NORMAL);
        assertEquals(1.0, MathUtil.factorial(1.0), Double.MIN_NORMAL);
        assertEquals(6.0, MathUtil.factorial(3), Double.MIN_NORMAL);
        assertEquals(720.0, MathUtil.factorial(6), Double.MIN_NORMAL);
        assertEquals(9.33262154439441E157, MathUtil.factorial(100), Double.MIN_NORMAL); 
    }
    
    @Test
    public void testCombination() {
        assertEquals(792.0, MathUtil.combination(12, 5), Double.MIN_NORMAL);
        
        try {
            MathUtil.combination(-1, -5);
            fail("Invalid input!");
        }
        catch (Exception e) {}
        
        try {
            MathUtil.combination(-1, 5);
            fail("Invalid input!");
        }
        catch (Exception e) {}
        
        try {
            MathUtil.combination(1, -5);
            fail("Invalid input!");
        }
        catch (Exception e) {}
        
        assertEquals(1.0, MathUtil.combination(0, 0), Double.MIN_NORMAL);
        assertEquals(1.0, MathUtil.combination(1, 0), Double.MIN_NORMAL);
        assertEquals(1.0, MathUtil.combination(1, 1), Double.MIN_NORMAL);
    }
    
}
