/*
 * KnuthSubtractiveTest.java
 * JUnit based test
 *
 * Created on January 21, 2003, 7:23 PM
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

package net.sourceforge.cilib.math.random;

import java.util.Random;

import net.sourceforge.cilib.math.random.KnuthSubtractive;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author Edwin Peer
 */
public class KnuthSubtractiveTest extends TestCase {
    
    public KnuthSubtractiveTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(KnuthSubtractiveTest.class);
        
        return suite;
    }
    
    /** Test of next method, of class za.ac.up.cs.ailib.Random.NumericalRecipesRan3. */
    public void testNextDouble() {
        RandomTester tester = new SimpleRandomTester();
        Random r = new KnuthSubtractive();
        for (int i = 0; i < 100000; ++i) {
            double d = r.nextDouble();
            assertTrue("Random value out of range", 0 <= d && d < 1); 
            tester.addSample(d);
        }
        assertTrue("Samples are not random", tester.hasRandomSamples());
    }
    
}
