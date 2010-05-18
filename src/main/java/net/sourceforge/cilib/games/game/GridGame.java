/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.games.game;

import net.sourceforge.cilib.games.states.GridGameState;

/**
 * @author leo
 * This class represents any game that takes place in a grid. It will use the
 * concrete {@linkplain GridGameState} object
 */
public abstract class GridGame extends Game<GridGameState> {
    private static final long serialVersionUID = -3460317865794650394L;
    /**
     *
     */
    public GridGame() {
        setCurrentGameState(new GridGameState());
    }
    /**
     * @param other
     */
    public GridGame(GridGame other) {
        super(other);
    }
    /**
     * @param other
     */
    public GridGame(GridGame other, GridGameState newState) {
        super(other, newState);
    }


    public int getWidth(){
        return getCurrentState().getGridWidth();
    }
    public int getHeight(){
        return getCurrentState().getGridHeight();
    }
    public void setWidth(int width){
        getCurrentState().setGridWidth(width);
    }
    public void setHeight(int height){
        getCurrentState().setGridHeight(height);
    }
}
