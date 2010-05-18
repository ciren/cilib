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
package net.sourceforge.cilib.games.items;

/**
 * An object in the game that is located in a grid.
 * @author leo
 *
 */
public class GridItem extends PlayerItem {
    private static final long serialVersionUID = 4878803247778205144L;
    /**
     * @param playerNo
     */
    public GridItem(int playerNo, Enum<?> token, int gridWidth, int gridHeight) {
        super(playerNo, token);
        itemLocation = new GridLocation(gridWidth, gridHeight);
    }

    public GridItem(int playerNo, Enum<?> token, ItemLocation location) {
        super(playerNo, token);
        itemLocation = location;
    }

    public GridItem(int playerNo, Enum<?> token, int xMax, int yMax, int zMax) {
        super(playerNo, token);
        itemLocation = new GridLocation(xMax, yMax, zMax);
    }
    /**
     * Copy constructor
     * @param Other
     */
    public GridItem(GridItem Other) {
        super(Other);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public GridItem getClone() {
        return new GridItem(this);
    }
}
