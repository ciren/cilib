/*
 * Created on Apr 20, 2004
 *
 * Copyright (C) 2003, 2004 - CIRG@UP 
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
 
package net.sourceforge.cilib.Games.Agents;

import net.sourceforge.cilib.Games.*;
import net.sourceforge.cilib.Games.States.*;

/**
 * @author Vangos
 *
 * This object is an abstract representation of a play out evaluation
 */

public abstract class PlayOutAgent extends Agent
{
	public PlayOutAgent(int currentPlayer_,int totalPlayers_,int samples_,Game game_,Agent evaluate_)
	{
		super(null); 
		currentPlayer = currentPlayer_;
		samples = samples_;
		theEvaluations = new Agent[totalPlayers_];
		for (int i=0; i<totalPlayers_; i++)
			theEvaluations[i] = evaluate_;
		theGame = game_;
	}
	
	//Returns the state with the highest fitness values
	public State ReturnMax(State[] stateArray_)
	{ 
		double[] scores = GetScores(stateArray_);
		
		double maximum = scores[0];
		int choice = 0;
		for (int i=0; i<stateArray_.length; i++)
		{
			if (scores[i] > maximum)
			{
				maximum = scores[i];
				choice = i;
			}
		}
		return stateArray_[choice];
	}
	
	//Returns the state with the lowest fitness values
	public State ReturnMin(State[] stateArray_)
	{ 
		double[] scores = GetScores(stateArray_);
		
		double minimum = scores[0];
		int choice = 0;
		for (int i=1; i<stateArray_.length; i++)
		{
			if (scores[i] < minimum)
			{
				minimum = scores[i];
				choice = i;
			}
		}
		return stateArray_[choice];
	}
	
	//Returns the scores for each state
	protected abstract double[] GetScores(State[] stateArray_);
	
	//The number of samples
	protected int samples;
	
	//The current player using the evaluation function
	protected int currentPlayer;
	
	//The array of evaluators
	protected Agent[] theEvaluations;
	
	//The game on which the evaluator must act on
	protected Game theGame;
}
