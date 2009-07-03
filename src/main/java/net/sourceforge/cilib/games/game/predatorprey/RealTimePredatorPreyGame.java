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

import net.sourceforge.cilib.games.game.RealTimeGame;
import net.sourceforge.cilib.games.states.ListGameState;

/**
 * @author leo
 * This is an implimentation of the Predator Prey game where the Predator and PRey agents make their movements simultaneously instead of turn based.
 * Therefore they each make a movement decision on the same state of the game, and they are both moved at the same time.
 */
public class RealTimePredatorPreyGame extends PredatorPreyGame implements RealTimeGame {
    private static final long serialVersionUID = -4172820771225474751L;
    //This stores the game state before the movement decision was made
    ListGameState roundStartGameState;
    public RealTimePredatorPreyGame() {
        super();
        roundStartGameState = getCurrentState().getClone();
    }
    public RealTimePredatorPreyGame(RealTimePredatorPreyGame other) {
        super(other);
        roundStartGameState = other.roundStartGameState.getClone();
    }

    public RealTimePredatorPreyGame(RealTimePredatorPreyGame other, ListGameState newstate) {
        super(other, newstate);
        roundStartGameState = other.roundStartGameState.getClone();
    }

    /**
     * {@inheritDoc}
     */
    public void recordRoundStartState() {
        roundStartGameState = getCurrentState().getClone();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ListGameState getDecisionState() {
        return roundStartGameState;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RealTimePredatorPreyGame getClone() {
        return new RealTimePredatorPreyGame(this);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public RealTimePredatorPreyGame getClone(ListGameState newState) {
        return new RealTimePredatorPreyGame(this, newState);
    }

}
