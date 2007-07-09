/*
 * Created on 2004/11/13
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.neuralnetwork.foundation;


import net.sourceforge.cilib.type.types.container.MixedVector;

/**
 * @author stefanv
 *
 * 
 */
public interface NeuralNetworkTopology extends Initializable{

	public MixedVector evaluate(NNPattern p);
	
	public MixedVector getWeights();
	
	public void setWeights(MixedVector w);
				
}
