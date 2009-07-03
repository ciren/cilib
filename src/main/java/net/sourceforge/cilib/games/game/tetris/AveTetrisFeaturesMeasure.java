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

import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Long;
import net.sourceforge.cilib.games.game.Game;
import net.sourceforge.cilib.games.measurement.AgentMeasure;
import net.sourceforge.cilib.games.states.GameState;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @author leo
 * This is an {@linkplain AgentMeasure} that records certain Tetris features after every piece has been placed.
 */
public class AveTetrisFeaturesMeasure extends AgentMeasure {
	/**
	 *
	 */
	private static final long serialVersionUID = -6733754320928755214L;
	int counter;
	long holesCount;
	long rowTransitionsCount;
	long columntransitionsCount;
	long heightCount;
	/**
	 *
	 */
	public AveTetrisFeaturesMeasure() {
		/*holesCount = new ArrayList<Integer>();
		rowTransitionsCount = new ArrayList<Integer>();
		columntransitionsCount = new ArrayList<Integer>();
		heightCount = new ArrayList<Integer>();*/
		counter = 0;
		holesCount = 0;
		rowTransitionsCount = 0;
		columntransitionsCount = 0;
		heightCount = 0;
	}

	/**
	 * Copy constructor
	 * @param other
	 */
	public AveTetrisFeaturesMeasure(AveTetrisFeaturesMeasure other) {
		super(other);
		counter = other.counter;
		holesCount = other.holesCount;
		rowTransitionsCount = other.rowTransitionsCount;
		columntransitionsCount = other.columntransitionsCount;
		heightCount = other.heightCount;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clearData() {
		counter = 0;
		holesCount = 0;
		rowTransitionsCount = 0;
		columntransitionsCount = 0;
		heightCount = 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AgentMeasure getClone() {
		return new AveTetrisFeaturesMeasure(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Type getMeasuredData() {
		Vector data = new Vector(5);
		data.add(new Int(counter));
		data.add(new Long(holesCount));
		data.add(new Long(rowTransitionsCount));
		data.add(new Long(columntransitionsCount));
		data.add(new Long(heightCount));
		return data;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void measure(Game<GameState> game) {
		TetrisGameState state = null;
		try{
			state = (TetrisGameState)game.getCurrentState();
		}catch(Exception e){
			throw new RuntimeException("This measurement can only be used on Tetris");
		}
		int Width = state.getGridWidth();
		int Height = state.getGridHeight();

		 int highestYCell = Height; //low y is high cell
         int amHoleCells = 0;

         int rowTransitions = 0;
         int columnTransitions = 0;

         boolean[] lastXOcc = new boolean[highestYCell];

         for (int x = 0; x < Width; ++x)
         {
             int amEmpty = 0;
             boolean lastYOccupied = true;
             for (int y = Height - 1; y >= 0; --y)
             {
                 if (state.getItem(x, y) != null)
                 {
                     if (y < highestYCell)
                         highestYCell = y;

                     amHoleCells += amEmpty;
                     amEmpty = 0;

                     if (!lastYOccupied) //this cell is occupied, if the precious one was not
                         ++columnTransitions;
                     lastYOccupied = true;

                     if(x != 0 && !lastXOcc[y]){ //left and rightmost are occ
                     	++rowTransitions;
                     }


                     lastXOcc[y] = true;
                 }
                 else
                 {
                     ++amEmpty;

                     if (lastYOccupied) //this cell is not occupied, if the precious one was
                         ++columnTransitions;

                     lastYOccupied = false;

                     if (x == 0 || x == Width - 1 || lastXOcc[y]){ //left and rightmost are occ
                     	if(x == Width - 1 && lastXOcc[y])
                     		rowTransitions += 2;
                     	else
                     		++rowTransitions;
                     }

                     lastXOcc[y] = false;
                 }
             }
             //outside cell counts as not occupied
             if (lastYOccupied)
                 ++columnTransitions;
         }
         ++counter;
         holesCount += amHoleCells;
         rowTransitionsCount += rowTransitions;
         columntransitionsCount += columnTransitions;
         heightCount += (Height- highestYCell);
	}
}
