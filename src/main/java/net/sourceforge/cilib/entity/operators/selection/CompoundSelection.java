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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.TopologyHolder;

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

    /**
     * @TODO: Correct this method
     */
//    public void performOperation(Topology<? extends Entity> topology, Topology<Entity> offspring) {
    public void performOperation(TopologyHolder holder) {
        this.select(holder.getTopology()); // This method needs to be corrected
    }

    public void addSelection(SelectionStrategy selectionOperator) {
        this.selectors.add(selectionOperator);
    }

}
