/*
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
 
package net.sourceforge.cilib.Games.States.Evaluations;

/**
 * @author Vangos
 *
 * An abstratct class that is used to evaluate a TicTacToe State
 */

public abstract class TicTacToeEvaluation extends Evaluations{
	public TicTacToeEvaluation(int totalPlayers_)
	{super(totalPlayers_);}
	
	//Checks all combinations of the board state
	protected void CheckPattern(int[] template,int side,int choice)
	{
		if ((choice == 0)||(choice == 2))
		{
			for (int i=0; i<side; i++)
			{
				int[] points = new int[totalPlayers+1];
				for (int j=i; j<side*side; j+=side)
					points[template[j]]++;
				AssignPoints(points,side,2);
			}
		
			for (int i=0; i<side*side; i+=side)
			{
				int[] points = new int[totalPlayers+1];
				for (int j=i; j-side!=i; j++)
					points[template[j]]++;
				AssignPoints(points,side,2);
			}
		}
		
		if ((choice == 1)||(choice == 2))
		{
			for (int i=0; i<2; i++)
			{
				int[] points = new int[totalPlayers+1];
				switch(i)
				{
					case 0:
					for (int j=0; j<side*side; j+=(side+1))
					{
						points[template[j]]++;
						AssignPoints(points,side,1);
					}
					break;
					case 1:
					for (int j=side-1; j<=side*(side-1); j+=(side-1))
					{
						points[template[j]]++;
						AssignPoints(points,side,1);
					}
					break;
				}
			}
		}
	}
	  
	//Works together with Evaluate and awards points to states
	protected abstract void AssignPoints(int[] points,int side,int divide);
}
