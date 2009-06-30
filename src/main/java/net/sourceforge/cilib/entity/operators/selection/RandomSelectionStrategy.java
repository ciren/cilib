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
import net.sourceforge.cilib.util.selection.recipes.RandomSelection;
import net.sourceforge.cilib.util.selection.recipes.SelectionRecipe;

/**
 *
 * @author gpampara
 */
public class RandomSelectionStrategy extends SelectionStrategy {
    private static final long serialVersionUID = -216894674927488180L;

    private SelectionRecipe<Entity> recipe;

    public RandomSelectionStrategy() {
        this.recipe = new RandomSelection<Entity>();
    }

    @Override
    public RandomSelectionStrategy getClone() {
        return new RandomSelectionStrategy();
    }

    @Override
    public <T extends Entity> T select(Topology<T> population) {
        return (T) this.recipe.select(population);
    }

    @Override
//    public void performOperation(Topology<? extends Entity> topology, Topology<Entity> offspring) {
    public void performOperation(TopologyHolder holder) {
        Topology<? extends Entity> topology = holder.getTopology();
//        Topology<Entity> offspring = (Topology<Entity>) holder.getOffpsring();

        holder.add(select(topology));
//        offspring.add(select(topology));
    }
}
