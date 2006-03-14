/*
 * TicTacToeState.java
 * 
 * Created on Apr 12, 2004
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
 
package net.sourceforge.cilib.games.states;

import net.sourceforge.cilib.games.gametokens.*;

/**
 * @author Vangos
 *
 * Representation of a simple NxN TicTacToe game state
 */

public class TicTacToeState extends State 
{
	//Constructs a board given a board size
	public TicTacToeState(int side_) 
	{
		side = side_;
		arraySize = side * side;
		gameState = new Token[arraySize];
		for (int i = 0; i < arraySize; i++)
			gameState[i] = new SimpleToken(0);
	}

	//Returns a clone of the object
	public State Clone()
	{
		TicTacToeState clone = new TicTacToeState(side);
		for (int i=0; i<arraySize; i++)
			clone.ExecuteAction(new Action(gameState[i],i));
		return clone;
	}

	//Returns the size of the board side 
	public int GetOtherSize() 
	{ return side; }

	//Displays the game state on the screen
	public void Print() 
	{
		for (int i = 0; i < arraySize; i++) {
			if (i % side == 0)
				System.out.println();
			System.out.print(gameState[i].GetPlayer());
		}
		System.out.println();
		System.out.println(score);
	}
	
	//The size of the board side
	 protected int side;
}
