/*
 * AllTest.java
 *
 * Created on January 21, 2003, 5:12 PM
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

package net.sourceforge.cilib.type.types;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 *
 * @author  Edwin Peer
 */
@RunWith(Suite.class)
@SuiteClasses(
		value = {
			NumericTest.class,
			BitTest.class,
			IntTest.class,
			MixedVectorTest.class,
			RealTest.class,
			StringTypeTest.class
		}
	)
public class AllTest {
    
    /** Creates a new instance of AllTests */
    public AllTest() {

    }
    
    /*public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        
        suite.addTestSuite(NumericTest.class);
        suite.addTestSuite(BitTest.class);
        suite.addTestSuite(IntTest.class);
        suite.addTestSuite(MixedVectorTest.class);
        suite.addTestSuite(RealTest.class);
        suite.addTestSuite(StringTypeTest.class);
        
        return suite;
    }
    */
}