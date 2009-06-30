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
import net.sourceforge.cilib.games.items.GameToken;
import net.sourceforge.cilib.games.items.ItemLocation;

/**
 * @author leo
 * This PP initialization strategy initialises the Agents to fixed locations in the grid.
 */
public class StaticPositionInitializationStrategy extends
        PredPreyPositionInitializationStrategy {
    private static final long serialVersionUID = -2380089057593561584L;
    ItemLocation predLocation;
    ItemLocation preyLocation;
    public StaticPositionInitializationStrategy() {
    }

    /**
     * Copy constructor
     * @param other
     */
    public StaticPositionInitializationStrategy(StaticPositionInitializationStrategy other) {
        super(other);
        predLocation = other.predLocation.getClone();
        preyLocation = other.preyLocation.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StaticPositionInitializationStrategy getClone() {
        return new StaticPositionInitializationStrategy(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializePP(PredatorPreyGame game) {
        game.getCurrentState().getItem(GameToken.PredatorPrey.PREDATOR).setLoction(predLocation.getClone());
        game.getCurrentState().getItem(GameToken.PredatorPrey.PREY).setLoction(preyLocation.getClone());
    }

    public ItemLocation getPredLocation() {
        return predLocation;
    }

    public void setPredLocation(ItemLocation predLocation) {
        this.predLocation = predLocation;
    }

    public ItemLocation getPreyLocation() {
        return preyLocation;
    }

    public void setPreyLocation(ItemLocation preyLocation) {
        this.preyLocation = preyLocation;
    }
}
