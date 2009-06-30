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
package net.sourceforge.cilib.entity.operators.creation;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.operators.Operator;

/**
 * Creation operator definition. The manner in which new {@code Entity} instances
 * are created is specified.
 */
public interface CreationStrategy extends Operator {

    @Override
    public CreationStrategy getClone();

    /**
     * Create an Entity, based on the provided parameters.
     *
     * TODO: this may need to be simplified in some way.
     *
     * @param targetEntity
     * @param current
     * @param topology
     * @return
     */
    public Entity create(Entity targetEntity, Entity current, Topology<? extends Entity> topology);

}
