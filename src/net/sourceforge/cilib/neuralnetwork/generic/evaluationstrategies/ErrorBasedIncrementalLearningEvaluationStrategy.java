/*
 * ErrorBasedIncrementalLearningEvaluationStrategy.java
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
package net.sourceforge.cilib.neuralnetwork.generic.evaluationstrategies;

import java.util.ArrayList;

import net.sourceforge.cilib.neuralnetwork.MSEErrorFunction;
import net.sourceforge.cilib.neuralnetwork.foundation.EvaluationStrategy;
import net.sourceforge.cilib.neuralnetwork.foundation.NNError;
import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkData;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkDataIterator;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkTopology;
import net.sourceforge.cilib.neuralnetwork.foundation.TrainingStrategy;


/**
 * @author stefanv
 *
 *
 * 
 */
public class ErrorBasedIncrementalLearningEvaluationStrategy extends EvaluationStrategy {
	
	protected NeuralNetworkDataIterator iteratorDt = null;
	protected NeuralNetworkDataIterator iteratorDv = null;
	
	//termination criterion 1
	int maxSubsetEpochs = 10;
	int subsetEpochCounter = 0;
	
	//termination criterion 2
	NNError subsetBeginErrorDt = null;
	NNError subsetBeginErrorDv = null;
	
	//termination criterion 3
	double minimumErrorDecrease = 0.001;
	double orderOfMagnitudeIndicator;
	double errorDtPrevious;
	
	//termination criterion 4
	double averageErrorDv = 0.0;
	int epochNumber = 0;
	ArrayList<NNError> history = null;
	
	

	/**
	 * @param topology
	 * @param data
	 * @param factory
	 */
	public ErrorBasedIncrementalLearningEvaluationStrategy(NeuralNetworkTopology topology_,
								   NeuralNetworkData data_, 
								   NNError base,
								   TrainingStrategy trainer_) {
		
		super(topology_, data_, base, trainer_);
		
		subsetBeginErrorDt = baseError.clone();
		subsetBeginErrorDv = baseError.clone();
		
		iteratorDt = data.getTrainingSetIterator();
		iteratorDv = data.getValidationSetIterator();
		
		//set the initial error to the error on Dt = 1 pattern.
		ArrayList tmpO = topology.evaluate(iteratorDt.value());
		subsetBeginErrorDt.addIteration(tmpO, iteratorDt.value());
		subsetBeginErrorDv.addIteration(tmpO, iteratorDt.value());
		
		//set the recoded error for criterion 3 to initial error as well
		orderOfMagnitudeIndicator = ((Double)subsetBeginErrorDt.getValue()).doubleValue();
		errorDtPrevious = 9999999;
		
		//criterion 4:
		history = new ArrayList<NNError>();
		
	}
		
	
	public void learningEpoch(){
					
		//set the number of patterns in Errors from Dt here...
		((MSEErrorFunction) baseError).setNoPatterns(data.getTrainingSetSize());
					
		NNError epochErrorDt = baseError.clone();
		((MSEErrorFunction)epochErrorDt).setNoPatterns(data.getTrainingSetSize());
		
		NNError epochErrorDv = baseError.clone(); 
		((MSEErrorFunction)epochErrorDv).setNoPatterns(data.getTrainingSetSize());
		
		
		//determine training error, perform learning steps
		//================================================
		iteratorDt.reset();
		
		trainer.preEpochActions(null);
		
		//iterate over training set
		while(iteratorDt.hasMore()){
			
			ArrayList outputDt = topology.evaluate(iteratorDt.value());
			
			NNError patternError = baseError.clone();
			//compute the per pattern error, use it to train the topology stochastically be default
			patternError.addIteration(outputDt, iteratorDt.value());
						
			trainer.invokeTrainer(iteratorDt.value());
			
			epochErrorDt.add(patternError);
				
			iteratorDt.next();
		}
		
		trainer.postEpochActions(null);
		epochErrorDt.postEpochActions();
		
		
		
		
		//determine validation error
		//==========================
		iteratorDv.reset();
		
		while(iteratorDv.hasMore()){
			
			ArrayList outputDv = topology.evaluate(iteratorDv.value());
			
			NNError patternError = baseError.clone();
			//compute the per pattern error, use it to train the topology stochastically be default
			patternError.addIteration(outputDv, iteratorDv.value());
			
			epochErrorDv.add(patternError);
			
			iteratorDv.next();
		}
		epochErrorDv.postEpochActions();
		
		
				
		
		//determine one of the subset termination criteria has been reached.
		//-----------------------------------------------------------------
		
		boolean terminate = false;
		
		//Criterion 1: maximum epochs per subset reached
		subsetEpochCounter++;
		if(subsetEpochCounter >= maxSubsetEpochs){
			terminate = true;
			System.out.println("___________Termination criterion 1: maximum epochs reached");
		}
		
		//criterion 2: error decreased by more than 80%
		double decreasePercentageDt = ((Double)epochErrorDt.getValue() / (Double)subsetBeginErrorDt.getValue()) * 100;
		double decreasePercentageDv = ((Double)epochErrorDv.getValue() / (Double)subsetBeginErrorDv.getValue()) * 100;
		
		if(decreasePercentageDt < 20.0){
			terminate = true;
		}
		
		if(decreasePercentageDv < 20.0){
			terminate = true;
			System.out.println("___________Termination criterion 2: error decreased by 80%");
			System.out.println("____ErrorDt = " + (Double)epochErrorDt.getValue() 
					           + " , ErrorDt start = " + (Double)subsetBeginErrorDt.getValue());
		}
		
		
		//criterion 3: average decrease per epoch too small		
		//update minimum decrease factor
		if ( (Double)epochErrorDt.getValue() < orderOfMagnitudeIndicator / 10){
			orderOfMagnitudeIndicator = ((Double)epochErrorDt.getValue()).doubleValue();
			minimumErrorDecrease /= 10;
		}
		
		if( (errorDtPrevious - (Double)epochErrorDt.getValue() < minimumErrorDecrease) 
			&& (errorDtPrevious - (Double)epochErrorDt.getValue() > 0)  ){
			terminate = true;
			System.out.println("___________Termination criterion 3: average error too small");
			System.out.println("____min decrease = " + minimumErrorDecrease 
					          + " , actual = " + (errorDtPrevious - (Double)epochErrorDt.getValue()) );
		}
				
		//save the local var epochErrorDt as previous
		errorDtPrevious = ((Double)epochErrorDt.getValue()).doubleValue();
				
		
		//criterion 4: Error on Dv increases too much
		//average = (average * periods + new_sample) / (periods + 1)
		epochNumber++;
		averageErrorDv = ((averageErrorDv * (epochNumber - 1)) + ((Double)epochErrorDv.getValue()).doubleValue())
						  / epochNumber;
		
		history.add(epochErrorDt);
		double errorDvStdDev = 0;
		
		for(int i = 0; i < history.size(); i++){
			errorDvStdDev += Math.pow(((Double)history.get(i).getValue()).doubleValue() - averageErrorDv,2);
		}
		
		errorDvStdDev = Math.sqrt(errorDvStdDev / history.size());		
		
		
		if(((Double)epochErrorDv.getValue()).doubleValue() > (averageErrorDv + errorDvStdDev)){
			terminate = true;
			System.out.println("___________Termination criterion 4: Validation error rising");
		}
		
		
		
		//if termination criteria --> update Dt from Dc
		//=================================================
		if(terminate){
			
			data.activeLearningUpdate(null);
			
			
			//criterion 1 reset
			subsetEpochCounter = 0;
			
			//criterion 2 reset
			((MSEErrorFunction)subsetBeginErrorDt).setValue(((Double)epochErrorDt.getValue()).doubleValue());
			((MSEErrorFunction)subsetBeginErrorDv).setValue(((Double)epochErrorDv.getValue()).doubleValue());
			
			
			System.out.println("Error based Incremental Learning update...");
			/*System.out.println("candidate set size      : " + data.getCandidateSetSize());
			System.out.println("training set size       : " + data.getTrainingSetSize());
			System.out.println("generalisation set size : " + data.getGeneralisationSetSize());
			System.out.println("validation set size     : " + data.getValidationSetSize());*/
		}
		
		//Set the respective Class Error variables for measurements
		this.errorDt = epochErrorDt;
		this.errorDv = epochErrorDv;
			
	}


	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Foundation.EvaluationStrategy#evaluate(net.sourceforge.cilib.NeuralNetwork.Foundation.NNPattern)
	 */
	public Object evaluate(NNPattern p) {
		return topology.evaluate(p);
	}
	
}
