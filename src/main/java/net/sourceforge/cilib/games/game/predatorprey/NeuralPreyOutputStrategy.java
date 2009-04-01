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

import net.sourceforge.cilib.games.agent.Agent;
import net.sourceforge.cilib.games.agent.neural.NeuralOutputInterpretationStrategy;
import net.sourceforge.cilib.games.game.Game;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @author leo
 *
 */
public class NeuralPreyOutputStrategy extends
        NeuralOutputInterpretationStrategy {

    /**
     *
     */
    public NeuralPreyOutputStrategy() {
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see net.sourceforge.cilib.games.agent.neural.NeuralOutputInterpretationStrategy#applyOutputToState(net.sourceforge.cilib.type.types.container.Vector, net.sourceforge.cilib.games.agent.Agent, net.sourceforge.cilib.games.game.Game)
     */
    @Override
    public void applyOutputToState(Vector outputData, Agent currentPlayer, Game oldState) {

        if(!(oldState instanceof PredatorPreyGame))
            throw new RuntimeException("Invalid game for this agent");

        PredatorPreyGame game = (PredatorPreyGame)oldState;
        int moveAmount = 1;
        if(outputData.getReal(0) >  0.5) //move 2 squares
            moveAmount = 2;

        int x = 0;
        if(outputData.getReal(1) >  0.5) //move on x axis
            if(outputData.getReal(2) >  0.5) //move right
                x = 1;
            else
                x = -1;

        int y = 0;
        if(outputData.getReal(3) >  0.5) //move on y axis
            if(outputData.getReal(4) >  0.5) //move down
                y = 1;
            else
                y = -1;
        game.movePlayer(currentPlayer.getPlayerID(), x * moveAmount, y * moveAmount);
    }

    /* (non-Javadoc)
     * @see net.sourceforge.cilib.games.agent.neural.NeuralOutputInterpretationStrategy#getAmOutputs()
     */
    @Override
    public int getAmOutputs() {
        // TODO Auto-generated method stub
        return 5;
    }

}
