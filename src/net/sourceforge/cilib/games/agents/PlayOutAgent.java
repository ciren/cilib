/*
 * PlayOutAgent.java
 * 
 * Created on Apr 20, 2004
 *
 * Copyright (C) 2003 - 2006 
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

import net.sourceforge.cilib.games.*;
import net.sourceforge.cilib.games.states.*;
import net.sourceforge.cilib.math.random.*;

/**
 * @author Vangos
 *
 * This object is an abstract representation of a play out evaluation
 */

public abstract class PlayOutAgent extends Agent
{
	public PlayOutAgent(Game game_)
	{
		super(game_);
		samples = 10;
		theGame.SetAgent(new RandomAgent(new MersenneTwister()),1);
		theGame.SetAgent(new RandomAgent(new MersenneTwister()),2);
	}
	
	public void SetSamples(int samples_)
	{ samples = samples_; }
	
	//Returns the state with the highest fitness values
	public State ReturnBest(State[] stateArray_)
	{ 
		double[] scores = GetScores(stateArray_);
		return stateArray_[MaxIndex(scores)];
	}
	
	//Returns the scores for each state
	protected abstract double[] GetScores(State[] stateArray_);
	
	//The number of samples
	protected int samples;
}
