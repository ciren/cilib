package net.sourceforge.cilib.neuralnetwork.generic.topologyvisitors;

import net.sourceforge.cilib.neuralnetwork.generic.Weight;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.NeuronConfig;
import net.sourceforge.cilib.type.types.container.MixedVector;

public class WeightExtractingVisitor implements GenericTopologyVisitor {
	
	MixedVector weights = null;
	
	

	public WeightExtractingVisitor() {
		this.weights = new MixedVector();
	}


	public void visitNeuronConfig(NeuronConfig n) {
		
		if(n.getInputWeights() != null){
			Weight[] neuronWeights = n.getInputWeights();
			
			for (int i = 0; i < neuronWeights.length; i++){
				weights.add(neuronWeights[i].getWeightValue().clone());
			}
		}
		
	}


	public MixedVector getWeights() {
		return weights;
	}


	public void setWeights(MixedVector weights) {
		throw new IllegalArgumentException("This operation is not supported for this visitor.");
	}
	
	

}
