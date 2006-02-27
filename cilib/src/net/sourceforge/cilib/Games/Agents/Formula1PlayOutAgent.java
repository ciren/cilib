/*
 * Created on Apr 12, 2004
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
 * The state evaluation is done through a play out where the following formula is used:
 * wins * (0.6) + draws * (0.4) + averageMoves
 */

public class Formula1PlayOutAgent extends PlayOutAgent
{
	public Formula1PlayOutAgent(int currentPlayer_,int totalPlayers_,int samples_,Game game_,Agent evaluate_)
	{ super(currentPlayer_,totalPlayers_,samples_,game_,evaluate_); }
	
	//Returns an array of fitness of the states using a special formula
	protected double[] GetScores(State[] stateArray_)
	{
		double[] scores = new double[stateArray_.length];
		for (int i=0; i<stateArray_.length; i++)
		{
			theGame.SetStartState(stateArray_[i]);
			Information info = new Information(samples,theGame,theEvaluations);
			info.PlayGames(currentPlayer,false);
			
			int [] allScores = info.GetWins();
			int draws = 0;
			for (int j=0; j<allScores.length; j++)
				draws += allScores[j];
			draws = samples - draws;
			
			scores[i] = (allScores[currentPlayer-1]*0.6) + (draws*0.4) + (theGame.GetState().GetSize()-info.GetAverageMoves());
		}	
		return scores;
	}
}
