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
package net.sourceforge.cilib.games.game.predatorprey;

import net.sourceforge.cilib.games.game.RealTimeGame;
import net.sourceforge.cilib.games.states.ListGameState;

/**
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
