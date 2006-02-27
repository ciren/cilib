/*
 * MOFitnessTest.java
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

package net.sourceforge.cilib.problem;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.sourceforge.cilib.problem.dataset.DataSetBuilder;
import net.sourceforge.cilib.type.DomainRegistry;

/**
 *
 * @author jkroon
 */
public class MOFitnessTest extends TestCase {

	public MOFitnessTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(MOFitnessTest.class);
        
        return suite;
    }
    
    public void setUp() {
    	moproblem = new MOOptimisationProblem();
    	for(int i = 0; i < 3; i++) {
    		moproblem.addOptimisationProblem(new DummyOptimisationProblem(i));
    	}
    }
        
    public void testAllInferior() {
    	Fitness inferior[] = new Fitness[]{ InferiorFitness.instance(), InferiorFitness.instance(), InferiorFitness.instance() };
    	Fitness oneFitness[] = new Fitness[]{
    			new MinimisationFitness(new Integer(1).doubleValue()),
    			new MinimisationFitness(new Integer(1).doubleValue()),
    			new MinimisationFitness(new Integer(1).doubleValue()) };
				
    	Fitness f1 = moproblem.getFitness(inferior, false);
    	Fitness f2 = moproblem.getFitness(oneFitness, false);
    	
    	assertTrue(f1.compareTo(f2) < 0);
    	assertTrue(f2.compareTo(f1) > 0);
    	
    }

    private MOOptimisationProblem moproblem;
    
    private class DummyOptimisationProblem implements OptimisationProblem {
    	private int i;
    	
    	DummyOptimisationProblem(int i) {
    		this.i = i;
    	}
    
		public Fitness getFitness(Object solution, boolean count) {
			return ((Fitness[])solution)[i];
		}

		public int getFitnessEvaluations() {
			return 0;
		}

		public DomainRegistry getDomain() {
			// TODO Auto-generated method stub
			return null;
		}

		public DomainRegistry getBehaviouralDomain() {
			// TODO Auto-generated method stub
			return null;
		}

		public DataSetBuilder getDataSetBuilder() {
			// TODO Auto-generated method stub
			return null;
		}

		public void setDataSetBuilder(DataSetBuilder dataSet) {
			// TODO Auto-generated method stub
		}
    	
    }
}
