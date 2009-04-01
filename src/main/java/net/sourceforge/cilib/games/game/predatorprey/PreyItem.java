/*
 * Copyright (C) 2003 - 2008
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
package net.sourceforge.cilib.games.game.predatorprey;

import net.sourceforge.cilib.games.items.GridLocation;
import net.sourceforge.cilib.games.items.PlayerItem;

/**
 * @author leo
 *
 */
public class PreyItem extends PlayerItem {

    /**
     *
     */
    private static final long serialVersionUID = -4634790531299172443L;

    /**
     * @param playerNo
     */
    public PreyItem(int playerNo, int gridWidth, int gridHeight) {
        super(playerNo);
        // TODO Auto-generated constructor stub
        itemLocation = new GridLocation(2, gridWidth, gridHeight);
    }

    /**
     * @param Other
     */
    public PreyItem(PlayerItem Other) {
        super(Other);
        // TODO Auto-generated constructor stub
    }

}
