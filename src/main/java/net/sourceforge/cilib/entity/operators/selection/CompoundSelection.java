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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;

/**
 * This class implements the Composite design pattern to correctly
 * apply a group of selection operators to perform a specific selection.
 * For example, a selection my require an Elitism selection of 10% of the
 * available entities, followed by a greedy fitness selection operator.
 *
 * @author gpampara
 *
 */
public class CompoundSelection extends SelectionStrategy {

    private static final long serialVersionUID = -5547221580096508262L;
    private List<SelectionStrategy> selectors;

    public CompoundSelection() {
        selectors = new ArrayList<SelectionStrategy>();
    }

    public CompoundSelection(CompoundSelection copy) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SelectionStrategy getClone() {
        return new CompoundSelection(this);
    }

    @Override
    public <T extends Entity> T select(Topology<T> population) {
        for (SelectionStrategy selection : selectors) {
            selection.select(population);
        }

        return null;
    }

    public void addSelection(SelectionStrategy selectionOperator) {
        this.selectors.add(selectionOperator);
    }
}
