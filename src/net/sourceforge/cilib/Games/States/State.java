/*
 * Created on Mar 2, 2004
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
 
package net.sourceforge.cilib.Games.States;

import net.sourceforge.cilib.Games.GameTokens.*;

/**
 * @author Vangos
 *
 * This object is an abstract representation of a game state
 */

public abstract class State 
{
	//Get the size of the array that stores Tokens
	public int GetSize()
	{ return arraySize; }
	
	//Returns a token that belongs to the array
	public Token GetPeice(int position_)
	{ return gameState[position_]; }
	
	//Returns all the tokens of the array
	public Token[] GetGameState()
	{ return gameState; }	
	
	//Places a new token in the array
	public void ExecuteAction(Action action_)
	{ gameState[action_.GetPosition()] = action_.GetGameToken(); }
	
	public void SetCurrentPlayer(int currentPlayer_)
	{ currentPlayer = currentPlayer_; }
	
	public int GetCurrentPlayer()
	{ return currentPlayer; }
	
	public void SetScore(double score_)
	{ score = score_; }
	
	public double GetScore()
	{ return score; }
	
	//Returns a clone of the object
	public abstract State Clone();
	
	//Returns a alternative size of the array
	public abstract int GetOtherSize();
	
	//Outputs the state on the screen
	public abstract void Print();
	
	//An array of tokens that represents the game
	protected Token[] gameState;
	
	//The size of the array of tokens that represents the game state
	protected int arraySize;
	
	public int currentPlayer;
	
	protected double score;
}
