/*
 * Created on Aug 5, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
 
package net.sourceforge.cilib.Coevolution;

import net.sourceforge.cilib.NeuralNetwork.*;
import net.sourceforge.cilib.Games.States.*;

/**
 * @author Vangos
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class NNCliveParticle extends ParticleAdapter
{
	public NNCliveParticle(int input_,int hidden_,int output_)
	{
		outputSize = output_;
		particle = new NN(input_,hidden_,output_);
		weights = new double[(input_*hidden_)+(hidden_*output_)];	
		RandomizeValues(2);
		inputmask = null;
	}
	
	public double GetEvaluation(State state_)
	{
		double[] input = inputmask.Mask(state_);
		double[] output = new double[1];
		particle.getOutput(input,weights,output);
		return output[0];
	}
	
	public double GetBestEvaluation(State state_)
	{
		double[] input = inputmask.Mask(state_);
		double[] output = new double[1];
		particle.getOutput(input,weights,output);
		return output[0];
	}
	
	private NN particle;
	@SuppressWarnings("unused")
	private int outputSize;
}
