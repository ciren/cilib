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

import net.sourceforge.cilib.games.agent.state.evaluation.StateEvaluator;
import net.sourceforge.cilib.games.game.Game;
import net.sourceforge.cilib.games.states.GameState;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Type;

/**
 * @author leo
 * This is an implimentation of the Tetris {@linkplain StateEvaluator} that was created by Pierre Dellacherie in 2003. The implimentation was adapted
 * from code downloaded from http://colinfahey.com/tetris/tetris_en.html
 */
public class PierreDellacherieTetrisEvaluator implements StateEvaluator {
	public PierreDellacherieTetrisEvaluator() {
	}

	/**
	 * {@inheritDoc}
	 */
	public double evaluateState(Game<GameState> state, int decisionPlayerID) {

		TetrisGameState stateData = (TetrisGameState)state.getCurrentState().getClone();
		int Width = stateData.getGridWidth();
		int Height = stateData.getGridHeight();
		double landingHeight = 0.5 * ((stateData.getCurrentShape().getBottomMostBlock().getInt(1) + 1) + (stateData.getCurrentShape().getTopMostBlock().getInt(1) + 1));
		int erodedPieceCount = stateData.getErodedShapeCount();
		int shapeX = stateData.getCurrentShape().getStaticBlock().getInt(0);
		int shapeRotation = stateData.getCurrentShape().getCurrentOrientation();
		stateData.mergeCurrentShape();
        int amLinesRemoved = stateData.clearFullRows();
        erodedPieceCount *= amLinesRemoved;
        int amHoleCells = 0;
        int wellCount = 0;

        int rowTransitions = 0;
        int columnTransitions = 0;
        boolean[] lastXOcc = new boolean[Height];
        for (int x = 0; x < Width; ++x)
        {
            int amEmpty = 0;
         //   int ColumWellHeight = 0;
            boolean LastYOccupied = true; //below grid counts as occupied

            for (int y = Height - 1; y >= 0; --y) //Iterate from bottom of grid to top. 0 is the top position
            {
                if (stateData.getItem(x, y) != null) //If this cell is occupied
                {

                    amHoleCells += amEmpty;

                    amEmpty = 0;
                    //ColumWellHeight = 0;

                    if (!LastYOccupied) //this cell is occupied, if the previous one was not
                        ++columnTransitions;
                    LastYOccupied = true;

                    if(x != 0 && !lastXOcc[y]){ //left and rightmost are occ
                    	++rowTransitions;
                    }

                    lastXOcc[y] = true;
                }
                else //not occupied
                {
                    if ((x == 0 || stateData.getItem(x - 1, y) != null)
                            && (x == (Width - 1) || stateData.getItem(x + 1, y) != null))
                    {
                        //i am empty, left is not empty, right is not empty
                        wellCount += stateData.getEmptyCellsBeforeOccupied(x, y);
                    }

                    ++amEmpty;

                    if (LastYOccupied) //this cell is not occupied, if the precious one was
                        ++columnTransitions;

                    LastYOccupied = false;

                    if (x == 0 || x == Width - 1 || lastXOcc[y]){ //left and rightmost are occ
                    	if(x == Width - 1 && lastXOcc[y])
                    		rowTransitions += 2;
                    	else
                    		++rowTransitions;
                    }

                    lastXOcc[y] = false;
                }
            }
            //above grid is not occupied
            if (LastYOccupied)
                ++columnTransitions;

        }

        double rating = 0;
        rating += ((-1.0) * (stateData.getGridHeight() -  (landingHeight - 1)));
        rating += ((1.0) * ((double)(erodedPieceCount)));
        rating += ((-1.0) * ((double)(rowTransitions)));
        rating += ((-1.0) * ((double)(columnTransitions)));
        rating += ((-4.0) * ((double)(amHoleCells)));
        rating += ((-1.0) * ((double)(wellCount)));
        /*TetrisMain.features.clear();
        TetrisMain.features.add((stateData.getGridHeight() -  (landingHeight - 1)));
        TetrisMain.features.add((double)erodedPieceCount);
        TetrisMain.features.add((double)rowTransitions);
        TetrisMain.features.add((double)columnTransitions);
        TetrisMain.features.add((double)amHoleCells);
        TetrisMain.features.add((double)wellCount);*/
   //     System.out.println("Landing Height: " + (stateData.getGridHeight() -  landingHeight));
     //   System.out.println("erordedPieceCount: " + erodedPieceCount);
       // System.out.println("Row Trans: " + rowTransitions);
   //     System.out.println("Column Trans: " + columnTransitions);
     //   System.out.println("Hole Count: " + amHoleCells);
     //   System.out.println("Well Count: " + wellCount);
       // System.out.println("Rating: " + rating);


        int startX = (Width / 2) + 1;
        int absoluteDistanceX = (shapeX + 1) - startX;
        if(absoluteDistanceX < 0)
        	absoluteDistanceX *= -1.0;

        int priority = (100 * absoluteDistanceX);
        if(shapeX < startX){
        	priority += 10;
        }
        priority -= shapeRotation;
       // TetrisMain.features.add(rating);
        //TetrisMain.features.add((double)priority);
        //System.out.println(rating + ", " + ((double)priority / 10000.0) + ", " + (rating + ((double)priority / 1000.0)));
        //TetrisMain.features.add(rating + ((double)priority / 10000.0));
        return rating + ((double)priority / 10000.0);
	}

	/**
	 * {@inheritDoc}
	 */
	public DomainRegistry getEvaluatorDomain() {
		throw new RuntimeException("This state evaluator cannot be optimised");
	}

	/**
	 * {@inheritDoc}
	 */
	public void initializeEvaluator(Type evaluatorData) {
		//nothing to initialize here, move along
	}

}
