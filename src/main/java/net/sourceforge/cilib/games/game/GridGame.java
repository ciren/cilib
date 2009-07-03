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
