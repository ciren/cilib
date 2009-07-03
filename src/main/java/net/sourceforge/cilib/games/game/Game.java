/**
 * Copyright (C) 2003 - 2009
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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.coevolution.score.EntityScore;
import net.sourceforge.cilib.games.agent.Agent;
import net.sourceforge.cilib.games.game.scoring.GameScoringStrategy;
import net.sourceforge.cilib.games.game.scoring.WinLoseDrawValueScoringStrategy;
import net.sourceforge.cilib.games.measurement.AgentMeasure;
import net.sourceforge.cilib.games.random.GameSeedingStrategy;
import net.sourceforge.cilib.games.result.AbstractGameResult;
import net.sourceforge.cilib.games.result.DrawResult;
import net.sourceforge.cilib.games.result.ScoreGameResult;
import net.sourceforge.cilib.games.result.WinGameResult;
import net.sourceforge.cilib.games.states.GameState;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.util.Cloneable;

/**
 * @author leo
 * A general framework for a Game, turn based or real time. A game is played by Agents until it is terminated. A game has four possible outcomes
 * from the perspective of a player: Win, lose, draw or receive a score. A fitness is assigned to each game playing {@linkplain Agent} by the {@linkplain GameScoringStrategy}.
 */
public abstract class Game<E extends GameState> implements Cloneable {
    private static final long serialVersionUID = -4258915435750291244L;

    //The current state of the game
    private E currentState;
    //an array of game players
    protected List<Agent> players;
    //the current active player
    protected int currentPlayer;
    //end of game flag
    //protected boolean gameOver;
    //the scoring strategy assigns fitness values to the players
    private GameScoringStrategy scoringStrategy;
    //A list of measurements
    protected List<AgentMeasure> agentMeasurement;
    /**
     * Default constructor
     */
    public Game() {

        players = new ArrayList<Agent>();
        currentPlayer = 1;
        //gameOver = false;
        scoringStrategy = new WinLoseDrawValueScoringStrategy();
        agentMeasurement = new ArrayList<AgentMeasure>();
    }

    /**
     * Copy constructor
     * @param other the other game object
     */
    @SuppressWarnings("unchecked")
    public Game(Game<E> other){
        //always do a deep copy of the game state
        currentState = (E)other.currentState.getClone();
        players = new ArrayList<Agent>();
        for(Agent player: players){
            players.add(player.getClone());
        }
        currentPlayer = other.currentPlayer;
        //gameOver = other.gameOver;
        scoringStrategy = other.scoringStrategy;

        agentMeasurement = new ArrayList<AgentMeasure>();
        for(AgentMeasure measure: other.agentMeasurement){
            agentMeasurement.add(measure.getClone());
        }
    }

    /**
     * Copy constructor, copy the given {@linkplain Game} object but use the given {@linkplain GameState} obect.
     * @param other the other game object
     * @param newState the new state to use as this {@linkplain Game}'s {@linkplain GameState}
     */
    @SuppressWarnings("unchecked")
    public Game(Game<E> other, E newState){
        //always do a deep copy of the game state
        currentState = newState;
        players = other.players;

        currentPlayer = other.currentPlayer;
        //gameOver = other.gameOver;
        scoringStrategy = other.scoringStrategy;
        agentMeasurement = other.agentMeasurement;
    }

    /**
     * Returns the state of the game that the agent needs to make a decision. This implies that
     * the state the agent receives isn't necceseraly the entire game state as it is
     * at that point in time. This method can be overwritten to introduce noise or imperfect player
     * information.
     * @return default returns the current game state, override to return other state
     */
    public E getDecisionState(){
        return currentState;
    }
    /**
     * returns the current state of the game, this isnt always the same as the decision state
     * @return the current state of the game
     */
    public E getCurrentState(){
        return currentState;
    }

    /**
     * Set the current state of the game
     * @param state the new game state
     */
    public void setCurrentGameState(E state){
        if(state == null)
            throw new RuntimeException("Cannot set the state to null");
        currentState = state;
        if(this instanceof RealTimeGame){
            ((RealTimeGame)this).recordRoundStartState();
        }
    }/*
    public void setCurrentGameState(E state, int stateIteration){
        if(state == null)
            throw new RuntimeException("Cannot set the state to null");
        currentIteration = stateIteration;
        setCurrentGameState(state);
    }*/
    /**
     * Add a new agent
     * @param player the {@linkplain}Agent to add
     */
    public void setAgent(Agent player){
        if(player.getPlayerID() == 0)
            player.setPlayerID(players.size() + 1);

        players.add(player);
    }
    /**
     * Add an agent with a specified playerID, if it already exists, replace it
     * @param player the new {@linkplain}Agent
     * @param playerID the playerID for the agent
     */
    public void setAgent(Agent player, int playerID){
        player.setPlayerID(playerID);

        boolean found = false;
        for(int i = 0; i < players.size(); ++i){
            if(players.get(i).getPlayerID() == playerID){
                players.set(i, player);
                found = true;
                break;
            }
        }
        if(!found){
            players.add(player);
        }
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
     * get the number of players
     * @return number of players
     */
    public int getPlayerCount(){
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
        if(startPlayer > getPlayerCount())
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
            players.add(0, newStart);
        }
    }
    public Agent getPlayer(int playerID){
        for(Agent p : players){
            if(p.getPlayerID() == playerID){
                return p;
            }
        }
        throw new RuntimeException("invalid playerid specified");
    }
    /**
     * This method returns the playerID who would play directly after the playerID that is given to the method.
     * This is not necceseraly in numeric order, since players take turns going first
     * @param playerID the playerID that preceeds the playerID to be returned
     * @return the player who's turn it is next
     */
    public int getNextPlayerID(int playerID){
        for(int i = 0; i < players.size(); ++i)
            if(players.get(i).getPlayerID() == playerID){
                if(i == players.size() - 1)
                    return players.get(0).getPlayerID();
                else
                    return players.get(i+1).getPlayerID();
            }
        throw new RuntimeException("invalid playerid specified");
    }
    /**
     * return a list of all the playerID's, in the order that they would play starting with startPlayerID
     * @param startPlayerID the ID of the starting player
     * @return the list of playerID's in the order that they would play
     */
    public int[] getPlayerIDList(int startPlayerID){
        int[] IDs = new int[getPlayerCount()];
        IDs[0] = startPlayerID;
        int c = 1;
        for(int i = 0; i < players.size(); ++i){
            if(players.get(i).getPlayerID() != startPlayerID)
            {
                IDs[c] = players.get(i).getPlayerID();
                ++c;
            }
        }
        return IDs;
    }

    /**
     * Measure features of the current state of the game, with the list of measurements supplied.
     *
     */
    @SuppressWarnings("unchecked")
    private void measureData(){
        for(AgentMeasure measure: agentMeasurement){
            measure.measure((Game<GameState>)this);
        }
    }
    /**
     * Remove all {@linkplain AgentMeasure} objects in the list
     *
     */
    public void clearMeasurements(){
        agentMeasurement.clear();
    }
    /**
     * Clear all the measured data while keeping all the {@linkplain}AgentMeasure
     *
     */
    public void clearMeasurementData(){
        for(AgentMeasure measure: agentMeasurement){
            measure.clearData();
        }
    }
    public List<AgentMeasure> getAgentMeasurements(){
        return agentMeasurement;
    }
    /**
     * Add a new {@linkplain AgentMeasure} to the list of measurements
     * @param measure the new measurement
     */
    public void addMeasurement(AgentMeasure measure){
        agentMeasurement.add(measure);
    }
    /**
     * initialize and play the game until the end conditions are met while recording any playing information.
     *  The Agents fitness values are assigned in this method
     */
    @SuppressWarnings("unchecked")
    public void playGame(boolean display){
        currentState.getRandomizer().seedGenerator();
        currentState.resetIterationCount(); //set to 0
        currentState.increaseIteration(); //set to 1
        initializeGame();
        boolean gameOver = gameOver();
        measureData();
        if(display)
            display();

        while(!gameOver)
        {
            if(this instanceof RealTimeGame)
                ((RealTimeGame)this).recordRoundStartState();
            for(Agent p : players){
                currentPlayer = p.getPlayerID();
                p.move((Game<GameState>)this);
                if(!(this instanceof RealTimeGame)){
                    if(display)
                        display();
                    if(gameOver()){
                        gameOver = true;
                        break;
                    }
                }
            }
            if(!gameOver && (this instanceof UpdateGame)){
                ((UpdateGame)this).Update();
            }
            if(display && this instanceof RealTimeGame)
                display();
            currentState.increaseIteration();
            if(this instanceof RealTimeGame)
                gameOver = gameOver();
            measureData();
        }
        scoringStrategy.assignPlayerScores(this);
    }
    public void playGame(){
        playGame(false);
    }

    /**
     * Store the result and fitness for a specific player in the entityscore object
     * @param playerID the id of the agent
     * @param score the entityscore object that is manipulated
     */
    public void setEntityScore(int playerID, EntityScore score){
        AbstractGameResult result = getGameResult();
        if(result instanceof WinGameResult){
            if(playerID == ((WinGameResult)result).getWinnerID())
                score.win(getPlayerScore(playerID));
            else
                score.lose(getPlayerScore(playerID));
        }
        else if(result instanceof DrawResult){
            score.draw(getPlayerScore(playerID));
        }
        else if(result instanceof ScoreGameResult){
            score.win(getPlayerScore(playerID));
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
    public abstract Game<E> getClone();

    /**
     * Make a clone of this Game object but only do a deep copy on that which is neccesary, this would typically exlude game player logic.
     * This method is typically used by the {@linkplain}StateTraversalStrategy and {@linkplain}Agent with regards to decision making when playing
     * the game. Since this process includes making alot of copies of the game a seperate, faster method is neccesary that does not waste time by
     * making unneccesary copies of the object. This method uses the copy constructor for the Game class and passes true to the quickCopy flag
     * @param other the Game object to copy
     * @return the semi-deep copy
     */
//    public abstract Game<E> getShallowClone();
    /**
     * Get a clone with the given {@linkplain GameState}
     * This method should typically be used by the {@linkplain}StateTraversalStrategy and {@linkplain}Agent with regards to decision making when playing
     * the game. Since this process includes making alot of copies of the game a seperate, faster, method is neccesary that does not waste time by
     * making unneccesary copies of the game state and player logic.
     * @param newState the new sate to use
     * @return the copy
     */
    public abstract Game<E> getClone(E newState);


    /**
     * Print the game to the console, is useful during testing.
     */
    public abstract void display();
    /**
     * Set the {@linkplain GameScoringStrategy} for this game. The scoring strategy might require certain {@linkplain AgentMeasure}s and
     * therefore clears any existing measures before adding its own.
     * @param scoringStrategy the scoring strategy
     */
    public void setScoringStrategy(GameScoringStrategy scoringStrategy) {
        this.scoringStrategy = scoringStrategy;
        this.clearMeasurements();
        scoringStrategy.initializeMeasurements(this);
    }

    public int getCurrentIteration() {
        return currentState.getCurrentIteration();
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public GameScoringStrategy getScoringStrategy() {
        return scoringStrategy;
    }
    /**
     * Set the seeding strategy of the game, this will determine how many and when unique games are played. The {@linkplain GameSeedingStrategy} is
     * in the {@linkplain GameState}
     * @param seedStrategy
     */
    public void setSeedingStrategy(GameSeedingStrategy seedStrategy){
        currentState.setRandomizer(seedStrategy);
    }

}
