/*
 * Created on 2004/03/31
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

import net.sourceforge.cilib.Games.Game;
import net.sourceforge.cilib.Games.States.State;

/**
 * @author Vangos
 *
 * This object is an abstract representation of how to evaluate a State
 */

public abstract class Agent
{
	public Agent(Game game_)
	{ 
		theGame = game_; 
		totalPlayers =theGame.GetTotalPlayers();
		player = -1;
	}
	
	public Game GetGame()
	{ return theGame; }
	
	public void SetPlayer(int player_)
	{ player = player_; }
	
	//Returns the max value from an array
	protected int MaxIndex(double[] minmax)
	{
		double max = -999999;
		int index = 0;
		for (int i=0; i<minmax.length; i++)
		{
			//System.out.print(minmax[i] + " ");
			if (minmax[i] > max)
			{
				max = minmax[i];
				index = i;
			}
		}
		//System.out.println(index + " is max!");
		return index;
	}
	
	//Returns the min value from an array
	protected int MinIndex(double[] minmax)
	{
		double min = 999999;
		int index = 0;
		for (int i=0; i<minmax.length; i++)
		{
			//System.out.print(minmax[i] + " ");
			if (minmax[i] < min)
			{
				min = minmax[i];
				index = i;
			}
		}
		//System.out.println(index + " is min!");
		return index;
	}
	
	//Returns the state with the highest fitness values
	public abstract State ReturnBest(State[] stateArray_);
		
	protected Game theGame;
	protected int player;
	protected int totalPlayers;
}
