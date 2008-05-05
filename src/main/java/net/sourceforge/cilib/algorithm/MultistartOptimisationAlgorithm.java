/*
 * MultistartOptimisationAlgorithm.java
 *
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
package net.sourceforge.cilib.algorithm;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.cooperative.ParticipatingAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.visitor.TopologyVisitor;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationProblemAdapter;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.stoppingcondition.SingleIteration;
import net.sourceforge.cilib.stoppingcondition.StoppingCondition;
import net.sourceforge.cilib.type.DomainRegistry;

/**
 * <code>MultistartOptimisationAlgorithm</code> is simply a wrapper. The wrapped 
 * optimisation algorithm is subject to restart conditions. Each time one of these
 * conditions is satisfied, the wrapped algorithm is re-initialised and execution continues until 
 * this algorithm's stopping conditions are satisfied.
 * 
 * <p>
 * This class implements a generalised multistart optimisation algorithm. The 
 * original Multistart PSO is due to F. van den Bergh, reference:
 *          F. van den Bergh, "An Analysis of Particle Swarm Optimizers,"
 *          PhD thesis, Department of Computer Science, 
 *          University of Pretoria, South Africa, 2002.
 *          
 * @author  Edwin Peer
 */
public class MultistartOptimisationAlgorithm extends Algorithm implements ParticipatingAlgorithm {
    private static final long serialVersionUID = 1493525363256406120L;

	/** Creates a new instance of MultistartOptimisationAlgorithm. */
    public MultistartOptimisationAlgorithm() {
        singleIteration = new SingleIteration();
        problem = null;
    }
    
    public MultistartOptimisationAlgorithm(MultistartOptimisationAlgorithm copy) {
    	
    }
    
    public MultistartOptimisationAlgorithm getClone() {
    	return new MultistartOptimisationAlgorithm(this);
    }
    
    /**
     * Sets the target optimisation algorithm that is subject to restarting.
     *
     * @param algorithm Any {@link OptimisationAlgorithm} that extends {@link Algorithm}.
     */
    public void setTargetAlgorithm(Algorithm algorithm) {
        optimisationAlgorithm = algorithm;
        this.algorithm = (Algorithm) algorithm;
        this.algorithm.addStoppingCondition(singleIteration);
    }
    
    public OptimisationProblem getOptimisationProblem() {
    	return problem.getTarget();
    }
    
    public Fitness getSolutionFitness() {
        return fitness;
    }
    
    public void setOptimisationProblem(OptimisationProblem problem) {
        this.problem = new MultistartProblemAdapter(problem);
    }
    
    public Entity getContribution() {
        return ((ParticipatingAlgorithm) algorithm).getContribution();
    }
    
    public Fitness getContributionFitness() {
        return ((ParticipatingAlgorithm) algorithm).getContributionFitness();
    }
    
    public void updateContributionFitness(Fitness fitness) {
        ((ParticipatingAlgorithm) algorithm).updateContributionFitness(fitness);
    }
    
    /** 
     * Add a stopping condition used to determine when the algorithm
     * should be restarted. Equivalent to calling {@link Algorithm#addStoppingCondition(StoppingCondition)} on
     * the algorithm set in {@link #setTargetAlgorithm(OptimisationAlgorithm)}.
     *
     * @param indicator The {@link StoppingCondition} to be added.
     */
    public void addRestartCondition(StoppingCondition condition) {
        algorithm.addStoppingCondition(condition);
    }
    
    /** 
     * Removes a restart condition.
     * Equivalent to calling {@link Algorithm#removeStoppingCondition(StoppingCondition)} on
     * the algorithm set in {@link #setTargetAlgorithm(OptimisationAlgorithm)}.
     *
     * @param indicator The {@link StoppingCondition} to be removed.
     */
    public void removeRestartCondition(StoppingCondition condition) {
        algorithm.removeStoppingCondition(condition);
    }
    
    public void performInitialisation() {
        if (problem != null) {
            optimisationAlgorithm.setOptimisationProblem(problem);
        }
        fitness = InferiorFitness.instance();
        restarts = 0;
        algorithm.initialise();
        solution = optimisationAlgorithm.getBestSolution();
    }
    
    public void algorithmIteration() { 
        algorithm.run();
        singleIteration.reset();
       
        OptimisationSolution tmp = optimisationAlgorithm.getBestSolution();
        if (tmp.getFitness().compareTo(fitness) > 0) {
            fitness = tmp.getFitness();
            solution = tmp;
        }
               
        if (algorithm.isFinished()) {
        	problem.resetFitnessCounter();
            algorithm.initialise();
            ++restarts;
        } 
    }
    
    /**
     * Returns the number of times that the algorithm has been restarted.
     *
     * @return The number of restarts.
     */
    public int getRestarts() {
        return restarts;
    }
    
    public OptimisationSolution getBestSolution() {
        return solution;
    }
    
    public List<OptimisationSolution> getSolutions() {
    	// TODO: Fix this so that all the solutions found at the time of each restart are added to the collection
    
        ArrayList<OptimisationSolution> solutions = new ArrayList<OptimisationSolution>(1);
        solutions.add(getBestSolution());
        return solutions;
    }
    
    private Algorithm algorithm;
    private Algorithm optimisationAlgorithm;
    private int restarts;
    private SingleIteration singleIteration;
    private MultistartProblemAdapter problem;
    private OptimisationSolution solution;
    private Fitness fitness;
    
    private class MultistartProblemAdapter extends OptimisationProblemAdapter {
    	
		private static final long serialVersionUID = -3156973576101060294L;

		public MultistartProblemAdapter() {
    		
    	}

    	public MultistartProblemAdapter(OptimisationProblem target) {
    		this.target = target;
    	}
    	
    	public MultistartProblemAdapter(MultistartProblemAdapter copy) {
    		
    	}
    	
    	public MultistartProblemAdapter getClone() {
    		return new MultistartProblemAdapter(this);
    	}
    	
    	public OptimisationProblem getTarget() {
    		return target;
    	}
    	
		/* (non-Javadoc)
		 * @see net.sourceforge.cilib.Problem.OptimisationProblemAdapter#calculateFitness(java.lang.Object)
		 */
		protected Fitness calculateFitness(Object solution) {
			return target.getFitness(solution, true);
		}

		/* (non-Javadoc)
		 * @see net.sourceforge.cilib.Problem.OptimisationProblemAdapter#getDomain()
		 */
		/*public DomainComponent getDomain() {
			return target.getDomain();
		}*/
		/*public Domain getDomain() {
			return Domain.getInstance();
		}*/
    	
		public void resetFitnessCounter() {
			fitnessEvaluations.set(0);
		}
		
		
		public DomainRegistry getDomain() {
			// TODO Auto-generated method stub
			return null;
		}
		
		public DomainRegistry getBehaviouralDomain() {
			return null;
		}
		
		private OptimisationProblem target;
		
    }

	@Override
	public double accept(TopologyVisitor visitor) {
		throw new UnsupportedOperationException("Needs an implementation");
	}
}
