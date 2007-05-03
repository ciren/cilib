package net.sourceforge.cilib.neuralnetwork.generic.topologyvisitors;

import net.sourceforge.cilib.neuralnetwork.generic.neuron.NeuronConfig;
import net.sourceforge.cilib.neuralnetwork.generic.Weight;
import net.sourceforge.cilib.type.types.Real;

public class FanInWeightInitialiser implements GenericTopologyVisitor {
	
	
	
	public FanInWeightInitialiser() {
		super();
	}

	public void visitNeuronConfig(NeuronConfig n) {
			
		//first initialise all neuron-to-neuron weights if they exist
		if (n.getInputWeights() != null){
			double fanInRange = 1.0 / Math.sqrt((double)n.getInputWeights().length);
			
			for (int i = 0; i < n.getInputWeights().length; i++){
								
				Weight w = n.getInputWeights()[i];
				//set weight value in range [-1/sqrt(fanIn), 1/sqrt(fanIn)]
				w.setWeightValue( new Real( (Math.random() * 2 * fanInRange ) - fanInRange  ));
			}
		}
		
		//also initialise pattern-to-neuron weight if they exist.
		//default = 1
		if (n.getPatternWeight() != null){
			n.getPatternWeight().setWeightValue(new Real(1.0));
		}

	}

}
