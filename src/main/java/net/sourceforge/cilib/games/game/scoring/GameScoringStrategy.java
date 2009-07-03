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
package net.sourceforge.cilib.games.game.scoring;

import net.sourceforge.cilib.games.game.Game;

/**
 * @author leo
 * This class assigns a fitness to a player after a game has been played
 */
public abstract class GameScoringStrategy {
    public GameScoringStrategy() {
    }

    /**
     * Initialize the game with the neccesary {@linkplain AgentMeasure}'s that the scoring strategy may require to calculate the {@linkplain Fitness}
     * for all the {@linkplain Agent}s
     * @param game the game to initilaise
     */
    public abstract void initializeMeasurements(Game game);
    /**
     * Assign a {@linkplain Fitness} to each {@linkplain Agent} after the completion of a {@linkplain Game}
     * @param game the game object
     */
    public abstract void assignPlayerScores(Game game);
}
