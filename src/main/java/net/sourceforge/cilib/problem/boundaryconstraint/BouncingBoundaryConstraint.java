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
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.StructuredType;

/**
 * Instead of making use of <i>reactive</i> boundary constraints that reinitialise an entire
 * {@linkplain Particle} (or components thereof), this class is a <b>proactive</b> approach to
 * prevent the {@linkplain Particle} from moving outside of the domain. The component of the
 * {@linkplain Particle} that will be outside of the domain is placed on the boundary of the domain
 * and the corresponding velocity component is recalculated (inverting the direction), effectively
 * making the {@linkplain Particle} bounce off the sides of the domain. The effect achieved is a
 * skewed type of reflection with built-in velocity damping.
 */
public class BouncingBoundaryConstraint implements BoundaryConstraint {
    private static final long serialVersionUID = -2790411012592758966L;

    /**
     * {@inheritDoc}
     */
    @Override
    public BoundaryConstraint getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enforce(Entity entity) {
        StructuredType structuredType = (StructuredType) entity.getProperties().get(EntityType.Particle.VELOCITY);

        if (structuredType == null)
            throw new UnsupportedOperationException("Cannot perform this boundary constrain on a "
                + entity.getClass().getSimpleName());

        Iterator pIterator = entity.getCandidateSolution().iterator();
        Iterator vIterator = structuredType.iterator();

        while (pIterator.hasNext()) {
            Numeric position = (Numeric) pIterator.next();
            Numeric velocity = (Numeric) vIterator.next();
            Bounds bounds = position.getBounds();

            double previousPosition = position.getReal();

            if (Double.compare(position.getReal(), bounds.getLowerBound()) < 0) {
                position.set(position.getBounds().getLowerBound());    // lower boundary is inclusive
                velocity.set(previousPosition - position.getReal());
            }
            else if (Double.compare(position.getReal(), bounds.getUpperBound()) > 0) {
                position.set(bounds.getUpperBound() - Maths.EPSILON);
                velocity.set(previousPosition - position.getReal());
            }
        }
    }

}
