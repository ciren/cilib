/*
 * User.java
 * 
 * Created on Dec 29, 2004
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
package net.sourceforge.cilib.neuralnetwork.basicffnn;

import java.io.IOException;
import java.util.ArrayList;
import net.sourceforge.cilib.neuralnetwork.DefaultData;
import net.sourceforge.cilib.neuralnetwork.MSEErrorFunction;
import net.sourceforge.cilib.neuralnetwork.StandardPattern;
import net.sourceforge.cilib.neuralnetwork.foundation.NNError;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkController;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkData;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkProblem;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkTopology;
import net.sourceforge.cilib.neuralnetwork.foundation.TrainingStrategy;
import net.sourceforge.cilib.neuralnetwork.generic.evaluationstrategies.FFNNEvaluationStrategy;
import net.sourceforge.cilib.stoppingcondition.MaximumIterations;
 
/**
 * @author stefanv
 * 
 * 
 */
public class TrainAndTestNN {

	public static void main(String[] args) {
						
		NeuralNetworkTopology NNtopology = new FFNNTopology(2, 2, 1, 0.5, 0.2);
		TrainingStrategy train = new FFNNTrainingStrategy((FFNNTopology)NNtopology);
		
		NeuralNetworkData data = null;
		
		try {
			data = new DefaultData("c:\\temp\\easyfunc.txt", 2, 0, 90, 10, 0);
			data.populate();
		} catch (IOException e) {
			
			e.printStackTrace();
		}	
			
		
		NNError err = new MSEErrorFunction(1,data.getTrainingSetSize());
				
		//use the Generic Package's FFNNEvaluationStrategy.
		FFNNEvaluationStrategy eval = new FFNNEvaluationStrategy(NNtopology, 
														 data, 
														 err,
														 train);
		
				
		NeuralNetworkProblem NNprob = new NeuralNetworkProblem(eval);
				
		NeuralNetworkController NNControl = new NeuralNetworkController(NNprob);
				
		NNControl.addStoppingCondition(new MaximumIterations(1000));
				
		System.out.println("Configuration completed...");
		//-----------------------------------------------------------------------------------------------------------
		
		NNControl.initialise();
		//needed by Algorithm...
		
		NNControl.run();
		//run Algorithm
		
		
		//create a pattern manually and test output
		ArrayList<Double> in = new ArrayList<Double>(2);
		
		in.add(new Double(1)); in.add(new Double(1));
		
		StandardPattern p = new StandardPattern(in, null);
		
		ArrayList result = NNtopology.evaluate(p);
		
		//output line - add any status here that reports on the manual pattern entered.
		System.out.println("test result: 1 and 1 = 0.6    -->  " + ((Double)result.get(0)).doubleValue());
		
		System.out.println("data stats:\n\n");
		
		System.out.println("candidate set size      : " + data.getCandidateSetSize());
		System.out.println("training set size       : " + data.getTrainingSetSize());
		System.out.println("generalisation set size : " + data.getGeneralisationSetSize());
		System.out.println("validation set size     : " + data.getValidationSetSize());
		
		
		
	}
}
