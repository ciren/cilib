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
package net.sourceforge.cilib.games.states;

import net.sourceforge.cilib.games.items.GameItem;

/**
 * This is a {@linkplain GameState} where all the {@linkplain GameItem}'s are stored in a matrix. For games like TickTackToe or Tetris
 * storing all the gameItems in a list would not be desireable since accessing an item in a specific location in the grid would be
 * much more complex.
 * @author leo
 */
public class GridGameState extends GameState {
    private static final long serialVersionUID = 1906158746298773282L;
    int gridWidth;
    int gridHeight;
    protected GameItem state[][];
    public GridGameState() {
        gridWidth = 0;
        gridHeight = 0;
        state = new GameItem[gridWidth][gridHeight];
    }

    public GridGameState(int gridWidth, int gridHeight) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        state = new GameItem[gridWidth][gridHeight];
    }

    /**
     * Copy constructor
     * @param other
     */
    public GridGameState(GridGameState other) {
        super(other);
        gridWidth = other.gridWidth;
        gridHeight = other.gridHeight;
        state = new GameItem[gridWidth][gridHeight];
        for(int i = 0; i < gridWidth; ++i){
            for(int j = 0; j < gridHeight; ++j){
                if(other.state[i][j] != null){
                    state[i][j] = other.state[i][j].getClone();
                }
            }
        }
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public void setGridHeight(int gridHeight) {
        this.gridHeight = gridHeight;
        state = new GameItem[gridWidth][this.gridHeight];
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public void setGridWidth(int gridWidth) {
        this.gridWidth = gridWidth;
        state = new GameItem[this.gridWidth][gridHeight];
    }
    /**
     * Get an Item at a specific index in the matrix.
     * @param i the specified Column
     * @param j the specified Row
     * @return the Game Item. If this position is empty, {@code null} will be returned.
     */
    public GameItem getItem(int i, int j){
        if(i < 0 || i >= state.length || j < 0 || j >= state[i].length)
            throw new RuntimeException("Invalid Index : " + i + ", " + j);
        return state[i][j];
    }

    /**
     * Set an item at a specific index in the matrix
     * @param i the specified Column
     * @param j the specified Row
     * @param item the item to set. This method does not check if an item already exists, and will simply override
     * what is in that position.
     */
    public void setItem(int i, int j, GameItem item){
        state[i][j] = item;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearState(){
        state = new GameItem[gridWidth][gridHeight];
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public GridGameState getClone() {
        return new GridGameState(this);
    }

}
