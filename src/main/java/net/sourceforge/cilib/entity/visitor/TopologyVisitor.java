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
package net.sourceforge.cilib.entity.visitor;

import net.sourceforge.cilib.container.visitor.Visitor;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;

/**
 * Interface for all visitor instances that visit an entire {@link Topology}
 * at once. These type of visitors are generally assocaited with the
 * calculation of topology related information, such as diameter and
 * radius calculations of the provided topologies.
 *
 */
public interface TopologyVisitor extends Visitor<Topology<? extends Entity>> {
    /**
     * Perfrom the visit operation on the provided {@link Topology}.
     * @param topology The {@link Topology} to visit.
     */
    @Override
    public void visit(Topology<? extends Entity> topology);

    /**
     * Get the result of the visitor after it has performed the visit()
     * action.
     * @return The result of the visit()
     */
    public Object getResult();
}
