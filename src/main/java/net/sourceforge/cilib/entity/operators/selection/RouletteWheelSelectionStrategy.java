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

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.TopologyHolder;
import net.sourceforge.cilib.util.selection.recipes.RouletteWheelSelection;
import net.sourceforge.cilib.util.selection.weighing.entity.EntityWeighing;

/**
 * This class implements Roulette Wheel selection, also known as proportionate
 * selection. The probability of selecting an {@linkplain Entity} is equal to
 * the ratio of its fitness to the sum total of the fitness of all the
 * {@linkplain Entity}s in the population.
 *
 * <p>
 * This method of selection is not particularly useful if a single solution has
 * very high fitness as compared to the rest. This results in selecting the
 * fittest solution every time we make a selection.
 *
 * @author Vikash Ranjan Parida
 */
public class RouletteWheelSelectionStrategy extends SelectionStrategy {
    private static final long serialVersionUID = 6827649649373047787L;

    /**
     * Create an instance of the {@linkplain RouletteWheelSelectionStrategy}.
     */
    public RouletteWheelSelectionStrategy() {
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public RouletteWheelSelectionStrategy(final RouletteWheelSelectionStrategy copy) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SelectionStrategy getClone() {
        return new RouletteWheelSelectionStrategy(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends Entity> T select(final Topology<T> population) {
        return new RouletteWheelSelection<T>(new EntityWeighing<T>()).select(population);
    }

    /**
     * {@inheritDoc}
     */
    @Override
//    public void performOperation(Topology<? extends Entity> topology, Topology<Entity> offspring) {
    public void performOperation(TopologyHolder holder) {
//        Topology<Entity> offspring = (Topology<Entity>) holder.getOffpsring();
        Topology<? extends Entity> topology = holder.getTopology();
//        offspring.add(select(topology));
        holder.add(select(topology));
    }
}
