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
import net.sourceforge.cilib.Games.Chances.*;
import net.sourceforge.cilib.Games.GameTokens.*;
import net.sourceforge.cilib.Games.Agents.*;
import net.sourceforge.cilib.Random.*;

/**
 * @author Vangos
 *
 * This object represents a simple NxNxN probabilistic Tic Tac Toe game
 */

public class ProbTicTacToe3D extends Probabailistic {
	public ProbTicTacToe3D(int d) {
		startState = new TicTacToe3DState(d);
		currentState = new TicTacToe3DState(d);
		InitialState();

		chanceArray = new Chance[1];
		chanceArray[0] = new DiceChance(new Tausworthe(),currentState.GetOtherSize());
	}

	//Sets the start state to that that the game usually starts with
	protected void InitialState() {
		for (int i = 0; i < currentState.GetSize(); i++)
			 (startState.GetGameState())[i] = new SimpleToken(0);
	}

	//Sets the start state to the parameter state
	public void SetStartState(State state_) {
		for (int i = 0; i < startState.GetSize(); i++)
			(startState.GetGameState())[i] =
				new SimpleToken((state_.GetGameState())[i]);
	}

	//Sets the current state to that of the start state	
	public void ResetState() {
		for (int i = 0; i < currentState.GetSize(); i++)
			(currentState.GetGameState())[i] =
				new SimpleToken((startState.GetGameState())[i]);
		counter = 0;
	}
	
	//Returns the best state given an evaluiataion and array of states
	protected State GetBestState(Agent evaluate_,State[] stateArray_)
	{ return new TicTacToe3DState(evaluate_.ReturnMax(stateArray_)); }

	//Determines if the game is over
	protected boolean GameOver() {
		for (int i = 0; i < currentState.GetSize(); i++) {
			if (currentState.GetPeice(i).GetPlayer() == 0)
				return false;
		}
		return true;
	}

	//Assigns the score to the players competing
	protected void AssignScore() 
	{
		int side = currentState.GetOtherSize();
		int[] template = new int[side * side];
		int counter = 0;

		for (int i=0; i<side; i++)
		{
			counter = 0;
			for (int j=i*(side*side); j<(i+1)*(side*side); j++)
				template[counter++] = (currentState.GetPeice(j)).GetPlayer();
			AssignScore2(template,side,2);
		}
		
		for (int i = 0; i < side; i++) 
		{
			counter = 0;
			for (int j = i * side; j < (side * side * side); j += side * side) 
				for (int k = 0; k < 3; k++)
					template[counter++] = (currentState.GetPeice(j + k)).GetPlayer();
			AssignScore2(template, side,2);
		}
		
		for (int i = 0; i < side; i++) 
		{
			counter = 0;
			for (int k = i; k < (side*side*side); k+=side)
				template[counter++] = (currentState.GetPeice(k)).GetPlayer();
			AssignScore2(template, side,2);
		}
		
		for (int i=0; i<side ; i++)
		{
			counter = 0;
			for (int j=i*(side+1);j<(side*side*side);j+=(side*side))
				template[counter++] = (currentState.GetPeice(j)).GetPlayer();
			AssignScore2(template, side,1);
		}
			
		for (int i=1; i<=side ; i++)
		{
			counter = 0;
			for (int j=i*(side-1);j<(side*side*side);j+=(side*side))
				template[counter++] = (currentState.GetPeice(j)).GetPlayer();
			AssignScore2(template, side,1);
		}
	}

	//Assigns the score to the players competing
	private void AssignScore2(int[] template,int side,int choice)
	{
		if ((choice == 0)||(choice == 2))
		{
			for (int i=0; i<side; i++)
			{
				int[] points = new int[score.length+1];
				for (int j=i; j<side*side; j+=side)
					points[template[j]]++;
				AssignScore3(points,side,2);
			}
		
			for (int i=0; i<side*side; i+=side)
			{
				int[] points = new int[score.length+1];
				for (int j=i; j-side!=i; j++)
					points[template[j]]++;
				AssignScore3(points,side,2);
			}
		}
		
		if ((choice == 1)||(choice == 2))
		{
			for (int i=0; i<2; i++)
			{
				int[] points = new int[score.length+1];
				switch(i)
				{
					case 0:
					for (int j=0; j<side*side; j+=(side+1))
					{
						points[template[j]]++;
						AssignScore3(points,side,1);
					}
					break;
					case 1:
					for (int j=side-1; j<=side*(side-1); j+=(side-1))
					{
						points[template[j]]++;
						AssignScore3(points,side,1);
					}
					break;
				}
			}
		}
	}

	//	Assigns the score to the players competing
	private void AssignScore3(int[] points,int side,int divide)
	{
		for (int j=1; j<score.length+1; j++)
		{
			if (points[j]==side)
				score[j-1]+= 1.0/divide;	
		}
	}

	//Returns an array of states representing possible moves for a player
	public State[] NextStates(int player) {
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
				stateArray[empty] = new TicTacToe3DState(currentState);
				stateArray[empty++].ExecuteAction(
					new Action(new SimpleToken(player), i));
			}
		}

		return stateArray;
	}
}
