/*
 * MinFitnessTest.java
 * JUnit based test
 *
 * Created on January 21, 2003, 4:45 PM
 *
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

package net.sourceforge.cilib.Problem;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author espeer
 */
public class MinFitnessTest extends TestCase {

	public MinFitnessTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(MinFitnessTest.class);
        
        return suite;
    }
    
    public void setUp() {
    	oneFitness = new MinimisationFitness(new Integer(1));
    	twoFitness = new MinimisationFitness(new Integer(2));
    	inferiorFitness = InferiorFitness.instance();
    }
        
    public void testLessThan() {
    	assertEquals(twoFitness.compareTo(oneFitness), -1);
    }
    
    public void testMoreThan() {
    	assertEquals(oneFitness.compareTo(twoFitness), 1);
    }
		
    public void testInferior() {		
    	assertEquals(inferiorFitness.compareTo(oneFitness), -1);
    	assertEquals(oneFitness.compareTo(inferiorFitness), 1);
    }

    private Fitness oneFitness;
    private Fitness twoFitness;
    private Fitness inferiorFitness;
    
}
