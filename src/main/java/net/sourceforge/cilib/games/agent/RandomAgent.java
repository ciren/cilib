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
package net.sourceforge.cilib.games.agent;

import java.util.List;

import net.sourceforge.cilib.games.game.Game;
import net.sourceforge.cilib.games.game.StateGame;
import net.sourceforge.cilib.games.states.GameState;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.type.DomainRegistry;
import net.sourceforge.cilib.type.types.Type;

/**
 * This is an agent that chooses a random state as its decision, it will only work on {@linkplain Game}'s that impliment {@linkplain StateGame}
 */
public class RandomAgent extends Agent {

    private static final long serialVersionUID = -1179969007031881319L;

    public RandomAgent() {
    }

    /** Copy constructor
     * @param other
     */
    public RandomAgent(Agent other) {
        super(other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomainRegistry getAgentDomain() {
        throw new RuntimeException("This agent cannot be optomised");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Agent getClone() {
        return new RandomAgent(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializeAgent(Type agentData) {
        throw new RuntimeException("This agent cannot be optomised");

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void move(Game<GameState> game) {
        if(!(game instanceof StateGame))
            throw new RuntimeException("Random agent can only operate on an state game");
        List<GameState> states = ((StateGame)game).generateStates(playerID);
        RandomProvider rand = new MersenneTwister();
        game.setCurrentGameState(states.get(rand.nextInt(states.size())));
    }
}
