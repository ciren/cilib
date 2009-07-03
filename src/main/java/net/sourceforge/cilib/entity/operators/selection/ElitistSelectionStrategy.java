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
package net.sourceforge.cilib.entity.operators.selection;

import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.ProportionalControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.TopologyHolder;
import net.sourceforge.cilib.util.selection.recipes.ElitistSelection;

public class ElitistSelectionStrategy extends SelectionStrategy {
    private static final long serialVersionUID = -3055600262753819388L;

    private ControlParameter selectionPercentage;

    public ElitistSelectionStrategy() {
        this.selectionPercentage = new ProportionalControlParameter();
    }

    public ElitistSelectionStrategy(ElitistSelectionStrategy copy) {
        this.selectionPercentage = copy.selectionPercentage.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ElitistSelectionStrategy getClone() {
        return new ElitistSelectionStrategy(this);
    }

    /**
     * Perform an elitist selection. This selection will return the most fit entity
     * within the current {@link Topology}. The selected entity is not removed from
     * the topology.
     * @param <T> The generic entity type.
     * @param population The population from which the selections are to be made.
     * @return The selected entity.
     */
    @Override
    public <T extends Entity> T select(Topology<T> population) {
        return new ElitistSelection<T>().select(population);
    }

    /**
     * Perform the selection procedure. The selection will be made and the resulting selected entities
     * will be placed into the offspring list and effectively removed from the topology. This will result
     * in no modification operators (cross-over or mutation) being applied to these entities.
     * @param holder The {@link TopologyHolder} maintaining the various entity collections.
     */
    @Override
    public void performOperation(TopologyHolder holder) {
        int size = Double.valueOf(this.selectionPercentage.getParameter() * holder.getTopology().size()).intValue();

        // This effectively removes the selected entity from the topology and adds it to the offspring
        for (int i = 0; i < size; i++) {
            Entity selected = this.select(holder.getTopology());
            holder.add(selected);
        }
    }

    /**
     * Get the percentage of selection for the elitist selection.
     * @return The value of the selection percentage.
     */
    public ControlParameter getSelectionPercentage() {
        return selectionPercentage;
    }

    /**
     * Set the percentage for the selection. The selection should be a percentage.
     * @param selectionPercentage The value to set.
     */
    public void setSelectionPercentage(ControlParameter selectionPercentage) {
        this.selectionPercentage = selectionPercentage;
    }
}
