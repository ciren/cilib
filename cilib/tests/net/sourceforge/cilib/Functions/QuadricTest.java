/*
 * QuadricTest.java
 * JUnit based test
 *
 * Created on January 21, 2003, 3:51 PM
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

package net.sourceforge.cilib.Functions;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author espeer
 */
public class QuadricTest extends TestCase {
    
    public QuadricTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(QuadricTest.class);
        
        return suite;
    }
    
    /** Test of evaluate method, of class za.ac.up.cs.ailib.Functions.Quadric. */
    public void testEvaluate() {
    	ContinuousFunction function = new Quadric();
        function.setDomain("R(-100, 100)^3");
        double[] x = {1, 2, 3};
        assertEquals(46.0, function.evaluate(x), 0.0);
    }
    
    
}
