/*
 * Game.java
 * 
 * Created on Mar 2, 2004
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
 
package net.sourceforge.cilib.games;

import net.sourceforge.cilib.coevolution.ParticleAdapter;
import net.sourceforge.cilib.games.agents.Agent;
import net.sourceforge.cilib.games.agents.RandomAgent;
import net.sourceforge.cilib.games.gametokens.Action;
import net.sourceforge.cilib.games.states.State;
import net.sourceforge.cilib.games.states.evaluations.Evaluations;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;

/**
 * @author Vangos
 *
 * This object is an abstract representation of a game
 */

public abstract class Game
{
	public Game()
	{ startPlayer = 1; }
	
	public Game(Game game_)
	{
		startPlayer = game_.GetStartPlayer();
		startState = game_.startState.Clone();
		currentState = game_.currentState.Clone();
		totalPlayers = game_.GetTotalPlayers();
		adapter = game_.GetParticleAdapter();
		score = new double[totalPlayers];
		for (int i=0; i<totalPlayers; i++)
			score[i] = game_.GetScores()[i];
		counter = game_.GetCounter();
		theEvaluation = game_.GetEvaluation().Clone();
		
		theAgents = new Agent[totalPlayers];
		for (int i=0; i<totalPlayers; i++)
			theAgents[i] = null;
	}
	
	//Modifies the variables for a total player change
	protected void TotalPlayerChange(int totalPlayers_)
	{ 
		totalPlayers = totalPlayers_;
		score = new double[totalPlayers_]; 
		theAgents = new Agent[totalPlayers];
		for (int i=0; i<totalPlayers; i++)
			theAgents[i] = new RandomAgent(new MersenneTwister());
	}
	
	//Returns an array of points for all players competing in the game
	public double[] GetScores()
	{ return score;}
	
	//Returns the current state of the game
	public State GetState()
	{ return currentState; }
	
	//Returns a value indicating the number of turns the game required to end
	public int GetCounter()
	{ return counter; }
	
	public int GetTotalPlayers()
	{ return totalPlayers; }
	
	public int GetStartPlayer()
	{ return startPlayer; }
	
	public State GetCurrentState()
	{ return currentState; }
	
	public State GetStartState()
	{ return startState; }
	
	public Evaluations GetEvaluation()
	{ return theEvaluation; }
	
	public ParticleAdapter GetParticleAdapter()
	{ return adapter; }
		
	public void SetStartPlayer(int startPlayer_)
	{ startPlayer = startPlayer_; }
	
	public void SetAgent(Agent theAgent_,int player_)
	{ 
		theAgent_.SetPlayer(player_);
		theAgents[player_-1] = theAgent_; 
	}
	
	//Executes an action on the current state
	protected void ExecuteAction(Action action_)
	{ currentState.ExecuteAction(action_); }
	
	//Returns the next player whose turn it is
	public int NextPlayer(int currentPlayer_,int totalPlayers_)
	{
		if (++currentPlayer_ == totalPlayers_)
			currentPlayer_ = 0;
		return currentPlayer_;
	}
	
	//Determines who the winner is from the assigned scores
	protected int Winner()
	{
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
	public int PlayGame(boolean print_)
	{
		ResetState();
		
		int currentPlayer = startPlayer-2;
		int totalPlayers = theAgents.length;
		
		while (!GameOver())
		{
			counter++;
			currentPlayer = NextPlayer(currentPlayer,totalPlayers);
			State[] possibleStates = NextStates(currentPlayer+1);
			if (possibleStates.length != 0)
				currentState = GetBestState(theAgents[currentPlayer],possibleStates);
			if (print_)
				currentState.Print();
		}
		return Winner();	
	}
	
	//Returns the best state given an evaluiataion and array of states
	protected State GetBestState(Agent evaluate_,State[] stateArray_)
	{ return evaluate_.ReturnBest(stateArray_).Clone(); }
	
	//Sets the start state appropriatly depending on the game played
	protected abstract void InitialState();
	
	//Sets the start state of the game
	public abstract void SetStartState(State state_);
	
	//Makes the current state the start state of the game
	public abstract void ResetState();
		
	//Specify the rules that determine if a game has ended or not
	public abstract boolean GameOver();
	
	//Returns the best state given an evaluiataion and array of states
	//protected abstract State GetBestState(Agent evaluate_,State[] stateArray_);
	
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
	
	protected int totalPlayers;
	
	//A counter to keep track of the number of turns required to end the game
	protected int counter;
	
	private Agent[] theAgents;
	
	private int startPlayer;
	
	protected Evaluations theEvaluation;
	
	protected ParticleAdapter adapter;
}
