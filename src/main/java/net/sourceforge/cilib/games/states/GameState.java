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
package net.sourceforge.cilib.games.states;



import net.sourceforge.cilib.games.random.GameSeedingStrategy;
import net.sourceforge.cilib.games.random.UniqueSeedingStrategy;
import net.sourceforge.cilib.util.Cloneable;

/**
 * @author leo
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
