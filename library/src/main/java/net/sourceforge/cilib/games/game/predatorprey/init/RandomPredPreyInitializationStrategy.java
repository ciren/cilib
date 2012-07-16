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
package net.sourceforge.cilib.games.game.predatorprey.init;

import net.sourceforge.cilib.games.game.predatorprey.PredatorPreyGame;
import net.sourceforge.cilib.games.items.GridLocation;
import net.sourceforge.cilib.math.random.generator.RandomProvider;

/**
 * This {@linkplain PredPreyPositionInitializationStrategy} initialises each agent to a random location
 */
public class RandomPredPreyInitializationStrategy extends PredPreyPositionInitializationStrategy {

    private static final long serialVersionUID = 6061475792359499040L;

    public RandomPredPreyInitializationStrategy() {
    }

    /**
     * Copy constructor
     * @param other
     */
    public RandomPredPreyInitializationStrategy(PredPreyPositionInitializationStrategy other) {
        super(other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RandomPredPreyInitializationStrategy getClone() {
        return new RandomPredPreyInitializationStrategy(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializePP(PredatorPreyGame game) {
        RandomProvider rand = game.getCurrentState().getRandomizer().getGenerator();
        for(int i = 0; i < game.getCurrentState().getSize(); ++i){
            GridLocation itemLoc = (GridLocation)game.getCurrentState().getItem(i).getLocation();
            itemLoc.setInt(0, rand.nextInt(game.getBoardWidth()));
            itemLoc.setInt(1, rand.nextInt(game.getBoardHeight()));
        }
    }

}
