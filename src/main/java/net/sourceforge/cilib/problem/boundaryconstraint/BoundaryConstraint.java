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
package net.sourceforge.cilib.problem.boundaryconstraint;

import net.sourceforge.cilib.entity.Entity;
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
    public BoundaryConstraint getClone();

    /**
     * Enforce the defined boundary constraint on the provided {@linkplain Entity}.
     * @param entity The {@linkplain Entity} with which the boundary is to be enforced.
     */
    public void enforce(Entity entity);

}
