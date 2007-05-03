/*
 * Created on 2005/04/24
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.neuralnetwork.generic.topologyvisitors;


import net.sourceforge.cilib.neuralnetwork.generic.neuron.NeuronConfig;
import net.sourceforge.cilib.neuralnetwork.generic.Weight;
import net.sourceforge.cilib.type.types.Real;

/**
 * @author stefanv
 *
 */
public class RandomWeightInitialiser implements GenericTopologyVisitor {

	//the range over which values may be initiated. 
	//offset is center from  of the distribution
	//i.e. range = 2, offset 0 means range [-1,1]
	
	double range = 2;
	double offset = 0;
	
	
	public RandomWeightInitialiser() {
		this.range = 2;
		this.offset = 0;
	}
	
	
	
	public void visitNeuronConfig(NeuronConfig n) {
		
		//first initialise all neuron-to-neuron weights if they exist
		if (n.getInputWeights() != null){
			for (int i = 0; i < n.getInputWeights().length; i++){
				Weight w = n.getInputWeights()[i];
				w.setWeightValue( new Real( (Math.random() * range) - range/2.0 + offset));
			}
		}
		
		//also initialise pattern-to-neuron weight if they exist.
		//default = 1
		if (n.getPatternWeight() != null){
			n.getPatternWeight().setWeightValue(new Real(1.0));
		}

	}


	public double getOffset() {
		return offset;
	}



	public void setOffset(double offset) {
		this.offset = offset;
	}



	public double getRange() {
		return range;
	}



	public void setRange(double range) {
		this.range = range;
	}
	
	

}
