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
package net.sourceforge.cilib.games.game.predatorprey;

import net.sourceforge.cilib.algorithm.InitialisationException;
import net.sourceforge.cilib.games.agent.NeuralAgent;
import net.sourceforge.cilib.games.agent.neural.NeuralStateInputStrategy;
import net.sourceforge.cilib.games.game.Game;
import net.sourceforge.cilib.games.items.GameToken;
import net.sourceforge.cilib.games.items.GridLocation;
import net.sourceforge.cilib.games.states.ListGameState;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @author leo
 * This is the neural input strategr for a Predator or Prey agent. It determins how the game is described to the {@linkplain NeuralAgent}
 */
public class PredatorPreyNeuralInputStrategy extends NeuralStateInputStrategy {

    /**
     *
     */
    public PredatorPreyNeuralInputStrategy() {
    }

    /**
     * This {@linkplain NeuralStateInputStrategy} requires 4 input units. The first two is the position of the Predator agent, and the 2nd two is the position of the Prey agent.
     */
    @Override
    public int amountInputs() {
        return 4;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getNeuralInputArray(NeuralAgent currentPlayer, Game state) {
        try{
            if(!(state instanceof PredatorPreyGame))
                throw new RuntimeException("Invalid game for this agent");
            ListGameState lstate = (ListGameState)state.getDecisionState();
            Vector predPos = null, preyPos = null;
            for(int i = 0; i < lstate.getSize(); i++){
                if(lstate.getItem(i).getToken().equals(GameToken.PredatorPrey.PREDATOR))
                    predPos = ((GridLocation)lstate.getItem(i).getLocation());
                else
                    preyPos = ((GridLocation)lstate.getItem(i).getLocation());
            }
            Vector inputvector = new Vector(4);
            inputvector.add(new Real(currentPlayer.getScaledInput((double)predPos.getInt(0), 0, ((PredatorPreyGame)state).getBoardWidth())));
            inputvector.add(new Real(currentPlayer.getScaledInput((double)predPos.getInt(1), 0, ((PredatorPreyGame)state).getBoardHeight())));
            inputvector.add(new Real(currentPlayer.getScaledInput((double)preyPos.getInt(0), 0, ((PredatorPreyGame)state).getBoardWidth())));
            inputvector.add(new Real(currentPlayer.getScaledInput((double)preyPos.getInt(1), 0, ((PredatorPreyGame)state).getBoardHeight())));
            return inputvector;
        }
        catch(Exception e)
        {
            throw new InitialisationException("Game not initialized, predator and prey items do not exist");
        }
    }

}
