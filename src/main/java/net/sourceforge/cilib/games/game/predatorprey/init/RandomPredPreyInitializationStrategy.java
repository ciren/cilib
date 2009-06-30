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
package net.sourceforge.cilib.games.game.predatorprey.init;

import net.sourceforge.cilib.games.game.predatorprey.PredatorPreyGame;
import net.sourceforge.cilib.games.items.GridLocation;
import net.sourceforge.cilib.math.random.generator.Random;

/**
 * @author leo
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
        Random rand = game.getCurrentState().getRandomizer().getGenerator();
        for(int i = 0; i < game.getCurrentState().getSize(); ++i){
            GridLocation itemLoc = (GridLocation)game.getCurrentState().getItem(i).getLocation();
            itemLoc.setInt(0, rand.nextInt(game.getBoardWidth()));
            itemLoc.setInt(1, rand.nextInt(game.getBoardHeight()));
        }
    }

}
