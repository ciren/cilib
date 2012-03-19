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
package net.sourceforge.cilib.problem.boundaryconstraint;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Cloneable;

/**
 * Enforce predefined boundary constraints on {@link Entity} instances that are
 * operating in the current search space. Various strategies are available to
 * enforce these boundary contraints on the provided {@link Entity} objects.
 */
public interface BoundaryConstraint extends Cloneable {

    /**
     * {@inheritDoc}
     */
    @Override
    BoundaryConstraint getClone();

    /**
     * Enforce the defined boundary constraint on the provided {@linkplain Entity}.
     * @param entity The {@linkplain Entity} with which the boundary is to be enforced.
     */
    Entity enforce(Entity entity);

}
