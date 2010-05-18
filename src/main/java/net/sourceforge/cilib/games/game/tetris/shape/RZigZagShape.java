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
package net.sourceforge.cilib.games.game.tetris.shape;

import net.sourceforge.cilib.games.game.tetris.TetrisBlock;
import net.sourceforge.cilib.games.items.GameToken;
import net.sourceforge.cilib.games.items.GridLocation;

/**
 * This class represents the Reverse ZigZag shape
 * @author leo
 *
 */
public class RZigZagShape extends AbstractShape {

	private static final long serialVersionUID = -3094608599864790701L;

	public RZigZagShape(int gridWidth, int gridHeight) {
		super();
		staticCellIndex = 0;
		GridLocation block = new GridLocation(gridWidth, gridHeight);
		block.setInt(0, (gridWidth / 2));
		block.setInt(1, 0);
		shapeBlocks.add(new TetrisBlock(GameToken.Tetris.RZIGZAG, block.getClone()));
		block.setInt(0, (gridWidth / 2) + 1);
		block.setInt(1, 0);
		shapeBlocks.add(new TetrisBlock(GameToken.Tetris.RZIGZAG, block.getClone()));
		block.setInt(0, (gridWidth / 2) - 1);
		block.setInt(1, 1);
		shapeBlocks.add(new TetrisBlock(GameToken.Tetris.RZIGZAG, block.getClone()));
		block.setInt(0, (gridWidth / 2));
		block.setInt(1, 1);
		shapeBlocks.add(new TetrisBlock(GameToken.Tetris.RZIGZAG, block.getClone()));

	}

	/**
	 * @param other
	 */
	public RZigZagShape(RZigZagShape other) {
		super(other);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RZigZagShape getClone() {
		return new RZigZagShape(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rotate() {
        switch (currentOrientation)
        {
            case 0:
                moveBlock(shapeBlocks.get(1), -1, -1);
                moveBlock(shapeBlocks.get(2), 2, 0);
                moveBlock(shapeBlocks.get(3), 1, -1);

                ++currentOrientation;
                break;
            case 1:
                moveBlock(shapeBlocks.get(1), 1, 1);
                moveBlock(shapeBlocks.get(2), -2, 0);
                moveBlock(shapeBlocks.get(3), -1, 1);

                --currentOrientation;
                break;
        }

	}


}
