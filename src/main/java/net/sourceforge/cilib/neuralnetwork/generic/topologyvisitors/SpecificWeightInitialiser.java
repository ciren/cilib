/*
 * Created on 2005/08/10
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.neuralnetwork.generic.topologyvisitors;

import java.util.ArrayList;

import net.sourceforge.cilib.neuralnetwork.generic.Weight;
import net.sourceforge.cilib.neuralnetwork.generic.neuron.NeuronConfig;
import net.sourceforge.cilib.type.types.MixedVector;

/**
 * @author stefanv
 *
 */
public class SpecificWeightInitialiser implements GenericTopologyVisitor {
	
	ArrayList<Weight> weights = null;
		
	public SpecificWeightInitialiser() {
		super();
		this.weights = null;
	}
		
	
	public void visitNeuronConfig(NeuronConfig n) {
		
		if(n.getInputWeights() != null){
			
		   Weight[] tmp = new Weight[n.getInputWeights().length];
		   for (int i = 0; i < tmp.length; i++){
		   		tmp[i] = weights.remove(0);
		   }
		   n.setInputWeights(tmp);
		}

	}

	public void setWeights(MixedVector w) {
		
		weights = new ArrayList<Weight>();
		for (int i = 0; i < w.size(); i++){
			weights.add(new Weight(w.get(i)));
		}
	}
	
	public boolean isEmpty(){
		return weights.size() == 0;
	}

}
