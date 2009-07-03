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
package net.sourceforge.cilib.games.agent;


import net.sourceforge.cilib.games.game.Game;
import net.sourceforge.cilib.games.items.GameToken;
import net.sourceforge.cilib.games.states.GameState;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.util.Cloneable;

/**
 * This class represents a game playing agent.
 * @author leo
 */
public abstract class Agent implements Cloneable {
    private static final long serialVersionUID = -9100541859324175287L;

    //the playerID of the agent
    protected int playerID;
    //the fitness of the agent
    protected Fitness agentScore;
    //the token representing the agent, this is initialized to DEFAULT
    protected Enum<?> agentToken;

    public Agent() {
        this.playerID = 0;
        agentToken = GameToken.DEFAULT;
    }
    public Agent(Agent other){
        playerID = other.playerID;
        if(other.agentScore != null)
            agentScore = other.agentScore.getClone();
        agentToken = other.agentToken;
    }

    public int getPlayerID(){
        return playerID;
    }

    public void setPlayerID(int playerID){
        this.playerID = playerID;
    }

    /**
     * Initialize the agent with the contents of an entity. This data should represent what the
     * @param agentData the data to initialize the agent with
     */
    public abstract void initializeAgent(Type agentData);

    /**
     * {@inheritDoc}
     */
    public abstract Agent getClone();

    /**
     * Alter the current game by making a decision.
     * @param game the game state
     */
    public abstract void move(Game<GameState> game);

    /**
     * Get the domain the agent's solution is represented by
     * @return the relevant {@linkplain DomainRegistry}
     */
    public abstract DomainRegistry getAgentDomain();

    public Fitness getAgentScore() {
        return agentScore;
    }
    public void setAgentScore(Fitness agentScore) {
        this.agentScore = agentScore;
    }
    public Enum<?> getAgentToken() {
        return agentToken;
    }
    public void setAgentToken(Enum<?> agentToken) {
        this.agentToken = agentToken;
    }
    /**
     * Creates an enum based on the string, enum has to be inside the GameToken enum class, and cannot be another
     * nested enum. NOTE: Could refactor this into the XML creation stuff if neccesary
     * @param agentToken the string representation of the enum
     */
    @SuppressWarnings("unchecked")
    public void setAgentToken(String agentToken) {
        try{
            String [] tokenParts = agentToken.split("\\.");
            Enum<?> vals[] = (Enum<?>[])Class.forName("net.sourceforge.cilib.games.items.GameToken$" + tokenParts[0]).getEnumConstants();
            for(Enum instance: vals){
                if(instance.name().equals(tokenParts[1]))
                    this.agentToken = instance;
            }
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
