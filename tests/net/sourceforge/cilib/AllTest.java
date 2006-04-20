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

package net.sourceforge.cilib;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 *
 * @author  Edwin Peer
 */
@RunWith(Suite.class)
@SuiteClasses(
		value={
				net.sourceforge.cilib.container.AllTest.class,
				net.sourceforge.cilib.container.graph.AllTest.class,
				net.sourceforge.cilib.functions.continuous.AllTest.class,
				net.sourceforge.cilib.functions.discrete.AllTest.class,
				net.sourceforge.cilib.math.AllTest.class,
				net.sourceforge.cilib.math.random.AllTest.class,
				net.sourceforge.cilib.measurement.AllTest.class,
				net.sourceforge.cilib.problem.AllTest.class,
				net.sourceforge.cilib.pso.AllTest.class,
				net.sourceforge.cilib.type.AllTest.class,
				net.sourceforge.cilib.type.creator.AllTest.class,
				net.sourceforge.cilib.type.types.AllTest.class,
				net.sourceforge.cilib.util.AllTest.class
			}
		)
public class AllTest {
    
    public AllTest() {
    }
    
    public static void main(java.lang.String[] args) {
    	JUnitCore.main(AllTest.class.getName());
    	//System.out.println("Running JUnit 3.8.1 Tests");
        //junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        
        //suite.addTest(net.sourceforge.cilib.container.AllTest.suite());
        //suite.addTest(net.sourceforge.cilib.container.graph.AllTest.suite());
        //suite.addTest(net.sourceforge.cilib.functions.continuous.AllTest.suite());
        //suite.addTest(net.sourceforge.cilib.functions.discrete.AllTest.suite());
        //suite.addTest(net.sourceforge.cilib.pso.AllTest.suite());
        //suite.addTest(net.sourceforge.cilib.math.AllTest.suite());
        //suite.addTest(net.sourceforge.cilib.math.random.AllTest.suite());
        //suite.addTest(net.sourceforge.cilib.measurement.AllTest.suite());
        //suite.addTest(net.sourceforge.cilib.problem.AllTest.suite());
        //suite.addTest(net.sourceforge.cilib.type.AllTest.suite());
        //suite.addTest(net.sourceforge.cilib.type.creator.AllTest.suite());
        //suite.addTest(net.sourceforge.cilib.type.types.AllTest.suite());
        //suite.addTest(net.sourceforge.cilib.util.AllTest.suite());
        
        return suite;
    }
}
