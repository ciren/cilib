/*
 * CoOperativeOptimisationAglorithm.java
 *
 * Created on January 24, 2003, 11:44 AM
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

package net.sourceforge.cilib.Algorithm;

import java.util.ArrayList;

import net.sourceforge.cilib.Domain.Component;
import net.sourceforge.cilib.Domain.Composite;
import net.sourceforge.cilib.Domain.Vector;
import net.sourceforge.cilib.Problem.Fitness;
import net.sourceforge.cilib.Problem.InferiorFitness;
import net.sourceforge.cilib.Problem.OptimisationProblem;
import net.sourceforge.cilib.Problem.OptimisationProblemAdapter;
import net.sourceforge.cilib.Problem.OptimisationSolution;

/** 
 * This class implements a generalised co-operative optimisation algorithm.
 * <p />
 * Any algorithm that wishes to participate in a co-operative optimisation algorithm must implement the
 * {@link ParticipatingAlgorithm} interface. This class also implements ParticipatingAlgorithm meaning that
 * co-operative algorithms can be composed of co-operative algorithms again. 
 * <p />
 * The original co-operative PSO on which this class is based 
 *  is due to F. van den Bergh, "An Analysis of Particle Swarm Optimizers",
 *          PhD thesis, Department of Computer Science, 
 *          University of Pretoria, South Africa, 2002.
 *
 * @author  espeer
 */
public class CoOperativeOptimisationAlgorithm extends Algorithm implements OptimisationAlgorithm, ParticipatingAlgorithm {
    
    /** Creates a new instance of CoOperativeOptimisationAglorithm */
    public CoOperativeOptimisationAlgorithm() {
        participants = 1;
        fitness = InferiorFitness.instance();
        updateContributionFitness = true;
    }
    
    /**
     * Sets the algorithm factory used to create new participating algorithms.
     * <p />
     * The algorithm factory must produce algorithms that conform to the {@link ParticipatingAlgorithm} interface. 
     * @param factory An {@AlgorithmFactory} for creating partipants.
     */
    public void setAlgorithmFactory(AlgorithmFactory factory) {
        this.factory = factory;
    }
    
    /**
     * Initialises the co-operative algorithm. 
     * <p />
     * The optimisation problem is boken up into components according to the number of 
     * participating algorithms. Multiple participating algorithms are created from the algorithm 
     * factory and are each given a problem adapter that  only exposes them to the component of the 
     * problem that they are responsible for. The problem adapter maintains its portion of the 
     * context vector which comprises of all the components of the original problem.
     */
    public void initialise() {
        super.initialise();
        
        if (participants == 0) {
            participants = problem.getDomain().getDimension();
        }
        
        optimisers = new Algorithm[participants];
        int dim = problem.getDomain().getDimension() / participants;
        int extras = problem.getDomain().getDimension() % participants;
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
    
    protected void performIteration() {
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
        
        if (updateContributionFitness) {
            fitness = participant.getContributionFitness();
            for (int i = 0; i < participants - 1; ++i) {
                ((ParticipatingAlgorithm) optimisers[i]).updateContributionFitness(fitness);
            }
        }
    }
 
    public OptimisationProblem getOptimisationProblem() {
        return problem;
    }
    
    public void setOptimisationProblem(OptimisationProblem problem) {
        this.problem = problem;
        context = new double[problem.getDomain().getDimension()];
    }
    
    /** 
     * Sets the number of partipating algorithms.
     *
     * @param participants The number of participants.
     */
    public void setParticipants(int participants) {
        this.participants = participants;
    }

	public int getParticipants() {
		return participants;
	}
    
    public double[] getContribution() {
        return context;
    }
    
    public Fitness getContributionFitness() {
        return fitness;
    }
    
    public void updateContributionFitness(Fitness fitness) {
        this.fitness = fitness;
        
        for (int i = 0; i < participants; ++i) {
            ParticipatingAlgorithm participant = 
                (ParticipatingAlgorithm) optimisers[i];
            
            participant.updateContributionFitness(fitness);
        }
    }
    
    /**
     * Determines whether this co-operative algorithm will update the fitness of participants.
     * Participants that execute later during an iteration may improve the fitness of the solution context.
     * This determines if the contribution's fitness is updated when this happens.
     * 
     * @param updateContributionFitness if true then fitness is updated, otherwise not. 
     */
    public void setUpdateContributionFitness(boolean updateContributionFitness) {
        this.updateContributionFitness = updateContributionFitness;
    }
    
    public OptimisationSolution getBestSolution() {
        return new OptimisationSolution(problem, context.clone());
    }
    
    public java.util.Collection getSolutions() {
        ArrayList solutions = new ArrayList(1);
        solutions.add(getBestSolution());
        return solutions;
    }
    
    private int participants;
    private AlgorithmFactory factory;
    private OptimisationProblem problem;
    private double[] context;
    private Fitness fitness;
    private Algorithm[] optimisers;
    private boolean updateContributionFitness;
    
    private class CoOperativeOptimisationProblemAdapter extends OptimisationProblemAdapter {
    
        public CoOperativeOptimisationProblemAdapter(OptimisationProblem problem, int dimension, int offset) {
            this.problem = problem;
            this.dimension = dimension;
            this.offset = offset;
            domain = (Vector) problem.getDomain();
            context = new double[domain.getDimension()];
            ArrayList components = new ArrayList();
            for (int i = 0; i < dimension; ++i) {
                components.add(domain.getComponent(offset + i));           
            }
            domain = new Composite(components);
        }
    
        public int getDimension() {
            return dimension;
        }
    
        public int getOffset() {
            return offset;
        }
        
        public void updateContext(double[] context) {
            for (int i = 0; i < problem.getDomain().getDimension(); ++i) {
                this.context[i] = context[i];
            }
        }
    
        public Component getDomain() {
            return domain;
        }
    
        protected Fitness calculateFitness(Object solution) {
            double[] tmp = (double[]) solution;
            for (int i = 0; i < dimension; ++i) {
                context[offset + i] = tmp[i];
            }
            return problem.getFitness(context, true);
        }
    
        private OptimisationProblem problem;
        private int offset;
        private int dimension;
        private double[] context;
        private Vector domain;
    }
}
