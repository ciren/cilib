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
package net.sourceforge.cilib.boa.positionupdatestrategies;

import net.sourceforge.cilib.boa.bee.HoneyBee;
import net.sourceforge.cilib.util.Cloneable;


/**
 * Interface for a bee position update strategy.
 *
 * @author Andrich
 */
public interface BeePositionUpdateStrategy extends Cloneable {

    /**
     * {@inheritDoc}
     */
    @Override
    public BeePositionUpdateStrategy getClone();

    /**
     * Updates the position of the given bee.
     * @param bee the bee the position update is for.
     * @param other another bee that the position update might use to update the position.
     * @return whether the position update was successful.
     */
    public boolean updatePosition(HoneyBee bee, HoneyBee other);

}
