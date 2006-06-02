/*
 * CoOperativeOptimisationAglorithm.java
 *
 * Created on January 24, 2003, 11:44 AM
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

package net.sourceforge.cilib.algorithm;

import java.util.ArrayList;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationProblemAdapter;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.MixedVector;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.Vector;

/** 
 * This abstract class forms that basis for any Co-Operative Optimisation implementations.
 * <p />
 * Any algorithm that wishes to participate in a co-operative optimisation algorithm must implement the
 * {@link ParticipatingAlgorithm} interface. This class also implements ParticipatingAlgorithm meaning that
 * co-operative algorithms can be composed of co-operative algorithms again. 
 * <p />
 *
 * @author  Edwin Peer
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
            participants = problem.getDomain().getDimension();
        	//participants = DomainParser.getInstance().getDimension();
        }
        
        //we need an algorithm for each part of the problem that will be optimised, these algorithms are the participants
        optimisers = new Algorithm[participants];
        //determine what the dimensions are for each of the sub-problems
        int dim = problem.getDomain().getDimension() / participants;
        //determine how many remaining dimensions there are (0 <= extras < dim)
        int extras = problem.getDomain().getDimension() % participants;
        //int dim = DomainParser.getInstance().getDimension() / participants;
        //int extras = DomainParser.getInstance().getDimension() % participants;
        int offset = 0;
        for (int i = 0; i < participants; ++i) {
        	//let the AlgorithmFactory return an Algorithm object of the correct type
        	//QUESTION not sure how AlgorithmFactory works; it seems as though the the classes that are specified in the XML gets created by the AlgorithmFactory
            optimisers[i] = factory.newAlgorithm();
            
            //CoOperativeOptimisationProblemAdapter exposes only that component of the full problem that the subProblem is responsible for
            CoOperativeOptimisationProblemAdapter subProblem;
            if (extras > 0) {
            	//the case where the problem cannot be split up into equal dimensions, i.e. there are remaining dimensions
                --extras;
                //QUESTION the first "extras" sub-problems will all have dimension "dim+1", after that the sub-problems will have dimension "dim"
                //QUESTION is there a specific reason for doing it this way?
                //QUESTION what other way can this be made more generic, so that the user can specify how to split up the problem?
                subProblem = 
                    new CoOperativeOptimisationProblemAdapter(problem, dim + 1, offset);
                offset += (dim + 1);
            }
            else {
            	//the case where the problem is split up into equal dimensions
                subProblem = 
                    new CoOperativeOptimisationProblemAdapter(problem, dim, offset);
                offset += dim;
            }
            
            try {
            	//assign the newly created sub-problem to the i'th participating algorithm
                ((OptimisationAlgorithm) optimisers[i]).setOptimisationProblem(subProblem);
            }
            catch (ClassCastException e) {
                throw new InitialisationException("Algorithm is not an OptimisationAlgorithm");
            }
            //initialise the i'th particiating Algorithm
            optimisers[i].initialise();
            
            //make sure that the i'th algorithm is a participating algorithm and try to convert it
            ParticipatingAlgorithm participant;
            try {
                participant = (ParticipatingAlgorithm) optimisers[i];
            }
            catch (ClassCastException e) {
                throw new InitialisationException("Algorithm is not a ParticipatingAlgorithm");
            }
            
            //get the contribution (vector) that the i'th sub-problem is making to the full problem
            Vector contribution = (Vector) participant.getContribution();
            //copy each dimension value from the i'th sub-problem's contribution vector over to the correct poition in the context vector of the full problem
            for (int j = 0; j < subProblem.getDimension(); ++j) {
                //context[subProblem.getOffset() + j] = participant.getContribution()[j];
            	//context[subProblem.getOffset() + j] = participant.getContribution().getReal(j);
            	context.setReal(subProblem.getOffset() + j, contribution.getReal(j));
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
        
        //context = new MixedVector(DomainParser.getInstance().getDimension());
        context = new MixedVector(problem.getDomain().getDimension());
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
    
    //all the participating algorithms' best solution (particle) is set the context vector's fitness
    public void updateContributionFitness(Fitness fitness) {
		this.fitness = fitness;
		for (int i = 0; i < participants; ++i) {
			ParticipatingAlgorithm participant = (ParticipatingAlgorithm) optimisers[i];
			participant.updateContributionFitness(fitness);
		}
    }
    

    //QUESTION this method just creates a solution object of the current problem
    public OptimisationSolution getBestSolution() {
    	OptimisationSolution solution = null;
    	
    	//try {
    		solution = new OptimisationSolution(problem, context.clone());
    	//}
    	//catch (CloneNotSupportedException c) {
    	//	c.printStackTrace();
    	//}
        
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
    
    	/**
    	 * Creates an OptimisationProblemAdpter that has the specified dimension starting at the given offset (index) position of the full given problem.
    	 * @param problem the full problem that is being split up
    	 * @param dimension the dimension that that this sub-problem should be
    	 * @param offset the offset (index) position in the full-problem where this sub-problem should start
    	 */
        public CoOperativeOptimisationProblemAdapter(OptimisationProblem problem, int dimension, int offset) {
            this.problem = problem;
            this.dimension = dimension;
            this.offset = offset;
            //domain = (net.sourceforge.cilib.Type.Types.Vector) DomainParser.getInstance().getBuiltRepresentation();
            domain = (net.sourceforge.cilib.type.types.Vector) problem.getDomain().getBuiltRepresenation();
            //context = new double[domain.getDimension()];
            context = new MixedVector(domain.getDimension());
            /*ArrayList<DomainComponent> components = new ArrayList<DomainComponent>();
            for (int i = 0; i < dimension; ++i) {
                components.add(domain.getComponent(offset + i));           
            }
            domain = new Composite(components);*/
            //QUESTION what does this code do? The variable is never used again...
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
            for (int i = 0; i < this.getDomain().getDimension(); ++i) {
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
    
        
		public DomainRegistry getDomain() {
			// TODO Auto-generated method stub
			return null;
		}
		
		public DomainRegistry getBehaviouralDomain() {
			// TODO Auto-generated method stub
			return null;
		}
		
		private OptimisationProblem problem;
        private int offset;
        private int dimension;
        //private double[] context;
        private Vector context;
        private net.sourceforge.cilib.type.types.Vector domain;
		
    }
}
