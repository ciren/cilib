/*
 * TicTacToe3DWinEvaluation.java
 * 
 * Created on Jun 3, 2004
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
 
package net.sourceforge.cilib.games.states.evaluations;

import net.sourceforge.cilib.games.states.*;

/**
 * @author Vangos
 *
 * This evaluation concrete class determines if one has won in Tic-Tac-Toe 3D
 */

public class TicTacToe3DWinEvaluation extends TicTacToeEvaluation{
	public TicTacToe3DWinEvaluation(int totalPlayers_)
	{super(totalPlayers_);}

	//Returns a clone of the object	
	public Evaluations Clone()
	{ return new TicTacToe3DWinEvaluation(GetTotalPlayer()); }
	
	//Evaluates in terms of a winning state
	public void Evaluate(State state_)
	{
		for (int i=0; i<totalPlayers; i++)
			evaluation[i] = 0.0;
		
		int side = state_.GetOtherSize();
		int[] template = new int[side*side];
		
		int counter;

		for (int i=0; i<side; i++)
		{
			counter = 0;
			for (int j=i*(side*side); j<(i+1)*(side*side); j++)
				template[counter++] = (state_.GetPeice(j)).GetPlayer();
			CheckPattern(template,side,2);
		}
		
		for (int i = 0; i < side; i++) 
		{
			counter = 0;
			for (int j = i * side; j < (side * side * side); j += side * side) 
				for (int k = 0; k < 3; k++)
					template[counter++] = (state_.GetPeice(j + k)).GetPlayer();
			CheckPattern(template, side,2);
		}
		
		for (int i = 0; i < side; i++) 
		{
			counter = 0;
			for (int k = i; k < (side*side*side); k+=side)
				template[counter++] = (state_.GetPeice(k)).GetPlayer();
			CheckPattern(template, side,2);
		}
		
		for (int i=0; i<side ; i++)
		{
			counter = 0;
			for (int j=i*(side+1);j<(side*side*side);j+=(side*side))
				template[counter++] = (state_.GetPeice(j)).GetPlayer();
			CheckPattern(template, side,1);
		}
			
		for (int i=1; i<=side ; i++)
		{
			counter = 0;
			for (int j=i*(side-1);j<(side*side*side);j+=(side*side))
				template[counter++] = (state_.GetPeice(j)).GetPlayer();
			CheckPattern(template, side,1);
		}
	}
	
	//Assigns the score to the players competing
	protected void AssignPoints(int[] points,int side,int divide)
	{
		for (int j=1; j<totalPlayers+1; j++)
		{
			if (points[j]==side)
				evaluation[j-1]+= 1.0/divide;	
		}
	}
	
	//Returns the evaluation of the state
	public double[] GetEvaluation()
	{ return evaluation; }
}
