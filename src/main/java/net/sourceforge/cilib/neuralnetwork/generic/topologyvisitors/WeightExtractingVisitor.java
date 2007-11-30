package net.sourceforge.cilib.neuralnetwork.generic.topologyvisitors;

import net.sourceforge.cilib.neuralnetwork.generic.Weight;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.NeuronConfig;
import net.sourceforge.cilib.type.types.container.Vector;

public class WeightExtractingVisitor implements GenericTopologyVisitor {
	
	Vector weights = null;
	
	

	public WeightExtractingVisitor() {
		this.weights = new Vector();
	}


	public void visitNeuronConfig(NeuronConfig n) {
		
		if(n.getInputWeights() != null){
			Weight[] neuronWeights = n.getInputWeights();
			
			for (int i = 0; i < neuronWeights.length; i++){
				weights.add(neuronWeights[i].getWeightValue().getClone());
			}
		}
		
	}


	public Vector getWeights() {
		return weights;
	}


	public void setWeights(Vector weights) {
		throw new IllegalArgumentException("This operation is not supported for this visitor.");
	}
	
	

}
