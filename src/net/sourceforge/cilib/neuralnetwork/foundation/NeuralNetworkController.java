/*
 * NeuralNetworkController.java
 * 
 * Created on Dec 06, 2004
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



import java.util.List;

import net.sourceforge.cilib.algorithm.SingularAlgorithm;
import net.sourceforge.cilib.problem.OptimisationSolution;

/**
 * @author stefanv
 *
 * This is the base Algorithm implementation to run "stand-alone" neural network simulations.  Use this
 * class if you want to create a neural network that uses a custom stand-alone training algorithm such
 * as Gradient Decent.  This class works 'as is' and no further development should be needed in most 
 * cases - the NN functionality is incorporated into NeuralNetworkProblem.
 * 
 * If more functionality is required in the performIteration method or elsewhere, the class can be extended.
 */
public class NeuralNetworkController extends SingularAlgorithm {

	protected NeuralNetworkProblem NNProblem = null;
	protected NNError errorDt = null;
	
	public NeuralNetworkController() {
		
	}
	
	public NeuralNetworkController(NeuralNetworkController copy) {
		
	}
	
	public NeuralNetworkController clone() {
		return new NeuralNetworkController(this);
	}
	
	/**
	 * 
	 */
	public NeuralNetworkController(NeuralNetworkProblem NNProblem_) {
		super();
		this.NNProblem = NNProblem_;
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.Algorithm.Algorithm#performIteration()
	 */
	public void performIteration() {
		
		errorDt = NNProblem.learningEpoch();
		System.out.println("Epoch " + this.getIterations() + " completed, error: " + ((Double)errorDt.getValue()).doubleValue());
	}

	/**
	 * @return Returns the error.
	 */
	public NNError getError() {
		return errorDt;
	}
	/**
	 * @param error The error to set.
	 */
	public void setError(NNError error) {
		this.errorDt = error;
	}
	
	
	
			
	/**
	 * @return Returns the nNProblem.
	 */
	public NeuralNetworkProblem getNNProblem() {
		return NNProblem;
	}
	/**
	 * @param problem The nNProblem to set.
	 */
	public void setNNProblem(NeuralNetworkProblem problem) {
		NNProblem = problem;
	}

	@Override
	public OptimisationSolution getBestSolution() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OptimisationSolution> getSolutions() {
		// TODO Auto-generated method stub
		return null;
	}
}
