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
package net.sourceforge.cilib.games.result;

/**
 * @author leo
 * One of the agents won the game
 */
//TODO Teams, multiple winners
public class WinGameResult extends AbstractGameResult {
    private static final long serialVersionUID = 2073705770597355550L;
    int winPlayerID;

    public WinGameResult(int playerNo) {
        this.winPlayerID = playerNo;
    }

    public WinGameResult(WinGameResult other){
        winPlayerID = other.winPlayerID;
    }

    public int getWinnerID(){
        return winPlayerID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WinGameResult getClone() {
        return new WinGameResult(this);
    }

}
