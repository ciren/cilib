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
 * The state evaluation is done through a play out where only wins are considered
 */

public class SimplePlayOutAgent extends PlayOutAgent
{
	public SimplePlayOutAgent(int cp,int tp,int s,Game g,Agent e)
	{ super(cp,tp,s,g,e); }
	
	//Returns an array of fitness of the states using only the expected wins
	protected double[] GetScores(State[] sa)
	{
		double[] scores = new double[sa.length];
		for (int i=0; i<sa.length; i++)
		{
			theGame.SetStartState(sa[i]);
			Information info = new Information(samples,theGame,theEvaluations);
			info.PlayGames(currentPlayer,false);
			scores[i] = (info.GetWins())[currentPlayer-1];
		}	
		return scores;
	}
}
