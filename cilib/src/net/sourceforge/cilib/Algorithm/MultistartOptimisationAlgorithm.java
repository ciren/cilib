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

import net.sourceforge.cilib.Problem.OptimisationProblem;
import net.sourceforge.cilib.Problem.OptimisationSolution;
import net.sourceforge.cilib.StoppingCondition.SingleIteration;
import net.sourceforge.cilib.StoppingCondition.StoppingCondition;

/**
 * <code>MultistartOptimisationAlgorithm</code> is simply a wrapper. The wrapped 
 * optimisation algorithm is subject to restart conditions. Each time one of these
 * conditions is satisfied, the wrapped algorithm is re-initialised without resetting 
 * any of the progress indicators used to determine stopping conditions.
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
     * Sets the optimisation algorithm that is subject to restart conditions.
     *
     * @param algorithm Any {@OptimisationAlgorithm}
     */
    public void setOptimisationAlgorithm(OptimisationAlgorithm algorithm) {
        this.algorithm = (Algorithm) algorithm;
        this.algorithm.addStoppingCondition(singleIteration);
        optimisationAlgorithm = algorithm;
    }
    
    public int getFitnessEvaluations() {
        return fitnessEvaluations;
    }
    
    public OptimisationProblem getOptimisationProblem() {
    	if (optimisationAlgorithm != null) {
    		return optimisationAlgorithm.getOptimisationProblem();
    	}
    	else {
    		return null;
    	}
    }
    
    public double getSolutionFitness() {
        return fitness;
    }
    
    public void setOptimisationProblem(OptimisationProblem problem) {
        this.problem = problem;
    }
    
    public double[] getContribution() {
        return ((ParticipatingAlgorithm) algorithm).getContribution();
    }
    
    public double getContributionFitness() {
        return ((ParticipatingAlgorithm) algorithm).getContributionFitness();
    }
    
    public void updateContributionFitness(double fitness) {
        ((ParticipatingAlgorithm) algorithm).updateContributionFitness(fitness);
    }
    
    /** 
     * Add a progress indicator used to determine when the algorithm
     * should be restarted. Equivalent to calling {@link Algorithm#addProgressIndicator(ProgressIndicator)} on
     * the algorithm set in {@link #setOptimisationAlgorithm(OptimisationAlgorithm)}.
     *
     * @param indicator The {@link ProgressIndicator} to be added.
     */
    public void addRestartCondition(StoppingCondition condition) {
        algorithm.addStoppingCondition(condition);
    }
    
    /** 
     * Removes a progress indicator used to determine a restarting condition.
     * Equivalent to calling {@link Algorithm#removeProgressIndicator(ProgressIndicator)} on
     * the algorithm set in {@link #setOptimisationAlgorithm(OptimisationAlgorithm)}.
     *
     * @param indicator The {@link ProgressIndicator} to be removed.
     */
    public void removeRestartCondition(StoppingCondition condition) {
        algorithm.removeStoppingCondition(condition);
    }
    
    public void initialise() {
        super.initialise();
        if (problem != null) {
            optimisationAlgorithm.setOptimisationProblem(problem);
        }
        previousFitnessEvaluations = 0;
        fitnessEvaluations = 0;
        fitness = - Double.MAX_VALUE;
        solution = new double[getOptimisationProblem().getDimension()];
        restarts = 0;
        algorithm.initialise();
    }
    
    public void performIteration() { 
        algorithm.run();
        singleIteration.reset();
        fitnessEvaluations = previousFitnessEvaluations + ((OptimisationAlgorithm) algorithm).getFitnessEvaluations();
       
        if (optimisationAlgorithm.getBestSolution().getFitness() > fitness) {
            fitness = optimisationAlgorithm.getBestSolution().getFitness();
            for (int i = 0; i < getOptimisationProblem().getDimension(); ++i) {
                solution[i] = optimisationAlgorithm.getBestSolution().getPosition()[i];
            }
        }
               
        if (algorithm.isFinished()) {
            previousFitnessEvaluations = fitnessEvaluations;
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
        return new OptimisationSolution(getOptimisationProblem(), solution);
    }
    
    public java.util.Collection getSolutions() {
        ArrayList solutions = new ArrayList(1);
        solutions.add(getBestSolution());
        return solutions;
    }
    
    private Algorithm algorithm;
    private OptimisationAlgorithm optimisationAlgorithm;
    private int fitnessEvaluations;
    private int previousFitnessEvaluations;
    private int restarts;
    private SingleIteration singleIteration;
    private OptimisationProblem problem;
    private double[] solution;
    private double fitness;
}
