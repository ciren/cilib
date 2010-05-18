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
