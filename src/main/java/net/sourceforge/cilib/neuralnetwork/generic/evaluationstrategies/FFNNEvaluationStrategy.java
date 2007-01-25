/*
 * FFNNEvaluationStrategy.java
 * 
 * Created on Apr 14, 2005
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
 * This class is an implementation of the EvaluationStrategy abstract class. It provides an implementation of the 
 * evaluate() method that merely evaluates a single NNPattern and returns the NN output.  The learningEpoch()
 * method itereates over the datasource's training set and performs invokes the trainer in a stochastic fashion.
 * 
 */
public class FFNNEvaluationStrategy extends EvaluationStrategy {

	protected NeuralNetworkDataIterator iterator = null;
	
	
	/**
	 * @param topology_
	 * @param data_
	 * @param factory_
	 * @param trainer_
	 */
	public FFNNEvaluationStrategy(NeuralNetworkTopology topology_, NeuralNetworkData data_, NNError base, TrainingStrategy trainer_) {
		super(topology_, data_, base, trainer_);
		iterator = data.getTrainingSetIterator();
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Foundation.EvaluationStrategy#evaluate()
	 */
	public void learningEpoch() {
		
		NNError epochError = baseError.clone();      
				
		iterator = data.getTrainingSetIterator();
		iterator.reset();
		
		trainer.preEpochActions(null);
		
		//iterate over each applicable pattern in dataset
		while(iterator.hasMore()){
			
			ArrayList output = topology.evaluate(iterator.value());
					
			NNError patternError = baseError.clone();
			
			//compute the per pattern error, use it to train the topology stochastically be default
			patternError.addIteration(output, iterator.value());
			
			
			trainer.invokeTrainer(iterator.value());
			
			epochError.add(patternError);
			
			iterator.next();
		}
		
		trainer.postEpochActions(null);
				
		epochError.postEpochActions();
		
		//set the respective errors
		this.errorDt = epochError;
		
		data.shuffleTrainingSet();
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.NeuralNetwork.Foundation.EvaluationStrategy#evaluate(net.sourceforge.cilib.NeuralNetwork.Foundation.NNPattern)
	 */
	public Object evaluate(NNPattern p) {
		return topology.evaluate(p);
	}
}
