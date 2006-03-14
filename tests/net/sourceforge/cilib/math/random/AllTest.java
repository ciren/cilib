/*
 * AllTests.java
 *
 * Created on January 21, 2003, 2:55 PM
 *
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

package net.sourceforge.cilib.math.random;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
/**
 *
 * @author  Edwin Peer
 */
public class AllTest extends TestCase {
    
    /** Creates a new instance of AllTests */
    public AllTest(java.lang.String testname) {
        super(testname);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(MersenneTwisterTest.suite());
        suite.addTest(KnuthSubtractiveTest.suite());
        suite.addTest(NumericalRecipesRan4Test.suite());
        suite.addTest(RANLUXTest.suite());
        suite.addTest(TauswortheTest.suite());
        suite.addTest(ZiffGFSR4Test.suite());
        return suite;
    }
}
