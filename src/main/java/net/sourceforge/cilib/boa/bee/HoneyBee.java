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
package net.sourceforge.cilib.boa.bee;

import net.sourceforge.cilib.boa.positionupdatestrategies.BeePositionUpdateStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Super interface for all types of bees in the artificial bee algorithm.
 */
public interface HoneyBee extends Entity {

    /**
     * {@inheritDoc}
     */
    @Override
    HoneyBee getClone();

    /**
     * {@inheritDoc}
     */
    @Override
    Fitness getFitness();

    /**
     * Updates the position of the bee based on the neighboring nectar content.
     */
    void updatePosition();

    /**
     * Gets the bee's position (contents).
     * @return the position.
     */
    Vector getPosition();

    /**
     * Sets the bee's position (contents).
     * @param position The value to set.
     */
    void setPosition(Vector position);

    /**
     * Getter method for the position update strategy.
     * @return the position update strategy.
     */
    BeePositionUpdateStrategy getPositionUpdateStrategy();

}
