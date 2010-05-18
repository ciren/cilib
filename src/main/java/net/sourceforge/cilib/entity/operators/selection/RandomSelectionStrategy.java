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
package net.sourceforge.cilib.entity.operators.selection;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
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
}
