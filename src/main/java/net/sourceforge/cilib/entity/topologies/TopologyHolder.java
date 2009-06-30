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
package net.sourceforge.cilib.entity.topologies;

import java.util.List;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;

public interface TopologyHolder {

    public Topology<? extends Entity> getTopology();

    /**
     * Add a child to the holder?
     * @param entity
     */
    public void add(Entity entity);

    /**
     * plural version of add()
     * @param entities
     */
    public void addAll(List<Entity> entities);

    public void add(Entity entity, boolean modifiable);

    /**
     * get the modifiable list of entities. The idea here is to return various
     * sets based on the type of topologyholder used.
     *
     * In the EC case, the actual offspring collection will be returned.
     * In a PSO, the main topology should be returned.
     *
     * @TODO: This gonna work? Looks like it might...
     *
     * @return
     */
    public List<? extends Entity> getModifiable();

    public List<? extends Entity> getUnmodifiable();

}
