/*
 * Created on May 12, 2004
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
 
package net.sourceforge.cilib.Games.States.Evaluations;

/**
 * @author Vangos
 *
 * An evaluation for nodes in a game tree
 */

public class TicTacToeNodeEvaluation extends TicTacToeWinEvaluation{
	public TicTacToeNodeEvaluation(int totalPlayers_)
	{super(totalPlayers_);}

	//Returns a clone of the object	
	public Evaluations Clone()
	{ return new TicTacToeNodeEvaluation(GetTotalPlayer()); }
	
	//Assigns points to the players competing
	protected void AssignPoints(int[] points,int side,int divide)
	{
		for (int j=1; j<totalPlayers+1; j++)
		{
			if (points[j] == side)
				evaluation[j-1] += 1000;
			if (points[j]+points[0]==side)
				evaluation[j-1] += 1;	
		}
	}
	
	//Returns the evaluation of the state
	public double[] GetEvaluation()
	{
		double[] evaluation2 = new double[totalPlayers];
		for (int i=0; i<totalPlayers; i++)
			evaluation2[i] = evaluation[i];
		
		for (int i=0; i<totalPlayers; i++) 
			for (int j=0; j<totalPlayers; j++)
			{
				if (i != j)
					evaluation2[i] -= evaluation[j];
			}
		return evaluation2;
	}
}
