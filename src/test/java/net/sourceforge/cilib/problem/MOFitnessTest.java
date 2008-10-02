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

import static org.junit.Assert.assertTrue;
import net.sourceforge.cilib.problem.dataset.DataSetBuilder;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Type;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jkroon
 */
public class MOFitnessTest {
	
	private static MOOptimisationProblem moproblem;

	public MOFitnessTest() {
        
    }
    
    @BeforeClass
    public static void setUp() {
    	moproblem = new MOOptimisationProblem();
    	for(int i = 0; i < 3; i++) {
    		moproblem.addOptimisationProblem(new DummyOptimisationProblem(i));
    	}
    }
        
	@Test
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

    
    
    private static class DummyOptimisationProblem implements OptimisationProblem {
    	private static final long serialVersionUID = -2955989686805159033L;
    	
		private int i;
    	
    	public DummyOptimisationProblem(int i) {
    		this.i = i;
    	}
    	
    	public DummyOptimisationProblem(DummyOptimisationProblem copy) {
    		
    	}
    	
    	public DummyOptimisationProblem getClone() {
    		return new DummyOptimisationProblem(this);
    	}
    
		public Fitness getFitness(Type solution, boolean count) {
			return (Fitness) solution;
//			return getFitness(new Type[]{solution}, count);
		}
		
		public Fitness getFitness(Type [] solution, boolean count) {
			return (Fitness) solution[i];
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

		public void accept(ProblemVisitor visitor) {
			throw new UnsupportedOperationException("This method is not implemented");
		}

		public void changeEnvironment() {
			throw new UnsupportedOperationException("This method is not implemented");
		}
    	
    }
}
