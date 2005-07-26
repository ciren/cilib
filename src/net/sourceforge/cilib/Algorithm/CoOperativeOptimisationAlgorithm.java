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
import net.sourceforge.cilib.Problem.Fitness;
import net.sourceforge.cilib.Problem.InferiorFitness;
import net.sourceforge.cilib.Problem.OptimisationProblem;
import net.sourceforge.cilib.Problem.OptimisationProblemAdapter;
import net.sourceforge.cilib.Problem.OptimisationSolution;
import net.sourceforge.cilib.Type.DomainParser;
import net.sourceforge.cilib.Type.Types.MixedVector;
import net.sourceforge.cilib.Type.Types.Type;
import net.sourceforge.cilib.Type.Types.Vector;

/** 
 * This abstract class forms that basis for any Co-Operative Optimisation implementations.
 * <p />
 * Any algorithm that wishes to participate in a co-operative optimisation algorithm must implement the
 * {@link ParticipatingAlgorithm} interface. This class also implements ParticipatingAlgorithm meaning that
 * co-operative algorithms can be composed of co-operative algorithms again. 
 * <p />
 *
 * @author  espeer
 */
public abstract class CoOperativeOptimisationAlgorithm extends Algorithm implements OptimisationAlgorithm, ParticipatingAlgorithm {
    
    /** Creates a new instance of CoOperativeOptimisationAglorithm */
    public CoOperativeOptimisationAlgorithm() {
        participants = 1;
        fitness = InferiorFitness.instance();
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
    public void performInitialisation() {
        
        if (participants == 0) {
            //participants = problem.getDomain().getDimension();
        	participants = DomainParser.getInstance().getDimension();
        }
        
        optimisers = new Algorithm[participants];
        //int dim = problem.getDomain().getDimension() / participants;
        //int extras = problem.getDomain().getDimension() % participants;
        int dim = DomainParser.getInstance().getDimension() / participants;
        int extras = DomainParser.getInstance().getDimension() % participants;
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
                //context[subProblem.getOffset() + j] = participant.getContribution()[j];
            	//context[subProblem.getOffset() + j] = participant.getContribution().getReal(j);
            	context.setReal(subProblem.getOffset() + j, participant.getContribution().getReal(j));
            }
        }
    }    
    
 
    public OptimisationProblem getOptimisationProblem() {
        return problem;
    }
    
    public void setOptimisationProblem(OptimisationProblem problem) {
        this.problem = problem;
        //context = new double[problem.getDomain().getDimension()];
        //context = new double[DomainParser.getInstance().getDimension()];
        context = new MixedVector(DomainParser.getInstance().getDimension());
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
    
    //public double[] getContribution() {
    public Vector getContribution() {
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
    

    
    public OptimisationSolution getBestSolution() {
    	OptimisationSolution solution = null;
    	
    	try {
    		solution = new OptimisationSolution(problem, context.clone());
    	}
    	catch (CloneNotSupportedException c) {
    		c.printStackTrace();
    	}
        
    	return solution;
    }
    
    public java.util.Collection<OptimisationSolution> getSolutions() {
        ArrayList<OptimisationSolution> solutions = new ArrayList<OptimisationSolution>(1);
        solutions.add(getBestSolution());
        return solutions;
    }
    
    protected int participants;
    private AlgorithmFactory factory;
    protected OptimisationProblem problem;
    //protected double[] context;
    protected Vector context;
    protected Fitness fitness;
    protected Algorithm[] optimisers;
    
    protected class CoOperativeOptimisationProblemAdapter extends OptimisationProblemAdapter {
    
        public CoOperativeOptimisationProblemAdapter(OptimisationProblem problem, int dimension, int offset) {
            this.problem = problem;
            this.dimension = dimension;
            this.offset = offset;
            domain = (net.sourceforge.cilib.Type.Types.Vector) DomainParser.getInstance().getBuiltRepresentation();
            //context = new double[domain.getDimension()];
            context = new MixedVector(domain.getDimension());
            /*ArrayList<DomainComponent> components = new ArrayList<DomainComponent>();
            for (int i = 0; i < dimension; ++i) {
                components.add(domain.getComponent(offset + i));           
            }
            domain = new Composite(components);*/
            ArrayList<Type> components = new ArrayList<Type>();
            for (int i = 0; i < dimension; ++i) {
            	components.add(domain.get(offset+i));
            }
        }
    
        public int getDimension() {
            return dimension;
        }
    
        public int getOffset() {
            return offset;
        }
        
        //public void updateContext(double[] context) {
        public void updateContext(Vector context) {
            for (int i = 0; i < DomainParser.getInstance().getDimension(); ++i) {
                //this.context[i] = context[i];
            	this.context.setReal(i, context.getReal(i));
            }
        }
    
        /*public DomainComponent getDomain() {
            return domain;
        }*/
        /*public Domain getDomain() {
        	return Domain.getInstance();
        }*/
    
        protected Fitness calculateFitness(Object solution) {
            double[] tmp = (double[]) solution;
            for (int i = 0; i < dimension; ++i) {
                //context[offset + i] = tmp[i];
            	context.setReal(offset+i,tmp[i]);
            }
            return problem.getFitness(context, true);
        }
    
        private OptimisationProblem problem;
        private int offset;
        private int dimension;
        //private double[] context;
        private Vector context;
        private net.sourceforge.cilib.Type.Types.Vector domain;
    }
}
