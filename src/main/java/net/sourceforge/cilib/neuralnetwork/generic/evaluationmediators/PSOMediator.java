package net.sourceforge.cilib.neuralnetwork.generic.evaluationmediators;

import net.sourceforge.cilib.neuralnetwork.foundation.EvaluationMediator;
import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.neuralnetwork.foundation.NeuralNetworkDataIterator;
import net.sourceforge.cilib.type.types.container.MixedVector;

public class PSOMediator extends EvaluationMediator {
	
	protected NeuralNetworkDataIterator iteratorDt = null;
	
	public PSOMediator() {
		super();
	}
	
	public void initialize(){
		
		super.initialize();
		
	}
	
	
	public MixedVector evaluate(NNPattern p) {
		return topology.evaluate(p);	
	}
	
	
	protected void learningEpoch() {
		
		this.resetError(this.errorDt);
		this.setErrorNoPatterns(this.errorDt, this.data.getTrainingSetSize());
		
		iteratorDt = data.getTrainingSetIterator();
		
		//iterate over each applicable pattern in training dataset
		while(iteratorDt.hasMore()){
			
			MixedVector output = topology.evaluate(iteratorDt.value());
			this.nrEvaluationsPerEpoch++;
			
			//compute the per pattern error.
			this.computeErrorIteration(this.errorDt, output, iteratorDt.value());
			
			iteratorDt.next();
		}
				
		//finalise errors
		this.finaliseErrors(this.errorDt);
		
	}
	
}
