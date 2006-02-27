/*
 * MultistartOptimisationAlgorithm.java
 *
 * Created on January 26, 2003, 3:06 PM
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
 *
 * This class implements a generalised multistart optimisation algorithm. The 
 * original Multistart PSO is due to F. van den Bergh, reference:
 *          F. van den Bergh, "An Analysis of Particle Swarm Optimizers,"
 *          PhD thesis, Department of Computer Science, 
 *          University of Pretoria, South Africa, 2002.
 */

package net.sourceforge.cilib.Algorithm;

import java.util.ArrayList;

import net.sourceforge.cilib.Domain.DomainComponent;
import net.sourceforge.cilib.Problem.Fitness;
import net.sourceforge.cilib.Problem.InferiorFitness;
import net.sourceforge.cilib.Problem.OptimisationProblem;
import net.sourceforge.cilib.Problem.OptimisationProblemAdapter;
import net.sourceforge.cilib.Problem.OptimisationSolution;
import net.sourceforge.cilib.StoppingCondition.SingleIteration;
import net.sourceforge.cilib.StoppingCondition.StoppingCondition;

/**
 * <code>MultistartOptimisationAlgorithm</code> is simply a wrapper. The wrapped 
 * optimisation algorithm is subject to restart conditions. Each time one of these
 * conditions is satisfied, the wrapped algorithm is re-initialised and execution continues until 
 * this algorithm's stopping conditions are satisfied.
 * 
 * @author  espeer
 */
public class MultistartOptimisationAlgorithm extends Algorithm implements OptimisationAlgorithm, ParticipatingAlgorithm {
    
    /** Creates a new instance of MultistartOptimisationAlgorithm */
    public MultistartOptimisationAlgorithm() {
        singleIteration = new SingleIteration();
        problem = null;
    }
    
    /**
     * Sets the target optimisation algorithm that is subject to restarting.
     *
     * @param algorithm Any {@link OptimisationAlgorithm} that extends {@link Algorithm}.
     */
    public void setTargetAlgorithm(OptimisationAlgorithm algorithm) {
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
    
    public double[] getContribution() {
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
    
    public void performIteration() { 
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
    
    public java.util.Collection getSolutions() {
        ArrayList solutions = new ArrayList(1);
        solutions.add(getBestSolution());
        return solutions;
    }
    
    private Algorithm algorithm;
    private OptimisationAlgorithm optimisationAlgorithm;
    private int restarts;
    private SingleIteration singleIteration;
    private MultistartProblemAdapter problem;
    private OptimisationSolution solution;
    private Fitness fitness;
    
    private class MultistartProblemAdapter extends OptimisationProblemAdapter {

    	public MultistartProblemAdapter(OptimisationProblem target) {
    		this.target = target;
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
		public DomainComponent getDomain() {
			return target.getDomain();
		}
    	
		public void resetFitnessCounter() {
			fitnessEvaluations = 0;
		}
		
		private OptimisationProblem target;
		
    }
}
