/*
 * NeuralNetworkProblem.java
 * 
 * Created on Nov 30, 2004
 *
 * Copyright (C) 2004 - CIRG@UP 
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
package net.sourceforge.cilib.neuralnetwork.foundation;

import java.util.ArrayList;

import net.sourceforge.cilib.neuralnetwork.StandardPattern;
import net.sourceforge.cilib.neuralnetwork.generic.TopologyException;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.dataset.DataSetBuilder;
import net.sourceforge.cilib.type.DomainRegistry;

/**
 * @author stefanv
 * 
 * This class is the main interface between any CIlib algorithm and a NN implementation.  By changing the 
 * EvaluationStrategy object, any behaviour can be obtained i.e. batch or stochastic learning, Active or 
 * Fixed set learning, etc.
 * 
 * In many ways NeuralNetworkProblem is merely the implementation of Facade pattern to access the NN 
 * functionality of EvaluationStrategy, NeuralNetworkTopology and TrainingStrategy.  NeuralNetworkProblem 
 * (together with NeuralNetworkController) should amost never need any modification.  This class can be 
 * used "as-is" with the Generic Neural Network Framework as well as any other "typical" NN writen in the
 * Foundation Neural Network Framework.
 *  
 * 
 */
public class NeuralNetworkProblem implements OptimisationProblem {
	
	protected int fitnessEvaluations;
	protected NNPattern basePattern = null;
	protected EvaluationStrategy evaluationStrategy = null;

	//--------------------------------------
	
	public NeuralNetworkProblem(EvaluationStrategy evaluationStrategy_){
		this.evaluationStrategy = evaluationStrategy_;
		this.fitnessEvaluations = 0;	
	}
	
	public NeuralNetworkProblem(NeuralNetworkProblem copy) {
		
	}
	
	public NeuralNetworkProblem clone() {
		return new NeuralNetworkProblem(this);
	}

	
	/**
	 * @return training error of network
	 * 
	 * This method invokes a learning epoch in the EvaluationStrategy class.  By default it returns 
	 * the training error for the epoch.  Training behaviour is dictated in the EvaluationStrategy object.
	 * 
	 */
	public NNError learningEpoch(){
		evaluationStrategy.performLearning();
		return evaluationStrategy.getErrorDt();
	}
	
	
	/**
	 * @param in - the inputs to the network
	 * @return the output of the pattern.
	 * 
	 * This method contructs a pattern from the given input and evaluates it against the NN topology.
	 * The output is returned.
	 */
	public Object evaluate(ArrayList in){
		NNPattern p = new StandardPattern(in,null);
		return evaluationStrategy.evaluate(p);
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.Problem.OptimisationProblem#getFitness(java.lang.Object, boolean)
	 * 
	 */
	public Fitness getFitness(Object solution, boolean count) {
		
		if (count) {
    		++fitnessEvaluations;
    	}
        //if (getDomain().isInDomain(solution)) {
        	try {
				this.getTopology().setWeights((ArrayList) solution);
			} catch (TopologyException e) {
				System.out.println("NeuralNetworkProblem error: " +e.getMessage());
				e.printStackTrace();
			}
            
            return evaluationStrategy.getErrorDt();
      //  }
       // else {
       // 	return InferiorFitness.instance();
      //  }
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.Problem.OptimisationProblem#getFitnessEvaluations()
	 */
	public int getFitnessEvaluations() {
		return this.fitnessEvaluations;
	}

	public NeuralNetworkTopology getTopology() {
		return evaluationStrategy.getTopology();
	}


	public void setTopology(NeuralNetworkTopology topology) {
		evaluationStrategy.setTopology(topology);
	}
	
	public EvaluationStrategy getEvaluationStrategy() {
		return evaluationStrategy;
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
