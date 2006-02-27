/*
 * MultistartOptimisationAlgorithm.java
 *
 * Created on January 26, 2003, 3:06 PM
 *
 *
 * Copyright (C) 2003 - Edwin S. Peer
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

import net.sourceforge.cilib.Indicator.*;
import net.sourceforge.cilib.Problem.*;

/**
 *
 * @author  espeer
 */
public class MultistartOptimisationAlgorithm extends Algorithm implements OptimisationAlgorithm, ParticipatingAlgorithm {
    
    /** Creates a new instance of MultistartOptimisationAlgorithm */
    public MultistartOptimisationAlgorithm() {
        singleIteration = new SingleIteration();
        problem = null;
    }
    
    public void setOptimisationAlgorithm(OptimisationAlgorithm algorithm) {
        this.algorithm = (Algorithm) algorithm;
        this.algorithm.addProgressIndicator(singleIteration);
        optimisationAlgorithm = algorithm;
    }
    
    public int getFitnessEvaluations() {
        return fitnessEvaluations;
    }
    
    public OptimisationProblem getOptimisationProblem() {
        return optimisationAlgorithm.getOptimisationProblem();
    }
    
    public double[] getSolution() {
        return solution;
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
    
    public void addRestartProgressIndicator(ProgressIndicator indicator) {
        algorithm.addProgressIndicator(indicator);
    }
    
    public void removeRestartProgressIndicator(ProgressIndicator indicator) {
        algorithm.removeProgressIndicator(indicator);
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
       
        if (optimisationAlgorithm.getSolutionFitness() > fitness) {
            fitness = optimisationAlgorithm.getSolutionFitness();
            for (int i = 0; i < getOptimisationProblem().getDimension(); ++i) {
                solution[i] = optimisationAlgorithm.getSolution()[i];
            }
        }
       
        if (algorithm.getPercentageComplete() >= 100) {
            previousFitnessEvaluations = fitnessEvaluations;
            algorithm.initialise();
            ++restarts;
        } 
    }
    
    public int getRestarts() {
        return restarts;
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
