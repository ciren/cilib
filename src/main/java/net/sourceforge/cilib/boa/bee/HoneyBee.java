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
package net.sourceforge.cilib.boa.bee;

import net.sourceforge.cilib.boa.positionupdatestrategies.BeePositionUpdateStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Super interface for all types of bees in the artificial bee algorithm.
 * @author Andrich
 */
public interface HoneyBee extends Entity {

    /**
     * {@inheritDoc}
     */
    @Override
    public HoneyBee getClone();

    /**
     * {@inheritDoc}
     */
    @Override
    public Fitness getFitness();

    /**
     * Updates the position of the bee based on the neighboring nectar content.
     */
    public void updatePosition();

    /**
     * Gets the bee's position (contents).
     * @return the position.
     */
    public Vector getPosition();

    /**
     * Sets the bee's position (contents).
     * @param position The value to set.
     */
    public void setPosition(Vector position);

    /**
     * Getter method for the position update strategy.
     * @return the position update strategy.
     */
    public BeePositionUpdateStrategy getPositionUpdateStrategy();

}
