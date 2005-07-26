/*
 * Created on Aug 10, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.NeuralNetwork;


/**
 * @author Vangos
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class NeuralNetwork {
	public NeuralNetwork(int input_,int hidden_,int output_)
	{
		inputSize = input_;
		hiddenSize = hidden_;
		outputSize = output_;
	}

	private double GetNeuronOutput(double[] inputArray,double[] weightArray)
	{
		double summation = 0;
		for (int i = 0; i < inputArray.length; i++) //For all the inputs.
			summation += inputArray[i] * weightArray[i];  //Add weight * input.
	
		summation = 1.0/(1.0+Math.exp(-1.0*summation)); //Calculate activation.(Sigmoid function).
		return summation;
	}
	
	private double[] GetHiddenWeights(int posi,double[] weights)
	{
		double[] wei = new double[inputSize+1];
		System.arraycopy(weights, posi*(inputSize+1), wei, 0, inputSize+1);	
		return wei;
	}
	
	private double[] GetOutputWeights(int posi,double[] weights)
	{
		double[] wei = new double[inputSize+1];
		System.arraycopy(weights, (posi*(hiddenSize+1))+((inputSize+1)*hiddenSize), wei, 0, hiddenSize+1);	
		return wei;
	}
	
	public double[] GetNetworkOutput(double[] inputArray,double[] weights)
	{
		double[] returnOutput = new double[outputSize];
		double[] hiddenOutput = new double[hiddenSize+1];
		
		for (int i=0; i < hiddenSize; i++) //for all hidden units.
			hiddenOutput[i] = GetNeuronOutput(inputArray,GetHiddenWeights(i,weights)); //Calculate activation.	
		hiddenOutput[hiddenSize] = -1;
		
		for (int i=0; i < outputSize; i++) //For output units.+
			returnOutput[i] = GetNeuronOutput(hiddenOutput,GetOutputWeights(i,weights)); //Calculate activation.
		
		return returnOutput;	
	}

	private int inputSize,hiddenSize,outputSize;
}
