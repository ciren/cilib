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
package net.sourceforge.cilib.games.items;

/**
 * any game item controlled by a player
 * @author leo
 */
public class PlayerItem extends GameItem {
    private static final long serialVersionUID = -674781677901305287L;
    int playerNo;

    public PlayerItem(int playerNo) {
        this.playerNo = playerNo;
    }

    public PlayerItem(int playerNo, Enum<?> token) {
        super(token);
        this.playerNo = playerNo;
    }

    public PlayerItem(PlayerItem Other) {
        super(Other);
        playerNo = Other.playerNo;
    }

    public int getPlayerID() {
        return playerNo;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public GameItem getClone() {
        return new PlayerItem(this);
    }

}
