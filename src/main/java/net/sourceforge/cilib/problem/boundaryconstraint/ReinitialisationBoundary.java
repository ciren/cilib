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
import net.sourceforge.cilib.type.types.Types;

/**
 * Once the entity has over shot the search space boundaries, re-initialise
 * the Entity once again to be witihin the search space of the problem at a
 * random position.
 *
 * @see Types#isInsideBounds(net.sourceforge.cilib.type.types.Type)
 */
public class ReinitialisationBoundary implements BoundaryConstraint {
    private static final long serialVersionUID = -512973040124015665L;

    /**
     * {@inheritDoc}
     */
    @Override
    public ReinitialisationBoundary getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public void enforce(Entity entity) {
        if (!Types.isInsideBounds(entity.getCandidateSolution())) {
            entity.reinitialise();
        }
    }

}
