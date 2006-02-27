/*
 * NNEvangelosParticle.java
 * 
 * Created on 2005/08/17
 *
 * Copyright (C) 2003, 2005 - CIRG@UP 
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science 
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA 
 * 
 */
package net.sourceforge.cilib.coevolution;

import net.sourceforge.cilib.games.states.*;

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
		
		//particle = new NeuralNetwork(input_,hidden_,output_);
		inputmask = null;
	}
	
	public double GetEvaluation(State state_)
	{
		//double[] input = inputmask.Mask(state_);
		//double[] output;
		//output = particle.GetNetworkOutput(input,weights);
		//return output[0];
		return 0;
	}
	
	public double GetBestEvaluation(State state_)
	{
		//double[] input = inputmask.Mask(state_);
		//double[] output;
		//output = particle.GetNetworkOutput(input,bestweights);
		//return output[0];
		return 0;
	}
	
	//private NeuralNetwork particle;
}
