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

package net.sourceforge.cilib.problem;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;


/**
 *
 * @author Edwin Peer
 */
public class MaxFitnessTest {

	public MaxFitnessTest() {
        
    }
    
    @BeforeClass
    public static void setUp() {
    	oneFitness = new MaximisationFitness(new Integer(1).doubleValue());
    	twoFitness = new MaximisationFitness(new Integer(2).doubleValue());
    	inferiorFitness = InferiorFitness.instance();
    }
        
	@Test
    public void testLessThan() {
    	assertEquals(oneFitness.compareTo(twoFitness), -1);
    }
    
	@Test
    public void testMoreThan() {
    	assertEquals(twoFitness.compareTo(oneFitness), 1);
    }
	
	@Test
    public void testInferior() {		
    	assertEquals(inferiorFitness.compareTo(oneFitness), -1);
    	assertEquals(oneFitness.compareTo(inferiorFitness), 1);
    }

    private static Fitness oneFitness;
    private static Fitness twoFitness;
    private static Fitness inferiorFitness;
    
}
