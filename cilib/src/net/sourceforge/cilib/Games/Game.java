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
 
package net.sourceforge.cilib.Games;

import net.sourceforge.cilib.Games.States.*;
import net.sourceforge.cilib.Games.Agents.*;
import net.sourceforge.cilib.Games.GameTokens.*;

/**
 * @author Vangos
 *
 * This object is an abstract representation of a game
 */

public abstract class Game 
{
	//Returns an array of points for all players competing in the game
	public double[] GetScores()
	{ return score;}
	
	//Returns the current state of the game
	public State GetState()
	{ return currentState; }
	
	//Returns a value indicating the number of turns the game required to end
	public int GetCounter()
	{ return counter; }
	
	//Executes an action on the current state
	protected void ExecuteAction(Action action_)
	{ currentState.ExecuteAction(action_); }
	
	//Returns the next player whose turn it is
	protected int NextPlayer(int currentPlayer_,int totalPlayers_)
	{
		if (++currentPlayer_ == totalPlayers_)
			currentPlayer_ = 0;
		return currentPlayer_;
	}
	
	//Determines who the winner is from the assigned scores
	protected int Winner()
	{
		for (int i=0; i<score.length; i++)
			score[i] = 0;
		AssignScore();
		
		//System.out.println(score[0]+ " " + score[1]);
		
		int winner = 1;
		double winnerscore = score[0];
		for (int i=1; i<score.length; i++)
		{
			if (score[i] > winnerscore)
			{
				winner = i+1;
				winnerscore = score[i];
			}
			else if (score[i] == winnerscore)
				winner = 0;
		}
		return winner;
	}
	
	//This operation plays the game in an abstract manner
	public int PlayGame(Agent[] evaluation_,int startPlayer_,boolean print_)
	{
		ResetState();
		
		int currentPlayer = startPlayer_-2;
		int totalPlayers = evaluation_.length;
		
		score = new double[evaluation_.length];
		
		while (!GameOver())
		{
			counter++;
			currentPlayer = NextPlayer(currentPlayer,totalPlayers);
			State[] possibleStates = NextStates(currentPlayer+1);
			
			if (possibleStates.length > 0)
				currentState = GetBestState(evaluation_[currentPlayer],possibleStates);
				
			if (print_)
				currentState.Print();
		}
		return Winner();	
	}
	
	//Sets the start state appropriatly depending on the game played
	protected abstract void InitialState();
	
	//Sets the start state of the game
	public abstract void SetStartState(State state_);
	
	//Makes the current state the start state of the game
	public abstract void ResetState();
		
	//Specify the rules that determine if a game has ended or not
	protected abstract boolean GameOver();
	
	//Returns the best state given an evaluiataion and array of states
	protected abstract State GetBestState(Agent evaluate_,State[] stateArray_);
	
	//Assigns a score to the players given the current game state
	protected abstract void AssignScore();
	
	//Returns the next states for a specific player
	public abstract State[] NextStates(int player_);
	
	//Store the current state of the game
	protected State currentState;
	
	//Stoere the start state of the game
	protected State startState;
	
	//An array to assign player scores
	protected double[] score;
	
	//A counter to keep track of the number of turns required to end the game
	protected int counter;
}
