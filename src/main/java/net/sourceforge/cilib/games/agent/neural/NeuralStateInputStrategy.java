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
package net.sourceforge.cilib.games.agent.neural;

import net.sourceforge.cilib.games.agent.NeuralAgent;
import net.sourceforge.cilib.games.game.Game;
import net.sourceforge.cilib.games.states.GameState;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @author leo
 * This class is used by the {@linkplain NeuralAgent} to get an input vector based on a given {@linkplain GameState}
 */
public abstract class NeuralStateInputStrategy {
    /**
     *
     */
    public NeuralStateInputStrategy() {
    }

    /**
     * get the amount of inputs that the Neural Network should have based on this input strategy
     * @return the input count
     */
    public abstract int amountInputs();

    /**
     * Get an input vector for the given game state
     * @param currentPlayer the agent represented by the Neural Network
     * @param state the current game state
     * @return the input vector
     */
    public abstract Vector getNeuralInputArray(NeuralAgent currentPlayer, Game<GameState> state);
}
