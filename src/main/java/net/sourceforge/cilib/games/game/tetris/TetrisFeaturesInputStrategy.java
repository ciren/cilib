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

import net.sourceforge.cilib.games.agent.NeuralAgent;
import net.sourceforge.cilib.games.agent.neural.NeuralStateInputStrategy;
import net.sourceforge.cilib.games.game.Game;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This is a {@linkplain NeuralStateInputStrategy} that gives certain specified features of a tetris game state as input to the Neural Network.
 * These feautures were taken from REFERENCE!
 * Others from REFERENCE
 * @author leo
 *
 */
public class TetrisFeaturesInputStrategy extends NeuralStateInputStrategy {
    boolean removedLines;
    boolean erodedShapeBlocks;
    boolean pileHeight;
    boolean holes;
    boolean landingHeight;
    boolean noBlocks;
    boolean maxWellDepth;
    boolean sumWells;
    boolean altitudeDiff;
    boolean rowTransitions;
    boolean columnTransitions;
    boolean weightedBlocks;
    boolean connectedHoles;
	public TetrisFeaturesInputStrategy() {
	    removedLines =
	    pileHeight =
	    holes =
	    landingHeight =
	    noBlocks =
	    maxWellDepth =
	    sumWells =
	    altitudeDiff =
	    rowTransitions =
	    columnTransitions =
	    weightedBlocks =
	    connectedHoles =
	    erodedShapeBlocks = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int amountInputs() {
		return  (removedLines ? 1 : 0) +
				(pileHeight ? 1 : 0) +
				(holes ? 1 : 0) +
			    (landingHeight ? 1 : 0) +
			    (noBlocks ? 1 : 0) +
			    (maxWellDepth ? 1 : 0) +
			    (sumWells ? 1 : 0) +
			    (altitudeDiff ? 1 : 0) +
			    (rowTransitions ? 1 : 0) +
			    (columnTransitions ? 1 : 0) +
			    (weightedBlocks ? 1 : 0) +
			    (connectedHoles ? 1 : 0) +
			    (erodedShapeBlocks ? 1 : 0);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector getNeuralInputArray(NeuralAgent currentPlayer,
			Game state) {

		             //set as inputs the following criteria

            //1.Removed Lines: The number of lines that were cleared in the last step to get to the
            //current board.

            //2. Pile Height: The row of the highest occupied cell in the board.

            //3. Holes: The number of all unoccupied cells that have at least one occupied above them.

            //4.Landing Height (PD): The height at which the last tetramino has been placed.

            //5. Blocks (CF): Number of occupied cells on the board.

            //6. Maximum Well Depth: The depth of the deepest well (with a width of one) on the board.

            //7. Sum of all Wells (CF): Sum of all wells on the board. This is 7 in figure 1 (b).

            //8. Altitude Dierence: The dierence between the highest occupied and lowest free cell that are directly reachable from the top.


            //9. Row Transitions (PD): Sum of all horizontal occupied/unoccupied-transitions on the
            //board. The outside to the left and right counts as occupied.

            //10. Column Transitions (PD): As Row Transitions above, but counts vertical transitions.
            //The outside below the game-board is considered occupied.

            //11. Weighted Blocks (CF): Same as Blocks above, but blocks in row n count n-times as much
                //as blocks in row 1 (counting from bottom to top).

            //12. Connectd Holes: Same as Holes above, however vertically connected unoccupied cells
            //only count as one hole.
			TetrisGameState stateData = (TetrisGameState)state.getCurrentState().getClone();
			int Width = stateData.getGridWidth();
			int Height = stateData.getGridHeight();
			Vector input = new Vector(amountInputs());
			double landingHeight = 0.5 * ((stateData.getCurrentShape().getBottomMostBlock().getInt(1) + 1) + (stateData.getCurrentShape().getTopMostBlock().getInt(1) + 1));
			int erodedPieceCount = stateData.getErodedShapeCount();
			stateData.mergeCurrentShape();
            int amLinesRemoved = stateData.clearFullRows();
            erodedPieceCount *= amLinesRemoved;
            int HighestYCell = Height; //low y is high cell
            int amHoleCells = 0;
            int amHoles = 0;
            int maximumWellDepth = 0;
            int wellCount = 0;
            int lowestFreeCell = 0;

            int blockCount = 0;
            int rowTransitionsCount = 0;
            int columnTransitionsCount = 0;

            boolean[] lastXOcc = new boolean[HighestYCell];

            float weightedBlockCount = 0;

            float CellWeight = 0.1f;

            for (int x = 0; x < Width; ++x)
            {
                int amEmpty = 0;
                int ColumWellHeight = 0;
                int ColumnLowestFree = -1;
                boolean LastYOccupied = true; //below grid counts as occupied
                for (int y = Height - 1; y >= 0; --y)
                {
                    if (stateData.getItem(x, y) != null)
                    {
                        if (y < HighestYCell)
                            HighestYCell = y;

                        amHoleCells += amEmpty;
                        if (amEmpty > 0)
                            ++amHoles;

                        amEmpty = 0;
                        ColumWellHeight = 0;
                        ColumnLowestFree = -1;

                        if (!LastYOccupied) //this cell is occupied, if the precious one was not
                            ++columnTransitionsCount;
                        LastYOccupied = true;
                        if(x != 0 && !lastXOcc[y]){ //left and rightmost are occ
                        	++rowTransitionsCount;
                        }

                        lastXOcc[y] = true;

                        weightedBlockCount += (((Height - 1) - y) + 1) * CellWeight;
                        ++blockCount;
                    }
                    else
                    {
                        if ((x == 0 || stateData.getItem(x - 1, y) != null)
                                && (x == (Width - 1) || stateData.getItem(x + 1, y) != null))
                        {
                            //i am empty, left is not empty, right is not empty
                            ++ColumWellHeight;
                        }
                        if(ColumnLowestFree == -1)
                            ColumnLowestFree = y;

                        ++amEmpty;

                        if (LastYOccupied) //this cell is not occupied, if the precious one was
                            ++columnTransitionsCount;

                        LastYOccupied = false;

                        if (x == 0 || x == Width - 1 || lastXOcc[y]){ //left and rightmost are occ
                        	if(x == Width - 1 && lastXOcc[y])
                        		rowTransitionsCount += 2;
                        	else
                        		++rowTransitionsCount;
                        }

                        lastXOcc[y] = false;
                    }
                }
                //outside cell counts as not occupied
                if (LastYOccupied)
                    ++columnTransitionsCount;

                wellCount += ColumWellHeight;
                if (ColumWellHeight > maximumWellDepth)
                    maximumWellDepth = ColumWellHeight;

                if (ColumnLowestFree != -1
                    && ColumnLowestFree > lowestFreeCell)
                        lowestFreeCell = ColumnLowestFree;
            }

            float MaxWeightedSum = 0;
            if(weightedBlocks){
            	for (int i = 1; i <= Height; ++i)
            		MaxWeightedSum += ((Width * CellWeight) * (float)i);
            }

            if (removedLines){
                input.add(new Real(currentPlayer.getScaledInput(amLinesRemoved, 0, 4)));
            }
            if(erodedShapeBlocks){
            	input.add(new Real(currentPlayer.getScaledInput(erodedPieceCount, 0, 4 * 4)));
            }
            if (pileHeight){
                input.add(new Real(currentPlayer.getScaledInput((Height - 1) - HighestYCell, 0, Height - 1))); //highest block
            }
            if (this.landingHeight){
                input.add(new Real(currentPlayer.getScaledInput((Height - 1) - landingHeight, 0, Height - 1)));
            }
            if (altitudeDiff){
                input.add(new Real(currentPlayer.getScaledInput((((Height - 1) - HighestYCell) + 1) - ((Height - 1) - lowestFreeCell), 0, Height - 1))); //highest block
            }
            if (holes){
                input.add(new Real(currentPlayer.getScaledInput(amHoleCells, 0, (Height - 1) * Width)));
            }
            if (connectedHoles){
                input.add(new Real(currentPlayer.getScaledInput(amHoles, 0, Width * (Height / 2))));
            }

            if (maxWellDepth){
                input.add(new Real(currentPlayer.getScaledInput(maximumWellDepth, 0, Height)));
            }
            if (sumWells){
                input.add(new Real(currentPlayer.getScaledInput(wellCount, 0, Height * (Width / 2))));
            }
            if (rowTransitions){
                input.add(new Real(currentPlayer.getScaledInput(rowTransitionsCount, 0, Height * Width)));
            }
            if (columnTransitions){
                input.add(new Real(currentPlayer.getScaledInput(columnTransitionsCount, 0, Height * Width))); //highest block
            }
            if (noBlocks){
                input.add(new Real(currentPlayer.getScaledInput(blockCount, 0, Height * Width)));
            }
            if (weightedBlocks){
                input.add(new Real(currentPlayer.getScaledInput(weightedBlockCount, 0, MaxWeightedSum)));
            }
            return input;
	}

	public boolean isAltitudeDiff() {
		return altitudeDiff;
	}

	public void setAltitudeDiff(boolean altitudeDiff) {
		this.altitudeDiff = altitudeDiff;
	}

	public boolean isColumnTransisitons() {
		return columnTransitions;
	}

	public void setColumnTransisitons(boolean columnTransitions) {
		this.columnTransitions = columnTransitions;
	}

	public boolean isConnectedHoles() {
		return connectedHoles;
	}

	public void setConnectedHoles(boolean connectedHoles) {
		this.connectedHoles = connectedHoles;
	}

	public boolean isHoles() {
		return holes;
	}

	public void setHoles(boolean holes) {
		this.holes = holes;
	}

	public boolean isLandingHeight() {
		return landingHeight;
	}

	public void setLandingHeight(boolean landingHeight) {
		this.landingHeight = landingHeight;
	}

	public boolean isMaxWellDepth() {
		return maxWellDepth;
	}

	public void setMaxWellDepth(boolean maxWellDepth) {
		this.maxWellDepth = maxWellDepth;
	}

	public boolean isNoBlocks() {
		return noBlocks;
	}

	public void setNoBlocks(boolean noBlocks) {
		this.noBlocks = noBlocks;
	}

	public boolean isPileHeight() {
		return pileHeight;
	}

	public void setPileHeight(boolean pileHeight) {
		this.pileHeight = pileHeight;
	}

	public boolean isRemovedLines() {
		return removedLines;
	}

	public void setRemovedLines(boolean removedLines) {
		this.removedLines = removedLines;
	}

	public boolean isRowTransitions() {
		return rowTransitions;
	}

	public void setRowTransitions(boolean rowTransitions) {
		this.rowTransitions = rowTransitions;
	}

	public boolean isSumWells() {
		return sumWells;
	}

	public void setSumWells(boolean sumWells) {
		this.sumWells = sumWells;
	}

	public boolean isWeightedBlocks() {
		return weightedBlocks;
	}

	public void setWeightedBlocks(boolean weightedBlocks) {
		this.weightedBlocks = weightedBlocks;
	}

	public boolean isErodedShapeBlocks() {
		return erodedShapeBlocks;
	}

	public void setErodedShapeBlocks(boolean erodedShapeBlocks) {
		this.erodedShapeBlocks = erodedShapeBlocks;
	}

}
