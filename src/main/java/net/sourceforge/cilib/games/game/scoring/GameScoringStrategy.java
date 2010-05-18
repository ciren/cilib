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
