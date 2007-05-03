/*
 * Created on 2004/11/13
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.neuralnetwork.foundation;



/**
 * @author stefanv
 *
 * NeuralNetworkData serves as the base interface for any "typical" interaction with a data source.  All NN data
 * should be presented to the NN via this interface.
 * 
 */
public interface NeuralNetworkData extends Initializable{
	
	public int getCandidateSetSize();
	
	public int getTrainingSetSize();
	
	public int getGeneralisationSetSize();
	
	public int getValidationSetSize();
	
	public NeuralNetworkDataIterator getTrainingSetIterator();
	
	public NeuralNetworkDataIterator getGeneralisationSetIterator();
	
	public NeuralNetworkDataIterator getValidationSetIterator();
	
	public NeuralNetworkDataIterator getCandidateSetIterator();
	
	public void activeLearningUpdate(Object input);
	
	public void shuffleTrainingSet();
	
}
