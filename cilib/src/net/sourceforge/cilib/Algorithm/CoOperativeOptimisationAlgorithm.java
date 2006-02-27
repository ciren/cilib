/*
 * CoOperativeOptimisationAglorithm.java
 *
 * Created on January 24, 2003, 11:44 AM
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
 * This class implements a generalised co-operative optimisation algorithm. The 
 * original co-operative PSO is due to F. van den Bergh, reference:
 *          F. van den Bergh, "An Analysis of Particle Swarm Optimizers,"
 *          PhD thesis, Department of Computer Science, 
 *          University of Pretoria, South Africa, 2002.
 *
 */

package net.sourceforge.cilib.Algorithm;

import java.lang.*;
import net.sourceforge.cilib.*;
import net.sourceforge.cilib.Problem.*;

/**
 *
 * @author  espeer
 */
public class CoOperativeOptimisationAlgorithm extends Algorithm implements OptimisationAlgorithm, ParticipatingAlgorithm {
    
    /** Creates a new instance of CoOperativeOptimisationAglorithm */
    public CoOperativeOptimisationAlgorithm() {
        participants = 0;
        fitness = - Double.MAX_VALUE;
    }
    
    public void setAlgorithmFactory(AlgorithmFactory factory) {
        this.factory = factory;
    }
    
    public void initialise() {
        super.initialise();
        
        if (participants == 0) {
            participants = problem.getDimension();
        }
        
        optimisers = new Algorithm[participants];
        int dim = problem.getDimension() / participants;
        int extras = problem.getDimension() % participants;
        int offset = 0;
        for (int i = 0; i < participants; ++i) {
            optimisers[i] = factory.newAlgorithm();
            
            CoOperativeOptimisationProblemAdapter subProblem;
            if (extras > 0) {
                --extras;
                subProblem = 
                    new CoOperativeOptimisationProblemAdapter(problem, dim + 1, offset);
                offset += (dim + 1);
            }
            else {
                subProblem = 
                    new CoOperativeOptimisationProblemAdapter(problem, dim, offset);
                offset += dim;
            }
            
            try {
                ((OptimisationAlgorithm) optimisers[i]).setOptimisationProblem(subProblem);
            }
            catch (ClassCastException e) {
                throw new InitialisationException("Algorithm is not an OptimisationAlgorithm");
            }
            
            optimisers[i].initialise();
            
            ParticipatingAlgorithm participant;
            try {
                participant = (ParticipatingAlgorithm) optimisers[i];
            }
            catch (ClassCastException e) {
                throw new InitialisationException("Algorithm is not a ParticipatingAlgorithm");
            }
            
            for (int j = 0; j < subProblem.getDimension(); ++j) {
                context[subProblem.getOffset() + j] = participant.getContribution()[j];
            }
        }
    }    
    
    public void performIteration() {
        ParticipatingAlgorithm participant = null;
        for (int i = 0; i < participants; ++i) {
            participant = (ParticipatingAlgorithm) optimisers[i];
            
            CoOperativeOptimisationProblemAdapter adapter = 
                (CoOperativeOptimisationProblemAdapter)
                    ((OptimisationAlgorithm) optimisers[i]).getOptimisationProblem();
            
            adapter.updateContext(context);
            
            optimisers[i].performIteration();

            for (int j = 0; j < adapter.getDimension(); ++j) {
                context[adapter.getOffset() + j] = participant.getContribution()[j];
            } 
            
        }
        
        fitness = participant.getContributionFitness();
        for (int i = 0; i < participants - 1; ++i) {
            ((ParticipatingAlgorithm) optimisers[i]).updateContributionFitness(fitness);
        }   
    }
 
    public OptimisationProblem getOptimisationProblem() {
        return problem;
    }
    
    public double[] getSolution() {
        return context;
    }
    
    public void setOptimisationProblem(OptimisationProblem problem) {
        this.problem = problem;
        context = new double[problem.getDimension()];
    }
    
    public void setParticipants(int participants) {
        this.participants = participants;
    }
    
    public double[] getContribution() {
        return context;
    }
    
    public double getContributionFitness() {
        return fitness;
    }
    
    public void updateContributionFitness(double fitness) {
        this.fitness = fitness;
        
        for (int i = 0; i < participants; ++i) {
            ParticipatingAlgorithm participant = 
                (ParticipatingAlgorithm) optimisers[i];
            
            participant.updateContributionFitness(fitness);
        }
    }
    
    public int getFitnessEvaluations() {
        int fitnessEvaluations = 0;
        for (int i = 0; i < participants; ++i) {
            fitnessEvaluations += 
                ((OptimisationAlgorithm) optimisers[i]).getFitnessEvaluations();
        }
        return fitnessEvaluations;
    }
    
    public double getSolutionFitness() {
        return fitness;
    }
    
    private int participants;
    private AlgorithmFactory factory;
    private OptimisationProblem problem;
    private double[] context;
    private double fitness;
    private Algorithm[] optimisers;
    
    private class CoOperativeOptimisationProblemAdapter implements OptimisationProblem {
    
        public CoOperativeOptimisationProblemAdapter(OptimisationProblem problem, int dimension, int offset) {
            this.problem = problem;
            this.dimension = dimension;
            this.offset = offset;
            context = new double[problem.getDimension()];
        }
    
        public int getDimension() {
            return dimension;
        }
    
        public int getOffset() {
            return offset;
        }
        
        public void updateContext(double[] context) {
            for (int i = 0; i < problem.getDimension(); ++i) {
                this.context[i] = context[i];
            }
        }
    
        public Domain getDomain(int component) {
            return problem.getDomain(offset + component);
        }
    
        public double getFitness(double[] solution) {
            for (int i = 0; i < dimension; ++i) {
                context[offset + i] = solution[i];
            }
            return problem.getFitness(context);
        }
    
        private OptimisationProblem problem;
        private int offset;
        private int dimension;
        private double[] context;
    }
}
