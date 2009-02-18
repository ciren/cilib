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

import net.sourceforge.cilib.algorithm.InitialisationException;
import net.sourceforge.cilib.games.agent.NeuralAgent;
import net.sourceforge.cilib.games.agent.neural.NeuralStateInputStrategy;
import net.sourceforge.cilib.games.game.Game;
import net.sourceforge.cilib.games.items.GridLocation;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @author leo
 *
 */
public class PredatorPreyNeuralInputStrategy extends NeuralStateInputStrategy {

	/**
	 *
	 */
	public PredatorPreyNeuralInputStrategy() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.games.agent.neural.NeuralStateInputStrategy#amountInputs()
	 */
	@Override
	public int amountInputs() {
		// TODO Auto-generated method stub
		return 4;
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.games.agent.neural.NeuralStateInputStrategy#getNeuralInputArray(net.sourceforge.cilib.games.states.GameState)
	 */
	@Override
	public Vector getNeuralInputArray(NeuralAgent currentPlayer, Game state) {
		try {
			if (!(state instanceof PredatorPreyGame)) {
				throw new RuntimeException("Invalid game for this agent");
			}

			// TODO Auto-generated method stub
			Vector pos1 = ((GridLocation) (state.getCurrentState().getItem(0).getLocation())).getPosition();
			Vector pos2 = ((GridLocation) (state.getCurrentState().getItem(1).getLocation())).getPosition();
			Vector inputvector = new Vector(4);
			inputvector.add(new Real(currentPlayer.getScaledInput((double) pos1.getInt(0), 0, ((PredatorPreyGame) state).getWidth())));
			inputvector.add(new Real(currentPlayer.getScaledInput((double) pos1.getInt(1), 0, ((PredatorPreyGame) state).getHeight())));
			inputvector.add(new Real(currentPlayer.getScaledInput((double) pos2.getInt(0), 0, ((PredatorPreyGame) state).getWidth())));
			inputvector.add(new Real(currentPlayer.getScaledInput((double) pos2.getInt(1), 0, ((PredatorPreyGame) state).getHeight())));
			return inputvector;
		} catch (Exception e) {
			throw new InitialisationException("Game not initialized, predator and prey items do not exist");
		}
	}
}
