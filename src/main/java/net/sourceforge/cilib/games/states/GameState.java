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
package net.sourceforge.cilib.games.states;



import net.sourceforge.cilib.games.random.GameSeedingStrategy;
import net.sourceforge.cilib.games.random.UniqueSeedingStrategy;
import net.sourceforge.cilib.util.Cloneable;

/**
 * This class represents a snapshot of the game at any given iteration.
 */
public abstract class GameState implements Cloneable {
    //the current iteration of the game. An iteration is completed after each player has made a move and the state is updated
    protected int currentIteration;
    protected GameSeedingStrategy randomizer;
    private static final long serialVersionUID = 3668288597302493178L;

    public GameState() {
        currentIteration = 0;
        randomizer = new UniqueSeedingStrategy();
    }

    public GameState(GameState other){
        currentIteration = other.currentIteration;
        randomizer = other.randomizer.getClone();
    }
    /**
     * Clear the state and reset it to a start state.
     */
    public abstract void clearState();
    /**
     * {@inheritDoc}
     */
    public abstract GameState getClone();

    public void resetIterationCount(){
        currentIteration = 0;
    }
    public void increaseIteration(){
        ++currentIteration;
    }
    public int getCurrentIteration(){
        return currentIteration;
    }

    public GameSeedingStrategy getRandomizer() {
        return randomizer;
    }

    public void setRandomizer(GameSeedingStrategy randomizer) {
        this.randomizer = randomizer;
    }
}
