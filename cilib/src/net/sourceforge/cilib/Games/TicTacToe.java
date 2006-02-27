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
 
package net.sourceforge.cilib.Games;

import net.sourceforge.cilib.Games.States.*;
import net.sourceforge.cilib.Games.GameTokens.*;
import net.sourceforge.cilib.Games.Agents.*;

/**
 * @author Vangos
 *
 * This object represents a simple NxN Tic Tac Toe game
 */

public class TicTacToe extends Deterministic
{
	public TicTacToe(int d)
	{ 
		startState = new TicTacToeState(d);
		currentState = new TicTacToeState(d);
		InitialState();
	}
	
	//Sets the start state to that that the game usually starts with
	protected void InitialState()
	{ 
		for (int i=0; i<startState.GetSize(); i++)
			(startState.GetGameState())[i] = new SimpleToken(0);
	}
	
	//Sets the start state to the parameter state
	public void SetStartState(State state_)
	{
		for (int i=0; i<startState.GetSize(); i++)
			(startState.GetGameState())[i] = new SimpleToken((state_.GetGameState())[i]);
	}
	
	//Sets the current state to that of the start state
	public void ResetState()
	{
		for (int i=0; i<currentState.GetSize(); i++)
			(currentState.GetGameState())[i] = new SimpleToken((startState.GetGameState())[i]);
		counter = 0;	
	}
	
	//Returns the best state given an evaluiataion and array of states
	protected State GetBestState(Agent evaluate_,State[] stateArray_)
	{ return new TicTacToeState(evaluate_.ReturnMax(stateArray_)); }
	
	//Determines if the game is over
	protected boolean GameOver()
	{
		if (Winner() != 0)
			return true;
			
		for (int i=0; i<currentState.GetSize(); i++)
		{
			if (currentState.GetPeice(i).GetPlayer()==0)
				return false;	
		}
		
		return true;
	}
	
	//Assigns the score to the players competing
	protected void AssignScore()
	{
		int side = currentState.GetOtherSize();
		int[] template = new int[side*side];
		
		for (int i=0; i<side*side; i++)
			template[i] = (currentState.GetPeice(i)).GetPlayer();
			
		AssignScore2(template,side);
	}
	
	//Assigns the score to the players competing
	private void AssignScore2(int[] template,int side)
	{
		for (int i=0; i<side; i++)
		{
			int[] points = new int[score.length+1];
			for (int j=i; j<side*side; j+=side)
				points[template[j]]++;
			AssignScore3(points,side);
		}
		
		for (int i=0; i<side*side; i+=side)
		{
			int[] points = new int[score.length+1];
			for (int j=i; j-side!=i; j++)
				points[template[j]]++;
			AssignScore3(points,side);
		}
		
		for (int i=0; i<2; i++)
		{
			int[] points = new int[score.length+1];
			switch(i)
			{
				case 0:
				for (int j=0; j<side*side; j+=(side+1))
				{
					points[template[j]]++;
					AssignScore3(points,side);
				}
				break;
				case 1:
				for (int j=side-1; j<=side*(side-1); j+=(side-1))
				{
					points[template[j]]++;
					AssignScore3(points,side);
				}
				break;
			}
		}
	}
	
	//Assigns the score to the players competing
	private void AssignScore3(int[] points,int side)
	{
		for (int j=1; j<score.length+1; j++)
		{
			if (points[j]==side)
				score[j-1] = 1;	
		}
	}
	
	//Returns an array of states representing possible moves for a player
	public State[] NextStates(int player)
	{
		int side = currentState.GetOtherSize();
		int empty = 0;
		
		for (int i=0; i<side*side; i++)
		{
			if (currentState.GetPeice(i).GetPlayer()==0)
				empty++;
		}
		
		State[] stateArray = new State[empty];
		
		empty = 0;
		for (int i=0; i<side*side; i++)
		{
			if (currentState.GetPeice(i).GetPlayer()==0)
			{
				stateArray[empty] = new TicTacToe3DState(currentState);
				stateArray[empty++].ExecuteAction(new Action(new SimpleToken(player),i));
			}
		}
		
		return stateArray;		
	}
}
