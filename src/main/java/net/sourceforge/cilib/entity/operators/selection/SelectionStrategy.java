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
import net.sourceforge.cilib.entity.operators.Operator;

/**
 *
 * @author Gary Pampara
 */
public abstract class SelectionStrategy implements Operator {
    private static final long serialVersionUID = 410467686448546547L;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract SelectionStrategy getClone();

    /**
     * Apply the selection strategy and return a single {@linkplain Entity}.
     * @param population The {@linkplain Topology} to make the selection from.
     * @param <T> The Entity type.
     * @return The selected {@linkplain Entity}.
     */
    public abstract <T extends Entity> T select(Topology<T> population);

}
