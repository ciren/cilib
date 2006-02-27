/*
 * ParticleAgent.java
 * 
 * Created on Jul 24, 2004
 *
 *
 * Copyright (C) 2004 - CIRG@UP 
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
 */
package net.sourceforge.cilib.games.agents;

import net.sourceforge.cilib.coevolution.*;
import net.sourceforge.cilib.games.*;
import net.sourceforge.cilib.games.states.*;

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
