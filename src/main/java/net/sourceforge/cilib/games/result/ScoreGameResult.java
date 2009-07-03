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
package net.sourceforge.cilib.games.result;

/**
 * @author leo
 * there is no winner and or loser, each player is given a score in the game, and this is recorded
 */
public class ScoreGameResult extends AbstractGameResult {
    private static final long serialVersionUID = -8148281346410253240L;
    private double highScore;

    public ScoreGameResult(double highScore) {
        this.highScore = highScore;
    }

    public ScoreGameResult(ScoreGameResult other){
        highScore = other.highScore;
    }

    public double getHighScore() {
        return highScore;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ScoreGameResult getClone() {
        return new ScoreGameResult(this);
    }

}
