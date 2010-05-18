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
 * This class represents the reverse L shape
 * @author leo
 *
 */
public class RLShape extends AbstractShape {
	private static final long serialVersionUID = 4210740743238072697L;

	public RLShape(int gridWidth, int gridHeight) {
		super();
		staticCellIndex = 1;
		GridLocation block = new GridLocation(gridWidth, gridHeight);
		block.setInt(0, (gridWidth / 2) - 1);
		block.setInt(1, 0);
		shapeBlocks.add(new TetrisBlock(GameToken.Tetris.RLSHAPE, block.getClone()));
		block.setInt(0, (gridWidth / 2));
		block.setInt(1, 0);
		shapeBlocks.add(new TetrisBlock(GameToken.Tetris.RLSHAPE, block.getClone()));
		block.setInt(0, (gridWidth / 2) + 1);
		block.setInt(1, 0);
		shapeBlocks.add(new TetrisBlock(GameToken.Tetris.RLSHAPE, block.getClone()));
		block.setInt(0, (gridWidth / 2) + 1);
		block.setInt(1, 1);
		shapeBlocks.add(new TetrisBlock(GameToken.Tetris.RLSHAPE, block.getClone()));
	}

	/**
	 * @param other
	 */
	public RLShape(AbstractShape other) {
		super(other);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RLShape getClone() {
		return new RLShape(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rotate() {
        switch (currentOrientation)
        {
            case 0:
                moveBlock(shapeBlocks.get(0), 1, 1);
                moveBlock(shapeBlocks.get(2), -1, -1);
                moveBlock(shapeBlocks.get(3), 0, -2);

                ++currentOrientation;
                break;
            case 1:
                moveBlock(shapeBlocks.get(0), 1, -1);
                moveBlock(shapeBlocks.get(2), -1, 1);
                moveBlock(shapeBlocks.get(3), -2, 0);

                ++currentOrientation;
                break;
            case 2:
                moveBlock(shapeBlocks.get(0), -1, -1);
                moveBlock(shapeBlocks.get(2), 1, 1);
                moveBlock(shapeBlocks.get(3), 0, 2);

                ++currentOrientation;
                break;
            case 3:
                moveBlock(shapeBlocks.get(0), -1, 1);
                moveBlock(shapeBlocks.get(2), 1, -1);
                moveBlock(shapeBlocks.get(3), 2, 0);

                currentOrientation = 0;
                break;
        }
	}

}
