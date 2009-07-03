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
package net.sourceforge.cilib.games.game.tictactoe;

import net.sourceforge.cilib.games.agent.NeuralAgent;
import net.sourceforge.cilib.games.agent.neural.NeuralStateInputStrategy;
import net.sourceforge.cilib.games.game.Game;
import net.sourceforge.cilib.games.game.GridGame;
import net.sourceforge.cilib.games.items.GameItem;
import net.sourceforge.cilib.games.items.PlayerItem;
import net.sourceforge.cilib.games.states.GridGameState;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This class is used to give a Neural Network a Tic Tac Toe game.
 * For this {@linkplain NeuralStateInputStrategy} the Neural Network needs to have a number of inputs equals to the number of cells in the game.
 * Each input is then given to the network depending if the cell is occupied by the current player, the opponent player or if its empty.
 * @author leo
 *
 */
public class TTTStateInputStrategy extends NeuralStateInputStrategy {

    public TTTStateInputStrategy() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int amountInputs() {
        return 9; //need to find a better way of doing this, hardcoding the value is bad mkay.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector getNeuralInputArray(NeuralAgent currentPlayer, Game state) {
        Vector input = new Vector(amountInputs());
        int Width = ((GridGame)state).getWidth();
        int Height = ((GridGame)state).getHeight();
        GridGameState gstate = (GridGameState)state.getDecisionState();

        for(int j = 0; j < Height; ++j){
            for(int i = 0; i < Width; ++i){
                GameItem item =gstate.getItem(i, j);
                if(item != null){
                    if(((PlayerItem)item).getPlayerID() == currentPlayer.getPlayerID())
                        input.add(new Real(currentPlayer.getScaledInput(1, -1, 1))); //it is me
                    else
                        input.add(new Real(currentPlayer.getScaledInput(-1, -1, 1))); //it is not me
                }
                else
                    input.add(new Real(currentPlayer.getScaledInput(0, -1, 1))); //it is nobody
            }
        }
        return input;
    }

}
