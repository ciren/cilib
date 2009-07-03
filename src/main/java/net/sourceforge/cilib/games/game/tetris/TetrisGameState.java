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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.games.game.tetris.shape.AbstractShape;
import net.sourceforge.cilib.games.game.tetris.shape.BoxShape;
import net.sourceforge.cilib.games.game.tetris.shape.LShape;
import net.sourceforge.cilib.games.game.tetris.shape.LineShape;
import net.sourceforge.cilib.games.game.tetris.shape.RLShape;
import net.sourceforge.cilib.games.game.tetris.shape.RZigZagShape;
import net.sourceforge.cilib.games.game.tetris.shape.TShape;
import net.sourceforge.cilib.games.game.tetris.shape.ZigZagShape;
import net.sourceforge.cilib.games.items.GameItem;
import net.sourceforge.cilib.games.items.GridLocation;
import net.sourceforge.cilib.games.states.GridGameState;
/**
 * This class represents a Tetris game state.
 *
 * @author leo
 *
 */
public class TetrisGameState extends GridGameState {
	private static final long serialVersionUID = -6789845392240968003L;
	private AbstractShape currentShape;
	private double currentSpeed;
	private int currentLevel;
	private double currentScore;
	private int totalRowsCleared;
	public TetrisGameState() {
		currentShape = null;
		currentSpeed = 0;
		currentLevel = 0;
		currentScore = 0;
		totalRowsCleared = 0;
	}

	public TetrisGameState(TetrisGameState other) {
		super(other);
		if(other.currentShape != null)
			currentShape = other.currentShape.getClone();
		currentSpeed = other.currentSpeed;
		currentLevel = other.currentLevel;
		currentScore = other.currentScore;
		totalRowsCleared = other.totalRowsCleared;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TetrisGameState getClone() {
		return new TetrisGameState(this);
	}
	/**
	 * Create a new random Tetris shape at the top of the grid
	 */
	public void createNewShape(){
		currentShape = null;
        int NShape = randomizer.getGenerator().nextInt(7);
        switch (NShape)
        {
            case 0:
                currentShape = new BoxShape(getGridWidth(),getGridHeight());
                break;
            case 1:
                currentShape = new LineShape(getGridWidth(),getGridHeight());
                break;
            case 2:
                currentShape = new RZigZagShape(getGridWidth(),getGridHeight());
                break;
            case 3:
                currentShape = new ZigZagShape(getGridWidth(),getGridHeight());
                break;
            case 4:
                currentShape = new LShape(getGridWidth(),getGridHeight());
                break;
            case 5:
                currentShape = new RLShape(getGridWidth(),getGridHeight());
                break;
            case 6:
                currentShape = new TShape(getGridWidth(),getGridHeight());
                break;
        }

        if (currentShapeCollides()){
            //start and collides game over
            currentShape = null;
        }
	}
	/**
	 * Merge the given {@linkplain AbstractShape} into the current grid
	 * @param shape the shape to merge
	 */
	private void mergeShape(AbstractShape shape){
		if(shape != null){
			List<TetrisBlock> shapeBlocks = shape.getBlocks();
	        for(TetrisBlock B: shapeBlocks){
	        	GridLocation itemLoc = (GridLocation)B.getLocation();
	        	setItem(itemLoc.getInt(0), itemLoc.getInt(1), B.getClone());
	        }
		}
	}

	@Override
    public void setItem(int i, int j, GameItem item){
		if(state[i][j] != null)
			throw new RuntimeException("This cell is already occupied: " + i + ", " + j);
        state[i][j] = item;
    }
	/**
	 * Merge the current {@linkplain AbstractShape} into the grid
	 *
	 */
	public void mergeCurrentShape(){
		mergeShape(currentShape);
		currentShape = null;
	}

	/**
	 * Calculate the score based on the amount of rows cleared
	 * @param rowsCleared
	 */
    public void calculateScore(int rowsCleared){
        if (rowsCleared > 0)
        {
            totalRowsCleared += rowsCleared;
            int earnedLevel = 0;
            if (totalRowsCleared <= 0)
            {
                earnedLevel = 1;
            }
            else if ((totalRowsCleared >= 1) && (totalRowsCleared <= 90))
            {
                earnedLevel = 1 + (((int)totalRowsCleared - 1) / 10);
            }
            else if (totalRowsCleared >= 91)
            {
                earnedLevel = 10;
            }

            if (currentLevel < earnedLevel)
            {
            	currentLevel++;
            }

            int pointAward = ((21 + (3 * (int)currentLevel))); //- freefallIterations);
            currentScore += pointAward;
        }
    }
    /**
     * Get the number of blocks form the current {@linkplain AbstractShape} that will be removed if the
     * shape was to be merged at its current position
     * @return the number of eroded blocks
     */
    public int getErodedShapeCount(){
    	if(currentShape == null)
    		return 0;
    	int erodedShapeCount = 0;
        for (int y = getGridHeight() - 1; y >= 0; --y)
        {
            boolean fullRow = true;
            for (int x = 0; x < getGridWidth(); ++x)
            {
                if (state[x][y] == null && !currentShape.isInCell(x, y))
                {
                	fullRow = false;
                    break;
                }
            }
            if (fullRow)
            {
            	erodedShapeCount += currentShape.countCellsInRow(y);
            }
        }
        return erodedShapeCount;
    }
    /**
     * Get the number of empty cells in the grid before an occupied one
     * @param x the row to look in
     * @param startY the column to start looking at
     * @return the number of empty cells
     */
    public int getEmptyCellsBeforeOccupied(int x, int startY){
    	int count = 0;
    	for(int y = startY; y < getGridHeight(); ++y){
    		if(getItem(x, y) == null){
    			++count;
    		}else{
    			return count;
    		}
    	}
    	return count;
    }

    /**
     * Clear all full rows for a given game grid
     * @param state the grid of blocks
     * @param Width the with of the grid
     * @param Height the heigth of the grid
     * @return the amount of rows cleared.
     */
	public static int clearFullRows(GameItem state[][], int Width, int Height){
		int rowsCleared = 0;
        List<int[]> changeKeys = new ArrayList<int[]>();
        for (int y = Height - 1; y >= 0; --y)
        {
            boolean FullRow = true;
            for (int x = 0; x < Width; ++x)
            {
                if (state[x][y] == null)
                {
                    FullRow = false;
                    break;
                }
            }
            if (FullRow)
            {

                ++rowsCleared;
                for (int x = 0; x < Width; ++x)
                {

                    for (int yUp = y - 1; yUp >= 0; --yUp)
                    {

                        if (state[x][yUp] != null)
                        {
                        	((TetrisBlock)state[x][yUp]).addMoveDown();
                        	int [] pos = new int[2];
                        	pos[0] = x;
                        	pos[1] = yUp;
                            changeKeys.add(pos);
                        }
                    }

                    state[x][y] = null;
                }
            }
        }

        List<TetrisBlock> changeBlocks = new ArrayList<TetrisBlock>();

        for (int [] key: changeKeys)
        {
            if(state[key[0]][key[1]] != null)
            {
            	TetrisBlock B = (TetrisBlock)state[key[0]][key[1]];
                B.updateMovesDown();
                if (((GridLocation)(B.getLocation())).getInt(1) != key[1])
                {
                    changeBlocks.add((TetrisBlock)B.getClone());
                    state[key[0]][key[1]] = null;
                }
            }
        }
        //update all new ones
        for (TetrisBlock change: changeBlocks)
        {
        	GridLocation loc = (GridLocation)change.getLocation();
        	state[loc.getInt(0)][loc.getInt(1)] = change;
        }
		return rowsCleared;

	}

	/**
	 * Clear any full rows in the current game grid.
	 * @return
	 */
	public int clearFullRows(){
		return TetrisGameState.clearFullRows(state, getGridWidth(), getGridHeight());
	}

	/**
	 * Set the current {@linkplain AbstractShape} as far to the left of the current grid as it can go
	 *
	 */
    public boolean setShapeLeftGrid(){
    	currentShape.moveShapeY(1); //set the block to one row below the top, so that rotations do not go outside the border
    	if(currentShapeCollides())
    		return false;
    	int left = currentShape.getLeftMostBlock().getInt(0);
    	List<TetrisBlock> subShape = currentShape.getLeftBlocks();
    	int x = 0;
    	boolean collides = collides(subShape);
    	while(!collides && (left - x) > 0){
    		++x;
        	for(TetrisBlock B: subShape)
        		if(B.getInt(1) >= 0 && state[B.getInt(0) - x][B.getInt(1)] != null)
        			collides = true;
    	}
    	if(collides && x > 0)
    		--x;
    	if(x > 0)
    		currentShape.moveShapeX(-x);

    	return !currentShapeCollides();
    }
    /**
     * Fit the current shape in the current grid from its current position moving down untill it either collides
     * or is at the bottowm of the grid
     * @return
     */
    public boolean FitShapeInGridTopDown()
    {
    	int bottom = currentShape.getBottomMostBlock().getInt(1);
    	List<TetrisBlock> subShape = currentShape.getBottomBlocks();
    	int y = 0;
        boolean collides = collides(subShape);
        while (!collides && (bottom + y) <= getGridHeight() - 2){
        	++y;
        	for(TetrisBlock B: subShape)
        		if(state[B.getInt(0)][B.getInt(1) + y] != null)
        			collides = true;
        }
    	if(collides && y > 0)
    		--y;
    	if(y > 0){
	        currentShape.moveShapeY(y);
    	}
    	collides = currentShapeCollides();
        if(!collides && currentShape.getTopMostBlock().getInt(1) >= 0)
        	return true;
        else
        	return false;
    }
    /**
     * Move the current piece one position to the right if it doesn't collide there.
     * @return
     */
    public boolean moveRight(){
    	TetrisBlock right = currentShape.getRightMostBlock();
    	if(right.getInt(0) == getGridWidth() - 1){
    		return false;
    	}
    	currentShape.moveShapeX(1);

        return !currentShapeCollides();
    }
	/**
	 * Check whether one of the blocks in the current shape of the game collides in the grid
	 * @return A boolean that indicates if a collision occurred
	 */
    public boolean currentShapeCollides(){
    	return this.collides(currentShape.getBlocks());
    }
	/**
	 * Check whether one of the blocks in the {@link AbstractShape} collides in the current grid
	 * @param blocks The given {@link AbstractShape}
	 * @return A boolean that indicates if a collision occurred
	 */
    public boolean collides(AbstractShape shape){
    	return this.collides(shape.getBlocks());
    }
	/**
	 * Check whether one of the blocks in the list collides in the current grid
	 * @param blocks A list of {@link TetrisBlock}s
	 * @return A boolean that indicates if a collision occurred
	 */
    public boolean collides(List<TetrisBlock> blocks){
		for(TetrisBlock B: blocks){
			GridLocation loc = (GridLocation)B.getLocation();
			try{
				if(state[loc.getInt(0)][loc.getInt(1)] != null) //if there is a block then this one collides with it!
					return true;
			}
			catch(Exception e){
				throw new RuntimeException(e.getMessage() + ": x: " + loc.getInt(0) + ", y: " + loc.getInt(1));
			}
		}
		return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearState(){
    	super.clearState();
		currentSpeed = 0;
		currentLevel = 1;
		currentScore = 0;
		totalRowsCleared = 0;
		createNewShape();
    }

	public int getCurrentLevel() {
		return currentLevel;
	}

	public double getCurrentScore() {
		return currentScore;
	}

	public AbstractShape getCurrentShape() {
		return currentShape == null ? null : currentShape;
	}
	public void setCurrentShape(AbstractShape newShape) {
		currentShape = newShape;
	}
	public double getCurrentSpeed() {
		return currentSpeed;
	}

	public int getTotalRowsCleared() {
		return totalRowsCleared;
	}

}
