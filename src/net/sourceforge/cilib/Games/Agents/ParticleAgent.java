/*
 * Created on Aug 5, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
 
package net.sourceforge.cilib.Games.Agents;

import net.sourceforge.cilib.Coevolution.*;
import net.sourceforge.cilib.Games.*;
import net.sourceforge.cilib.Games.States.*;

/**
 * @author Vangos
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ParticleAgent extends Agent{
	public ParticleAgent()
	{ 
		super(new NullGame()); 
		adapter = null;
		evaluationSwitch = 0;
	}
	
	public void SetSwitch(int switch_)
	{ evaluationSwitch = switch_;}
	
	public void SetParticleAdapter(ParticleAdapter adapter_)
	{ adapter = adapter_; }
	
	public ParticleAdapter GetParticleAdapter()
	{ return adapter; }
	
	public State ReturnBest(State[] stateArray_)
	{
		double[] evaluations = new double[stateArray_.length];
		for (int i=0; i<stateArray_.length; i++)
		{
			if (evaluationSwitch == 0)
				evaluations[i] = adapter.GetEvaluation(stateArray_[i]); 
			else if (evaluationSwitch == 1)
				evaluations[i] = adapter.GetBestEvaluation(stateArray_[i]); 
		}
		
		return stateArray_[MaxIndex(evaluations)];
	}
	
	private ParticleAdapter adapter;
	private int evaluationSwitch;
}
