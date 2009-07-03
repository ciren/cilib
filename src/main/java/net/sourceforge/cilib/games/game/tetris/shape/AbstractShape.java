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
package net.sourceforge.cilib.games.game.tetris.shape;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.games.game.tetris.TetrisBlock;
import net.sourceforge.cilib.games.states.GridGameState;
import net.sourceforge.cilib.util.Cloneable;

/**
 * This class represents a Shape in tetris.
 * @author leo
 *
 */
public abstract class AbstractShape implements Cloneable {
	List<TetrisBlock> shapeBlocks;
	int currentOrientation;
	int staticCellIndex;
	public AbstractShape() {
		staticCellIndex = 0;
		currentOrientation = 0;
		shapeBlocks = new ArrayList<TetrisBlock>();
	}
	/**
	 * {@inheritDoc}
	 */
	public AbstractShape(AbstractShape other) {
		staticCellIndex = other.staticCellIndex;
		currentOrientation = other.currentOrientation;
		shapeBlocks = new ArrayList<TetrisBlock>();
		for(int i = 0; i < other.shapeBlocks.size(); ++i)
			shapeBlocks.add(other.shapeBlocks.get(i).getClone());
	}
	/**
	*attempt to move each block in shape down
	*if collides before successfull move dont change pos and return false
	*if moves but lands on bottom or above other block return false
	*else return true
	 * @param currentSpeed
	 * @param state
	 * @return
	 */
	public boolean update(double currentSpeed, GridGameState state){
		//attempt to move each block in shape down
		//if collides before successfull move dont change pos and return false
		//if moves but lands on bottom or above other block return false
		//else return true
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	public abstract AbstractShape getClone();
	/**
	 * Rotate the shape to its next possible orientation
	 */
	public abstract void rotate();
	/**
	 * Rotate this shape for the specified amount of times
	 * @param AmRotations the amount of Rotations to do
	 * @return Wheather or not the rotation was successfull
	 */
    public boolean rotate(int AmRotations)
    {
        int OldPos = currentOrientation;
        for (int i = 0; i < AmRotations; ++i)
            rotate();
        if (currentOrientation != OldPos)
            return true;
        return (AmRotations == 0) ? true : false;
    }
    /**
     * Move a specific block in this shape
     * @param Block the block to move
     * @param amX The amount to move on the X axes
     * @param amY The amount to move on the Y axes
     */
    protected void moveBlock(TetrisBlock block, int amX, int amY){
    	block.setInt(0, block.getInt(0) + amX);
    	block.setInt(1, block.getInt(1) + amY);
    }
    /**
     * Move the shape on the X axes
     * @param amX The amount to move on the X axes
     */
    public void moveShapeX(int amX){
    	for(TetrisBlock B: shapeBlocks){
    		B.setInt(0, B.getInt(0) + amX);
    	}
    }
    /**
     * Move the shape on the Y axes
     * @param amY The amount ot move on the Y axes
     */
    public void moveShapeY(int amY){
    	for(TetrisBlock B: shapeBlocks){
    		B.setInt(1, B.getInt(1) + amY);
    	}
    }

    /**
     * Get a list of all the {@linkplain TetrisBlock}s for this shape
     * @return the list
     */
	public List<TetrisBlock> getBlocks(){
		return shapeBlocks;
	}
	/**
	 * Each shape has one block that never changes position when it rotates. This
	 * function returns that block
	 * @return The block that never rotates
	 */
	public TetrisBlock getStaticBlock(){
		return shapeBlocks.get(staticCellIndex);
	}

	/**
	 * Get the left most block in this shape
	 */
	public TetrisBlock getLeftMostBlock(){
    	TetrisBlock T = null;
        for (TetrisBlock B: shapeBlocks){
            if (T == null || T.getInt(0) > B.getInt(0))
                T = B;
        }
        return T;
    }
	/**
	 * Get the left most block in this shape for the given row
	 * @param y The specified row to look at
	 */
	public TetrisBlock getLeftMostBlock(int y){
    	TetrisBlock T = null;
        for (TetrisBlock B: shapeBlocks){
            if (B.getInt(1) == y && (T == null || T.getInt(0) > B.getInt(0)))
                T = B;
        }
        return T;
    }
    /**
     * @return A shape that only consists of the left most blocks for each row the current shape occupies.
     */
	public List<TetrisBlock> getLeftBlocks(){
    	List<TetrisBlock> blocks = new ArrayList<TetrisBlock>();
    	int topY = getTopMostBlock().getInt(1);
    	int bottomY = getBottomMostBlock().getInt(1);
    	for(int y = topY; y <= bottomY; ++y){
    		blocks.add(getLeftMostBlock(y));
    	}
    	return blocks;
	}
	/**
	 * Get the right most block in this shape
	 */
    public TetrisBlock getRightMostBlock(){
    	TetrisBlock T = null;
    	for (TetrisBlock B: shapeBlocks){
            if (T == null || T.getInt(0) < B.getInt(0))
                T = B;
        }
        return T;
    }
	/**
	 * Get the bottom most block in this shape
	 */
    public TetrisBlock getBottomMostBlock(){
    	TetrisBlock T = null;
    	for (TetrisBlock B: shapeBlocks){
            if (T == null || T.getInt(1) < B.getInt(1))
                T = B;
        }
        return T;
    }
    /**
     * Return the lowest block in a specific column
     * @param x The given column to search in
     * @return The lowest block in that column
     */
    public TetrisBlock getBottomMostBlock(int x){
    	TetrisBlock T = null;
    	for (TetrisBlock B: shapeBlocks){
            if (B.getInt(0) == x && (T == null || T.getInt(1) < B.getInt(1)))
                T = B;
        }
        return T;
    }
    /**
     * @return A shape that only consists of the lowest blocks for each column the current shape occupies.
     */
    public List<TetrisBlock> getBottomBlocks(){
    	List<TetrisBlock> blocks = new ArrayList<TetrisBlock>();
    	int leftX = getLeftMostBlock().getInt(0);
    	int rightX = getRightMostBlock().getInt(0);
    	for(int x = leftX; x <= rightX; ++x){
    		blocks.add(getBottomMostBlock(x));
    	}
    	return blocks;
    }
	/**
	 * Get the top most block in this shape
	 */
    public TetrisBlock getTopMostBlock(){
    	TetrisBlock T = null;
    	for (TetrisBlock B: shapeBlocks){
            if (T == null || T.getInt(1) > B.getInt(1))
                T = B;
        }
        return T;
    }

    /**
     * Set this shape at the top of the grid
     */
    public void setShapeTopGrid(){
        int YDist = getTopMostBlock().getInt(1);
        for(TetrisBlock B: shapeBlocks){
            B.setInt(1, B.getInt(1) - YDist);
        }
    }
    /**
     * Count all the Blocks that are in the given row
     * @param y the given row
     * @return the number of hits
     */
    public int countCellsInRow(int y){
    	int count = 0;
    	for (TetrisBlock B: shapeBlocks){
    		if((B.getInt(1) == y))
    			++count;
    	}
    	return count;
    }

    /**
     * Check if there are any Blocks in the specified cell
     * @param x the X coord
     * @param y the Y coord
     * @return wheather or not any Blocks are in that Cell
     */
    public boolean isInCell(int x, int y){
    	if(x == -1 && y == -1)
    		return false;
    	for (TetrisBlock B: shapeBlocks){
    		if((x == -1 || B.getInt(0) == x) && (y == -1 || B.getInt(1) == y))
    			return true;
    	}
    	return false;
    }
    public TetrisBlock getBlock(int x, int y){
    	for (TetrisBlock B: shapeBlocks){
    		if(B.getInt(0) == x && B.getInt(1) == y)
    			return B;
    	}
    	return null;
    }

	public int getCurrentOrientation() {
		return currentOrientation;
	}
}
