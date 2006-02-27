/*
 * Created on Aug 10, 2004
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
public class NNEvangelosParticle extends ParticleAdapter{
	public NNEvangelosParticle(int input_,int hidden_,int output_)
	{
		super();
		weights = new double[((input_+1)*hidden_)+((hidden_+1)*output_)];
		bestweights = new double[((input_+1)*hidden_)+((hidden_+1)*output_)];
		velocity = new double[((input_+1)*hidden_)+((hidden_+1)*output_)];
		
		RandomizeValues(2);
		
		particle = new NeuralNetwork(input_,hidden_,output_);
		inputmask = null;
	}
	
	public double GetEvaluation(State state_)
	{
		double[] input = inputmask.Mask(state_);
		double[] output;
		output = particle.GetNetworkOutput(input,weights);
		return output[0];
	}
	
	public double GetBestEvaluation(State state_)
	{
		double[] input = inputmask.Mask(state_);
		double[] output;
		output = particle.GetNetworkOutput(input,bestweights);
		return output[0];
	}
	
	private NeuralNetwork particle;
}
