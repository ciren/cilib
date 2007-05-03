/*
 * Created on 2005/01/22
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.neuralnetwork.foundation;


/**
 * @author stefanv
 *
 * 
 */
public interface TrainingStrategy extends Initializable{
	
	public void invokeTrainer(Object args);
	
	public void preEpochActions(Object args);
	
	public void postEpochActions(Object args);
	
	public void setTopology(NeuralNetworkTopology topo);

}
