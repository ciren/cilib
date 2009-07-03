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

import net.sourceforge.cilib.games.game.tetris.TetrisBlock;
import net.sourceforge.cilib.games.items.GameToken;
import net.sourceforge.cilib.games.items.GridLocation;
/**
 * This class represents the L shape
 * @author leo
 *
 */
public class LShape extends AbstractShape {

	private static final long serialVersionUID = -3497749852161198188L;

	public LShape(int gridWidth, int gridHeight) {
		super();
		staticCellIndex = 1;
		GridLocation block = new GridLocation(gridWidth, gridHeight);
		block.setInt(0, (int)(gridWidth / 2) + 1);
		block.setInt(1, 0);
		shapeBlocks.add(new TetrisBlock(GameToken.Tetris.LSHAPE, block.getClone()));
		block.setInt(0, (int)(gridWidth / 2));
		block.setInt(1, 0);
		shapeBlocks.add(new TetrisBlock(GameToken.Tetris.LSHAPE, block.getClone()));
		block.setInt(0, (int)(gridWidth / 2) - 1);
		block.setInt(1, 0);
		shapeBlocks.add(new TetrisBlock(GameToken.Tetris.LSHAPE, block.getClone()));
		block.setInt(0, (int)(gridWidth / 2) - 1);
		block.setInt(1, 1);
		shapeBlocks.add(new TetrisBlock(GameToken.Tetris.LSHAPE, block.getClone()));
	}

	public LShape(LShape other){
		super(other);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbstractShape getClone() {
		return new LShape(this);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rotate() {
        //rotate the block
        switch (currentOrientation)
        {
            case 0:
            	moveBlock(shapeBlocks.get(0), -1, -1);
                moveBlock(shapeBlocks.get(2), 1, 1);
                moveBlock(shapeBlocks.get(3), 2, 0);


                ++currentOrientation;
                break;
            case 1:
            	 moveBlock(shapeBlocks.get(0), -1, 1);
                moveBlock(shapeBlocks.get(2), 1, -1);
                moveBlock(shapeBlocks.get(3), 0, -2);


                ++currentOrientation;
                break;
            case 2:
                moveBlock(shapeBlocks.get(0), 1, 1);
                moveBlock(shapeBlocks.get(2), -1, -1);
                moveBlock(shapeBlocks.get(3), -2, 0);

                ++currentOrientation;
                break;
            case 3:
                moveBlock(shapeBlocks.get(0), 1, -1);
                moveBlock(shapeBlocks.get(2), -1, 1);
                moveBlock(shapeBlocks.get(3), 0, 2);

                currentOrientation = 0;
                break;
        }
	}

}
