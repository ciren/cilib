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
package net.sourceforge.cilib.games.game.tetris;

import net.sourceforge.cilib.games.items.GameEnum;
import net.sourceforge.cilib.games.items.GameItem;
import net.sourceforge.cilib.games.items.GridLocation;
import net.sourceforge.cilib.games.items.ItemLocation;
import net.sourceforge.cilib.games.states.GridGameState;

/**
 * @author leo
 * This class represents a Block in tetris
 */
public class TetrisBlock extends GameItem {
	private static final long serialVersionUID = -7655885046364352004L;
	double timeStationary;
	int movesDown;

	public TetrisBlock(Enum <? extends GameEnum> token, ItemLocation itemLocation) {
		super(token, itemLocation);
		timeStationary = 0;
		movesDown = 0;
	}
	public TetrisBlock(TetrisBlock other){
		super(other);
		timeStationary = other.timeStationary;
		movesDown = other.movesDown;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TetrisBlock getClone() {
		return new TetrisBlock(this);
	}

	public boolean update(double currentSpeed, GridGameState state){
		//add value to the amount of time spent in this position, if it exceeds the threshold for the current speed then this block moves down
		return true;
	}

	public void addMoveDown(){
		++movesDown;
	}

	public void updateMovesDown(){
		int currY = ((GridLocation)itemLocation).getInt(1);
		((GridLocation)itemLocation).setInt(1, currY + movesDown);
		movesDown = 0;
	}

	public int getInt(int dimension){
		return ((GridLocation)itemLocation).getInt(dimension);
	}
	public void setInt(int dimention, int value){
		((GridLocation)itemLocation).setInt(dimention, value);
	}
}
