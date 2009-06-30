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

import java.util.Iterator;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.StructuredType;

/**
 * Prevent any {@link Entity} from over-shooting the problem search space. Any
 * Entity that passes outside the search space is placed on the boundaries of
 * the seach space.
 * @author gpampara
 */
public class ClampingBoundaryConstraint implements BoundaryConstraint {
    private static final long serialVersionUID = 3910725111116160491L;

    /**
     * {@inheritDoc}
     */
    @Override
    public ClampingBoundaryConstraint getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enforce(Entity entity) {
        StructuredType candidateSolution = entity.getCandidateSolution();

        for (Iterator i = candidateSolution.iterator(); i.hasNext();) {
            Numeric numeric = (Numeric) i.next();
            Bounds bounds = numeric.getBounds();

            if (Double.compare(numeric.getReal(), bounds.getLowerBound()) < 0) {
                numeric.setReal(bounds.getLowerBound());
            }
            else if (Double.compare(numeric.getReal(), bounds.getUpperBound()) > 0) { // number > upper bound
                numeric.setReal(bounds.getUpperBound() - Maths.EPSILON);
            }
        }
    }

}
