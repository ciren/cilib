/*
 * MathUtilTest.java
 * 
 * Created on Aug 5, 2005
 *
 * Copyright (C) 2003, 2004 - CIRG@UP 
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
 *
 */
package net.sourceforge.cilib.math;

import net.sourceforge.cilib.math.MathUtil;
import junit.framework.TestCase;

/**
 * 
 * @author Gary Pampara
 *
 */
public class MathUtilTest extends TestCase {
	
	public void testFactorial() {
		assertEquals(1.0, MathUtil.factorial(0.0));
		assertEquals(1.0, MathUtil.factorial(1.0));
		
		try {
			MathUtil.factorial(-1.0);
			fail("Factorial accepted a negative input");
		}
		catch (Exception e) {}
		
		assertEquals(6.0, MathUtil.factorial(3));
		assertEquals(720.0, MathUtil.factorial(6));
		assertEquals(9.33262154439441E157, MathUtil.factorial(100)); 
	}
	
	
	public void testCombination() {
		assertEquals(792.0, MathUtil.combination(12, 5));
		
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
		
		assertEquals(1.0, MathUtil.combination(0, 0));
		assertEquals(1.0, MathUtil.combination(1, 0));
		assertEquals(1.0, MathUtil.combination(1, 1));
	}

}
