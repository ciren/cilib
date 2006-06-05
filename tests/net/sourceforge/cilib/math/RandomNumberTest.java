/*
 * RandomNumberTest.java
 * 
 * Created on Aug 8, 2005
 *
 * Copyright (C) 2003 - 2006 
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

import org.junit.Test;
import static org.junit.Assert.*;

import net.sourceforge.cilib.math.random.RandomNumber;

/**
 * 
 * @author Gary Pampara
 *
 */
public class RandomNumberTest {

	@Test
	public void testGuassian() {
		RandomNumber rand = new RandomNumber();

		for (int i = 0; i < 1000; i++) {
			double number = rand.getGaussian();
			assertTrue(-5.0 < number);
			assertTrue(number < 5.0);
		}
	}
	
	@Test
	public void testUniform() {
		RandomNumber rand = new RandomNumber();
		
		for (int i = 0; i < 200; i++) {
			double number = rand.getUniform();
			assertTrue(number <= 1.0);
			assertTrue(0.0 <= number);
		}
	}
	
	/*public void testCauchy() {
		RandomNumber rand = new RandomNumber();
		
		for (int i = 0; i < 200; i++ ) {
			//double number = rand.getCauchy();
			//assertTrue(-5.0 <= number);
			//assertTrue(number <= 5.0);
		}
	}*/
}
