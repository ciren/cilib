/*
 * Created on Aug 5, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package net.sourceforge.cilib.Coevolution;

import java.util.Random;

import net.sourceforge.cilib.Coevolution.InputMasks.InputMask;
import net.sourceforge.cilib.Games.States.State;
import net.sourceforge.cilib.Random.MersenneTwister;

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
