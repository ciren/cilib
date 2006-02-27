/*
 * ProbTicTacToe3DGame.java
 * 
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

package net.sourceforge.cilib.games;

//import net.sourceforge.cilib.Games.Agents.*;
import net.sourceforge.cilib.games.chances.*;
import net.sourceforge.cilib.games.gametokens.*;
import net.sourceforge.cilib.games.states.*;
import net.sourceforge.cilib.games.states.evaluations.*;
import net.sourceforge.cilib.math.random.*;

/**
 * @author Vangos
 *
 * This object represents a simple NxNxN probabilistic Tic Tac Toe game
 */

public class ProbTicTacToe3DGame extends Probabailistic {
	public ProbTicTacToe3DGame() {
		chanceArray = new Chance[1];
		TotalPlayerChange(2); 
		DimensionChange(3);
		theEvaluation = new TicTacToe3DNodeEvaluation(totalPlayers);
	}
	
	public ProbTicTacToe3DGame(Game game_)
	{ super(game_); }

	private void DimensionChange(int dimension_)
	{
		if (dimension_ < 3)
			dimension_ = 3;
		startState = new TicTacToe3DState(dimension_);
		currentState = new TicTacToe3DState(dimension_);
		InitialState();
		
		chanceArray[0] = new DiceChance(new Tausworthe(),currentState.GetOtherSize());
	}

	//Sets the start state to that that the game usually starts with
	protected void InitialState() {
		for (int i = 0; i < currentState.GetSize(); i++)
			 (startState.GetGameState())[i] = new SimpleToken(0);
	}

	//Sets the start state to the parameter state
	public void SetStartState(State state_) 
	{
		for (int i = 0; i < startState.GetSize(); i++)
			(startState.GetGameState())[i] =
				new SimpleToken((state_.GetGameState())[i]);
	}
	
	//Sets a dimension
	public void SetDimension(int dimension_)
	{ DimensionChange(dimension_); }

	//Sets the current state to that of the start state	
	public void ResetState() {
		for (int i = 0; i < currentState.GetSize(); i++)
			(currentState.GetGameState())[i] =
				new SimpleToken((startState.GetGameState())[i]);
		counter = 0;
	}
	
	//Returns the best state given an evaluiataion and array of states
	//protected State GetBestState(Agent evaluate_,State[] stateArray_)
	//{ return evaluate_.ReturnBest(stateArray_).Clone(); }

	//Determines if the game is over
	public boolean GameOver() {
		for (int i = 0; i < currentState.GetSize(); i++) {
			if (currentState.GetPeice(i).GetPlayer() == 0)
				return false;
		}
		return true;
	}

	//Assigns the score to the players competing
	protected void AssignScore() 
	{
		TicTacToe3DWinEvaluation win = new TicTacToe3DWinEvaluation(totalPlayers);
		win.Evaluate(currentState);
		score = win.GetEvaluation();
	}

	//Returns an array of states representing possible moves for a player
	public State[] NextStates(int player) 
	{
		int dice = (chanceArray[0]).NextChance();
		int side = currentState.GetOtherSize() * currentState.GetOtherSize();
		int empty = 0;

		for (int i = dice * side; i < (dice + 1) * side; i++) 
		{
			if (currentState.GetPeice(i).GetPlayer() == 0)
				empty++;
		}

		State[] stateArray = new State[empty];

		empty = 0;
		for (int i = dice * side; i < (dice + 1) * side; i++) 
		{
			if (currentState.GetPeice(i).GetPlayer() == 0) {
				stateArray[empty] = currentState.Clone();
				stateArray[empty].SetCurrentPlayer(player);
				stateArray[empty++].ExecuteAction(
					new Action(new SimpleToken(player), i));
			}
		}

		return stateArray;
	}
}
