package net.sourceforge.cilib.neuralnetwork.generic;

import java.util.ArrayList;

import net.sourceforge.cilib.neuralnetwork.foundation.NNPattern;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.NeuronConfig;
import net.sourceforge.cilib.type.types.MixedVector;
import net.sourceforge.cilib.type.types.Type;

public class LayeredGenericTopology extends GenericTopology {
	
	
	/*
	 * This class uses a "top to bottom, left to right" way of 
	 * evaluating neuron output.
	 * 
	 */
	
	
	public LayeredGenericTopology() {
		super();
	
	}
	
	public void initialize(){
		super.initialize();
	}
	
	public MixedVector evaluate(NNPattern p){
		
		MixedVector output = new MixedVector();
		
		for (int layer = 0; layer < layerList.size(); layer++){
			
			ArrayList<NeuronConfig> neuronList = layerList.get(layer);
			for (int i = 0; i < neuronList.size(); i++){
				
				NeuronConfig neuron = neuronList.get(i);
				Type result = neuron.computeOutput(neuron, p);
				
				//keep track of previous outputs.
				neuron.setTminus1Output(neuron.getCurrentOutput());
				neuron.setCurrentOutput(result);
				
				if (neuron.isOutputNeuron())
					output.add(result);
			}//end for i
			
		}
		
		return output;
		
	}
	
}
