/*
 * Formula2PlayOutAgent.java
 * 
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
 
package net.sourceforge.cilib.games.agents;

import net.sourceforge.cilib.games.*;
import net.sourceforge.cilib.games.states.*;

/**
 * @author Vangos
 *
 * The state evaluation is done through a play out where the following formula is used:
 */

public class Formula2PlayOutAgent extends PlayOutAgent
{
	public Formula2PlayOutAgent(Game game_)
	{ super(game_); }
	
	//Returns an array of fitness of the states using a special formula
	protected double[] GetScores(State[] stateArray_)
	{
		double[] scores = new double[stateArray_.length];
		for (int i=0; i<stateArray_.length; i++)
		{
			theGame.SetStartState(stateArray_[i]);
			Information info = new Information(theGame);
			info.SetTotalGames(samples);
			info.PlayGames(false,false);
			
			int [] allScores = info.GetWins();
			int draws = 0;
			for (int j=0; j<allScores.length; j++)
				draws += allScores[j];
			draws = samples - draws;
			int loss = samples - draws - allScores[player-1];
		
			double player1 = (1.0*loss) + (2.0*draws) + (3.0*allScores[player-1]);
			scores[i] = (1.0-(3.0-player1)/2.0)*100 + (theGame.GetState().GetSize()-info.GetAverageMoves());
		}	
		return scores;
	}
}
