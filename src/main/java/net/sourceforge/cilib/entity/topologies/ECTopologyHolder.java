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

public class ECTopologyHolder implements TopologyHolder {
    private Topology<? extends Entity> topology;
    private Topology<Entity> offspring;
    private Topology<Entity> unchanged;

    public ECTopologyHolder() {
        offspring = new GBestTopology<Entity>(); // needs to be re-thought... same instance as topology
        unchanged = new GBestTopology<Entity>();
    }

    public ECTopologyHolder(Topology<? extends Entity> topology) {
        this.topology = topology;
        this.offspring = new GBestTopology<Entity>();
        this.unchanged = new GBestTopology<Entity>();
    }

    @Override
    public Topology<? extends Entity> getTopology() {
        return topology;
    }

    @Override
    public void add(Entity entity) {
        add(entity, true);
    }

    @Override
    public void addAll(List<Entity> entities) {
        for (Entity entity : entities)
            add(entity, true);
    }

    public Topology<Entity> getOffspring() {
        return this.offspring;
    }

    @Override
    public List<? extends Entity> getModifiable() {
        return this.offspring.asList();
    }

    /**
     * Add the current provided {@code entity} to either the {@code modifiable} and {@code unmodifiable}
     * lists, maintained within this holder.
     * @param entity The entity insance to add.
     * @param modifiable If {@code true} add to the modifiable list, else add to the unmodifiable list.
     */
    @Override
    public void add(Entity entity, boolean modifiable) {
        if (modifiable) {
            this.offspring.add(entity);
            return;
        }

        this.unchanged.add(entity);
        this.topology.remove(entity);
    }

    @Override
    public List<? extends Entity> getUnmodifiable() {
        return this.unchanged.asList();
    }

}
