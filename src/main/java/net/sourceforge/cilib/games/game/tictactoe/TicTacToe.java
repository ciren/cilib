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
package net.sourceforge.cilib.games.game.tictactoe;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.games.game.Game;
import net.sourceforge.cilib.games.game.GridGame;
import net.sourceforge.cilib.games.game.StateGame;
import net.sourceforge.cilib.games.items.GameEnum;
import net.sourceforge.cilib.games.items.GameItem;
import net.sourceforge.cilib.games.items.GameToken;
import net.sourceforge.cilib.games.items.PlayerItem;
import net.sourceforge.cilib.games.result.AbstractGameResult;
import net.sourceforge.cilib.games.result.DrawResult;
import net.sourceforge.cilib.games.result.WinGameResult;
import net.sourceforge.cilib.games.states.GameState;
import net.sourceforge.cilib.games.states.GridGameState;

/**
 * This is an implimentation of the game of Tic Tac Toe
 * @author leo
 *
 */
public class TicTacToe extends GridGame implements StateGame {
    private static final long serialVersionUID = -6136653612592964152L;
    AbstractGameResult result;
    public TicTacToe() {
        setWidth(3); //default size is a 3 x 3 game
        setHeight(3);
        result = null;
    }

    /**
     * Copy constructor
     * @param other
     */
    public TicTacToe(TicTacToe other) {
        super(other);
        result = other.result;
    }

    /**
     * {@inheritDoc}
     */
    public TicTacToe(TicTacToe other, GridGameState newState) {
        super(other, newState);
        result = other.result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void display() {
        System.out.println("");
        for(int j = 0; j < getHeight(); ++j){
            String line = "|";
            for(int i = 0; i < getWidth(); ++i){
                GameItem item = getCurrentState().getItem(i, j);
                char player = ' ';
                if(item != null){
                    player = ((GameEnum)getPlayer(((PlayerItem)item).getPlayerID()).getAgentToken()).getDescription().charAt(0);
                }
                line += player + "|";
            }
            System.out.println(line);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean gameOver() {
        int[][] Down = new int[2][getWidth()];
        int[][] Across = new int[2][getHeight()];
        int[][] SideWays = new int[2][2];
        boolean allCelssOccupied = true;
        for(int i = 0; i < getWidth(); ++i){
            for(int j = 0; j < getHeight(); ++j){
                GameItem item = getCurrentState().getItem(i, j);
                if(item != null){
                    int player = ((PlayerItem)item).getPlayerID();

                    ++Down[player - 1][i];
                    ++Across[player - 1][j];
                    if(Down[player - 1][i] == getWidth() || Across[player - 1][j] == getHeight()){
                        result = new WinGameResult(player);
                        return true;
                    }

                    if(i == j){ //side left to right
                        ++SideWays[player - 1][0];
                        if(SideWays[player - 1][0] == getWidth()){
                            result = new WinGameResult(player);
                            return true;
                        }
                    } //side right to left
                    if(j == (getWidth() - 1) - i){
                        ++SideWays[player - 1][1];
                        if(SideWays[player - 1][1] == getWidth()){
                            result = new WinGameResult(player);
                            return true;
                        }
                    }
                }
                else
                    allCelssOccupied = false;
            }
        }
        if(allCelssOccupied)
            result = new DrawResult();
        return allCelssOccupied;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Game<GridGameState> getClone() {
        return new TicTacToe(this);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Game<GridGameState> getClone(GridGameState newState) {
        return new TicTacToe(this, newState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractGameResult getGameResult() {
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeGame() {
        if(getWidth() != getHeight()){
            throw new RuntimeException("Tic Tac Toe can only be played on a square grid of equal side lengths");
        }
        if(players.size() != 2)
            throw new RuntimeException("TicTacToe can only be played by two agents");
        getCurrentState().clearState();
        players.get(0).setAgentToken(GameToken.TicTacToe.CROSS); //start player is X
        players.get(1).setAgentToken(GameToken.TicTacToe.NOUGHT); //other player is O
        result = null;
    }

    /**
     * {@inheritDoc}
     */
    public List<GameState> generateStates(int currentPlayer) {
        List<GameState> newStates = new ArrayList<GameState>();
        for(int j = 0; j < getHeight(); ++j){
            for(int i = 0; i < getWidth(); ++i){
                GameItem item = getCurrentState().getItem(i, j);
                if(item == null){
                    GridGameState newState = getCurrentState().getClone();
                    newState.setItem(i, j, new PlayerItem(currentPlayer, getPlayer(currentPlayer).getAgentToken()));
                    newStates.add(newState);
                }
            }
        }
        return newStates;
    }
}
