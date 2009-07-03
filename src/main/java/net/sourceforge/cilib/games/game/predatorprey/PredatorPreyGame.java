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
package net.sourceforge.cilib.games.game.predatorprey;

import net.sourceforge.cilib.algorithm.InitialisationException;
import net.sourceforge.cilib.games.agent.Agent;
import net.sourceforge.cilib.games.game.Game;
import net.sourceforge.cilib.games.game.predatorprey.init.PredPreyPositionInitializationStrategy;
import net.sourceforge.cilib.games.game.predatorprey.init.RandomPredPreyInitializationStrategy;
import net.sourceforge.cilib.games.items.GameEnum;
import net.sourceforge.cilib.games.items.GameItem;
import net.sourceforge.cilib.games.items.GameToken;
import net.sourceforge.cilib.games.items.GridItem;
import net.sourceforge.cilib.games.items.GridLocation;
import net.sourceforge.cilib.games.items.PlayerItem;
import net.sourceforge.cilib.games.result.AbstractGameResult;
import net.sourceforge.cilib.games.result.WinGameResult;
import net.sourceforge.cilib.games.states.ListGameState;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

/**
 * @author leo
 * This is an implimentation of the predator prey game. The game is played on a 2 dimentional grid with a Predator and a Prey agent.
 * The goal of the Predator is to catch the Prey, and vice versa. For this implimentation the players take turns making movement decisions.
 */
public class PredatorPreyGame extends Game<ListGameState> {
    private static final long serialVersionUID = 332203013419474482L;
    int maxIterations;
    int boardHeight;
    int boardWidth;
    PredPreyPositionInitializationStrategy initializationStrategy;
    /**
     * {@inheritDoc}
     */
    public PredatorPreyGame(){
        maxIterations = 20;
        boardHeight = 9;
        boardWidth = 9;
        setCurrentGameState(new ListGameState());
        initializationStrategy = new RandomPredPreyInitializationStrategy();
    }
    /**
     * {@inheritDoc}
     */
    public PredatorPreyGame(PredatorPreyGame other){
        super(other);
        boardHeight = other.boardHeight;
        boardWidth = other.boardWidth;
        maxIterations = other.maxIterations;
        initializationStrategy = other.initializationStrategy.getClone();
    }
    /**
     * {@inheritDoc}
     */
    public PredatorPreyGame(PredatorPreyGame other, ListGameState newState){
        super(other, newState);
        boardHeight = other.boardHeight;
        boardWidth = other.boardWidth;
        maxIterations = other.maxIterations;
        initializationStrategy = other.initializationStrategy;
    }
    /**
     * This function determins whether or not the predator has caught the prey
     * @return true if the predator has caught the prey, otherwise false.
     */
    private boolean predatorCaughtPrey()
    {
        try
        {
            //if predator and prey players are next to or on the same cell then game over
            if(getCurrentState().getItem(0).getLocation().getDistance(new EuclideanDistanceMeasure(), getCurrentState().getItem(1).getLocation()) < 2.0)
                return true;

            return false;
        }
        catch(Exception e)
        {
            throw new InitialisationException("Game not initialized, predator and prey items do not exist");
        }
    }

    /**
     * Move a specified player
     * @param playerID the player to move
     * @param x the amount to move on the X axes
     * @param y the amount to move on the Y axes
     */
    public void movePlayer(int playerID, int x, int y){
        try{
            Vector moveVector = new Vector(2);
            moveVector.add(new Int(x));
            moveVector.add(new Int(y));
            for(int i = 0; i < getCurrentState().getSize(); ++i){
                if(((PlayerItem)getCurrentState().getItem(i)).getPlayerID() == playerID){
                    //move the item by the specified coords
                    getCurrentState().getItem(i).getLocation().moveItem(moveVector);
                }
            }
        }
        catch(Exception e){
            throw new InitialisationException("Game not initialized, predator and prey items not found");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean gameOver() {
        if(getCurrentIteration() >= maxIterations)
            return true;

        return predatorCaughtPrey();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PredatorPreyGame getClone() {
        return new PredatorPreyGame(this);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public PredatorPreyGame getClone(ListGameState newState) {
        return new PredatorPreyGame(this, newState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractGameResult getGameResult() {
        int predatorID = 2;
        if(getPlayer(1).getAgentToken().equals(GameToken.PredatorPrey.PREDATOR))
            predatorID = 1;
        if((getCurrentIteration() >= maxIterations) && !predatorCaughtPrey())
            return new WinGameResult(predatorID == 1 ? 2 : 1); //prey won
        else
            return new WinGameResult(predatorID); //predator won
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public void initializeGame() {
        if(players.size() != 2)
            throw new RuntimeException("Predator prey can only be played with 2 players");

        getCurrentState().clearState();
        for(Agent p : players){
            getCurrentState().addGameItem(new GridItem(p.getPlayerID(), p.getAgentToken(), boardWidth, boardHeight));
        }
        initializationStrategy.initializePP(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void display() {
        System.out.println("");
        GridLocation itemLoc = new GridLocation(boardWidth, boardHeight);
        for(int y = 0; y < boardHeight; ++y){
            String line = "|";
            for(int x = 0; x < boardWidth; ++x){
                itemLoc.setInt(0, x);
                itemLoc.setInt(1, y);
                GameItem item = getCurrentState().getItem(itemLoc);
                if(item != null){
                    Enum<?> pp = item.getToken();
                    line += ((GameEnum)pp).getDescription() + "|";
                }
                else
                    line += " |";
            }
            System.out.println(line);
        }
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    public void setBoardHeight(int boardHeight) {
        this.boardHeight = boardHeight;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public void setBoardWidth(int boardWidth) {
        this.boardWidth = boardWidth;
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    public void setInitializationStrategy(
            PredPreyPositionInitializationStrategy initializationStrategy) {
        this.initializationStrategy = initializationStrategy;
    }

    public PredPreyPositionInitializationStrategy getInitializationStrategy() {
        return initializationStrategy;
    }
}
