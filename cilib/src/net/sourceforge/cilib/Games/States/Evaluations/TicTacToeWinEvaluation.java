/*
 * Created on May 31, 2004
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

import net.sourceforge.cilib.Games.States.*;

/**
 * @author Vangos
 *
 * This evaluation concrete class determines if one has won in Tic-Tac-Toe
 */

public class TicTacToeWinEvaluation extends TicTacToeEvaluation{
	public TicTacToeWinEvaluation(int totalPlayers_)
	{super(totalPlayers_);}

	//Returns a clone of the object	
	public Evaluations Clone()
	{ return new TicTacToeWinEvaluation(GetTotalPlayer()); }
	
	//Evaluates in terms of a winning state
	public void Evaluate(State state_)
	{
		for (int i=0; i<totalPlayers; i++)
			evaluation[i] = 0.0;
		
		int side = state_.GetOtherSize();
		int[] template = new int[side*side];
		
		for (int i=0; i<side*side; i++)
			template[i] = ( state_.GetPeice(i)).GetPlayer();
			
		CheckPattern(template,side,2);
	}
	
	//Assigns the score to the players competing
	protected void AssignPoints(int[] points,int side,int divide)
	{
		for (int j=1; j<totalPlayers+1; j++)
		{
			if (points[j]==side)
				evaluation[j-1] = 1;	
		}
	}
	
	//Returns the evaluation of the state
	public double[] GetEvaluation()
	{ return evaluation; }
}
