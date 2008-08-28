/*
 * Copyright (C) 2003 - 2008
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
package net.sourceforge.cilib.games.game;

import java.util.Vector;

import net.sourceforge.cilib.coevolution.score.EntityScore;
import net.sourceforge.cilib.games.agent.Agent;
import net.sourceforge.cilib.games.game.scoring.GameScoringStrategy;
import net.sourceforge.cilib.games.game.scoring.WinLoseDrawValueScoringStrategy;
import net.sourceforge.cilib.games.result.AbstractGameResult;
import net.sourceforge.cilib.games.result.DrawResult;
import net.sourceforge.cilib.games.result.WinGameResult;
import net.sourceforge.cilib.games.states.GameState;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.util.Cloneable;

/**
 * @author leo
 * A general framework for a Game, turn based or real time. A game is played by Agents until it is terminated. A game has three possible outcomes
 * from the perspective of a player. Win, lose or draw. A fitness is assigned to an agent by the scoring strategy.
 */
public abstract class Game implements Cloneable {
	private static final long serialVersionUID = -4258915435750291244L;
	
	//The current state of the game
	protected GameState currentState;
	//an array of game players
	protected Vector<Agent> players;
	//the current iteration of the game. An iteration is completed after each player has made a move and the state is updated
	protected int currentIteration;
	//the current active player
	protected int currentPlayer;
	//end of game flag
	//protected boolean gameOver;
	//this flag determines if players make decisions simultaneously or in a turn based fashion
	protected boolean turnBasedGame; 
	//the scoring strategy assigns fitness values to the players
    private GameScoringStrategy scoringStrategy;
	/**
	 * Default constructor
	 */
	public Game() {
		currentState = new GameState();
		players = new Vector<Agent>();
		currentPlayer = 1;
		currentIteration = 0;
		//gameOver = false;
		turnBasedGame = true;
		scoringStrategy = new WinLoseDrawValueScoringStrategy();
	}
	
	/**
	 * Copy constructor
	 * @param other the other game object
	 */
	public Game(Game other){
		currentState = new GameState(other.currentState);
		players = new Vector<Agent>();
		for(Agent p: other.players)
			players.add(p.getClone());
		currentPlayer = other.currentPlayer;
		//gameOver = other.gameOver;
		currentIteration = other.currentIteration;
		turnBasedGame = other.turnBasedGame;
		scoringStrategy = other.scoringStrategy;
	}
	
	public GameState getCurrentState(){
		return currentState;
	}
	
	public void setAgent(Agent player){
		if(player.getPlayerID() == 0)
			player.setPlayerID(players.size() + 1);
		
		players.add(player);
	}
	
	/**
	 * This method is used to get the specific domain for an agents solution vector
	 * @param playerID the id of the player
	 * @return the domainregistry of that players solution
	 */
	public DomainRegistry getDomainForPlayer(int playerID){
		for(Agent p : players){
			if(p.getPlayerID() == playerID){
				return p.getAgentDomain();
			}
		}
		throw new RuntimeException("invalid playerid specified");
	}
	
	/**
	 * Assign a score to a player
	 * @param playerID the id of the player
	 * @param score the fitness
	 */
	public void assignPlayerScore(int playerID, Fitness score){
		for(Agent p : players){
			if(p.getPlayerID() == playerID){
				p.setAgentScore(score);
				return;
			}
		}
		throw new RuntimeException("invalid playerid specified");
	}
	
	/**
	 * return the fitness of the agent
	 * @param playerID the id of the player
	 * @return the relevant fitness
	 */
	public Fitness getPlayerScore(int playerID){
		for(Agent p : players){
			if(p.getPlayerID() == playerID){
				return p.getAgentScore();
			}
		}
		throw new RuntimeException("invalid playerid specified");
	}
	
	/**
	 * get the amount of players
	 * @return amount of players
	 */
	public int amPlayers(){
		return players.size();
	}
	
	/**
	 * This method initializes an agent with entity data.
	 * @param playerID the id of the player
	 * @param agentData the entity data that the agent uses to make decisions
	 */
	public void initializeAgent(int playerID, Type agentData){
		for(Agent p : players){
			if(p.getPlayerID() == playerID){
				p.initializeAgent(agentData);
				return;
			}
		}
		throw new RuntimeException("invalid playerid specified");
	}
	
	
	/**
	 * Re-arrange the players in the player vector to put a new agent first
	 * @param startPlayer the id of the lpayer to go first
	 */
	public void setStartPlayer(int startPlayer){
		if(startPlayer > amPlayers())
			throw new RuntimeException("Invalid start player specified, playerid larger than player array");
		//put startPlayer at the beginning of the player array
		int startNo = -1;
		for(Agent p: players){
			if(p.getPlayerID() == startPlayer){
				startNo = players.indexOf(p);
				break;
			}
		}
		
		if(startNo != 0){
			Agent newStart = players.get(startNo).getClone();
			players.remove(startNo);
			players.insertElementAt(newStart, 0);
		}		
	}
	
	/**
	 * loop through the game until the end conditions are met. The Agents fitness values are assigned in this method
	 */
	public void playGame(){
		currentIteration = 0;
		initializeGame();
		boolean gameOver = gameOver();
	//	display();
		while(!gameOver)
		{				
			for(Agent p : players){	
				currentPlayer = p.getPlayerID();
				p.move(this);
				if(turnBasedGame && gameOver()){
					gameOver = true;
					break;				
				}
			}
			if(!gameOver && (this instanceof UpdateGame)){
				((UpdateGame)this).Update();				
			}			
			
		//	display();
			++currentIteration;
			
			if(!turnBasedGame)
				gameOver = gameOver();
		}
		scoringStrategy.assignPlayerScores(this);
	}
	
	
	/**
	 * Store the result and fitness for a specific player in the entityscore object
	 * @param playerID the id of the agent
	 * @param score the entityscore object that is manipulated
	 */
	public void setEntityScore(int playerID, EntityScore score){
		AbstractGameResult result = getGameResult();
		if(result instanceof WinGameResult){
			if(playerID == ((WinGameResult)result).getWinner())
				score.win(getPlayerScore(playerID));
			else
				score.lose(getPlayerScore(playerID));
		}
		else if(result instanceof DrawResult){
			score.draw(getPlayerScore(playerID));
		}
	}
	
	/**
	 * perform any game specific initialization
	 */
	public abstract void initializeGame();
	
	/**
	 * check if the game should terminate
	 * @return the endgame flag
	 */
	public abstract boolean gameOver();
	
	/**
	 * Get the result of the game (Win/Lose/Draw) 
	 * @return the relevant {@linkplain}AbstractGameResult object
 	 */
	public abstract AbstractGameResult getGameResult();
	
	/**
	 * {@inheritDoc}
	 */
	public abstract Game getClone();	
	
	public abstract void display();

	public void setScoringStrategy(GameScoringStrategy scoringStrategy) {
		this.scoringStrategy = scoringStrategy;
	}

}
