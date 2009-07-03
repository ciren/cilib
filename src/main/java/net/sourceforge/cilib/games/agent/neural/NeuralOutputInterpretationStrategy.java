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
package net.sourceforge.cilib.games.agent.neural;

import net.sourceforge.cilib.games.agent.Agent;
import net.sourceforge.cilib.games.agent.NeuralAgent;
import net.sourceforge.cilib.games.game.Game;
import net.sourceforge.cilib.games.states.GameState;
import net.sourceforge.cilib.type.types.container.TypeList;

/**
 * @author leo
 * This class is used by the {@linkplain NeuralAgent} to alter the given game state by the output vector of the Neural Network after the input has
 * been processed.
 */
public abstract class NeuralOutputInterpretationStrategy {
    public NeuralOutputInterpretationStrategy() {
    }

    /**
     * the the number of output units that this strategy requires
     * @return the output count
     */
    public abstract int getAmOutputs();
    /**
     * Modify the game by the output vector
     * @param outputData the output data vector
     * @param currentPlayer the agent that is represented by the Neural Network
     * @param oldState the game state to be altered
     */
    public abstract void applyOutputToState(TypeList outputData, Agent currentPlayer, Game<GameState> oldState);
}
