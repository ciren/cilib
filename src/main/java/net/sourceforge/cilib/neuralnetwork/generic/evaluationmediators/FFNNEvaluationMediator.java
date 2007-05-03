/*
 * Created on 2005/04/19
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.neuralnetwork.generic.evaluationmediators;

import net.sourceforge.cilib.neuralnetwork.foundation.EvaluationMediator;
import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkDataIterator;
import net.sourceforge.cilib.type.types.MixedVector;

/**
 * @author stefanv
 *
 * 
 */
public class FFNNEvaluationMediator extends EvaluationMediator {

	protected NeuralNetworkDataIterator iteratorDt = null;
	protected NeuralNetworkDataIterator iteratorDg = null;
	
	
	public FFNNEvaluationMediator() {
		super();
	}

	public void initialize(){
				
		super.initialize();
		
	}
	
	
	public void learningEpoch() {
		
		this.resetError(this.errorDt);
		this.setErrorNoPatterns(this.errorDt, this.data.getTrainingSetSize());
		
		this.resetError(this.errorDg);
		this.setErrorNoPatterns(this.errorDg, this.data.getGeneralisationSetSize());	
		
		iteratorDt = data.getTrainingSetIterator();
				
		trainer.preEpochActions(null);
		
		//iterate over each applicable pattern in training dataset
		while(iteratorDt.hasMore()){
			
			MixedVector output = topology.evaluate(iteratorDt.value());
			this.nrEvaluationsPerEpoch++;
					
			//compute the per pattern error, use it to train the topology stochastically be default
			this.computeErrorIteration(this.errorDt, output, iteratorDt.value());
						
			trainer.invokeTrainer(iteratorDt.value());
						
			iteratorDt.next();
		}
		
		trainer.postEpochActions(null);
		
		//determine generalization error
		//==========================
		iteratorDg = data.getGeneralisationSetIterator();
				
		while(iteratorDg.hasMore()){
			
			MixedVector outputDg = topology.evaluate(iteratorDg.value());
						
			//compute the per pattern error, use it to train the topology stochastically be default
			this.computeErrorIteration(this.errorDg, outputDg, iteratorDg.value());
									
			iteratorDg.next();
		}
				
		//finalise errors
			this.finaliseErrors(this.errorDt);
			this.finaliseErrors(this.errorDg);
				
		data.shuffleTrainingSet();
	}

	
	public MixedVector evaluate(NNPattern p) {
		return topology.evaluate(p);
	}
}
