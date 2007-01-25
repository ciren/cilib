/*
 * ParticleAdapter.java
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

import java.util.Random;

import net.sourceforge.cilib.coevolution.inputmasks.InputMask;
import net.sourceforge.cilib.games.states.State;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;

/**
 * @author Vangos
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public abstract class ParticleAdapter {
	public ParticleAdapter() {
		bestscore = -1.0;
	}
	
	protected void RandomizeValues(int range)
	{
		Random ran = new MersenneTwister();
		for (int i=0; i<weights.length; i++)
		{
			weights[i] = (ran.nextInt(range*1000)*0.001)-(range/2.0);
			bestweights[i] = (ran.nextInt(range*1000)*0.001)-(range/2.0);
			velocity[i] = (ran.nextInt(range*1000)*0.001)-(range/2.0);
		}
	}
	
	public double FindPersonalBest(double score)
	{
		if (score > bestscore)
		{
			bestscore = score;
			for (int i=0; i<weights.length; i++)
				bestweights[i] = weights[i];
		}
		return bestscore;
	}
	
	public void UpdateParticle(ParticleAdapter gb)
	{
		Random ran = new MersenneTwister();
		double r1 = (ran.nextInt(2*1000)*0.001)-(2/2.0);
		double r2 = (ran.nextInt(2*1000)*0.001)-(2/2.0);
		
		for (int i=0; i<velocity.length; i++)
		{
			velocity[i] = velocity[i] + r1*(bestweights[i] - weights[i]) 
				+ r2*(gb.GetBestWeights()[i] - gb.GetWeights()[i]);
			
			weights[i] = weights[i] + velocity[i];
		}
		
		
	}
	
	public void SetInputMask(InputMask inputmask_)
	{ inputmask = inputmask_; }
	
	public void SetScore(int score_)
	{ currentscore = score_; }
	
	public void IncrementScore(int score_)
	{ currentscore += score_; }
	
	public double GetScore()
	{ return currentscore; }
	
	public double GetBestScore()
	{ return bestscore; }
	
	public double[] GetWeights()
	{ return weights; }
	
	public double[] GetBestWeights()
	{ return bestweights; }
	
	public abstract double GetEvaluation(State state_);
	public abstract double GetBestEvaluation(State state_);
	
	protected double[] weights;
	protected double[] bestweights;
	protected double[] velocity;
	double bestscore;
	double currentscore;
	protected InputMask inputmask;
}
