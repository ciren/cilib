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
import net.sourceforge.cilib.util.Cloneable;

/**
 * @author leo
 * This class determines how the Predator and Prey agent's positions will be initialized in a game of Predator vs Prey.
 */
public abstract class PredPreyPositionInitializationStrategy implements Cloneable {
    public PredPreyPositionInitializationStrategy() {
    }
    public PredPreyPositionInitializationStrategy(PredPreyPositionInitializationStrategy other) {
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public abstract PredPreyPositionInitializationStrategy getClone();
    /**
     * Initialize the positions of the predator and prey agents
     * @param game The game object to initialise
     */
    public abstract void initializePP(PredatorPreyGame game);
}
