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

import net.sourceforge.cilib.games.agent.Agent;
import net.sourceforge.cilib.games.agent.neural.NeuralOutputInterpretationStrategy;
import net.sourceforge.cilib.games.game.Game;
import net.sourceforge.cilib.games.items.GameToken;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.TypeList;

/**
 * @author leo
 * This is a {@linkplain NeuralOutputInterpretationStrategy} that will interperet the output of a neural network to make a movement decision for a Prey agent
 */
public class NeuralPreyOutputStrategy extends
        NeuralOutputInterpretationStrategy {
    public NeuralPreyOutputStrategy() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void applyOutputToState(TypeList outputData, Agent currentPlayer, Game oldState) {

        if(!(oldState instanceof PredatorPreyGame))
            throw new RuntimeException("Invalid game for this agent");
        if(!currentPlayer.getAgentToken().equals(GameToken.PredatorPrey.PREY))
            throw new RuntimeException("This strategy can only be used on a prey player");

        PredatorPreyGame game = (PredatorPreyGame)oldState;
        int moveAmount = 1;
        if(((Numeric) outputData.get(0)).getReal() >  0.0) //move 2 squares
            moveAmount = 2;

        int x = 0;
        if(((Numeric) outputData.get(1)).getReal() >  0.0) //move on x axis
            if(((Numeric) outputData.get(2)).getReal() >  0.0) //move right
                x = 1;
            else
                x = -1;

        int y = 0;
        if(((Numeric) outputData.get(3)).getReal() >  0.0) //move on y axis
            if(((Numeric) outputData.get(4)).getReal() >  0.0) //move down
                y = 1;
            else
                y = -1;
        //if(x < 0 || y < 0)
            //System.out.println("Prey: " + x * moveAmount + " " + y * moveAmount);
        game.movePlayer(currentPlayer.getPlayerID(), x * moveAmount, y * moveAmount);
    }

    /**
     * This strategy requires 5 outputs:
     * The first determins if the prey agent will Jump 2 cells, or move to an adjacent one
     * The second determines movement on the x axis
     * the third determines if that movement is left or right
     * The fourth determines movement on the y axes
     * The fifth determines if it is up or down
     */
    @Override
    public int getAmOutputs() {
        return 5;
    }

}
