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

import com.google.common.base.Function;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.EntityType.Particle;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;

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
    public Entity enforce(Entity entity) {
        Entity newEntity = entity.getClone();
        StructuredType<?> structuredType = (StructuredType<?>) newEntity.getProperties().get(EntityType.Particle.VELOCITY);

        if (structuredType == null) {
            throw new UnsupportedOperationException("Cannot perform this boundary constrain on a " + newEntity.getClass().getSimpleName());
        }

        Vector result = Vectors.transform((Vector) structuredType, new Function<Numeric, Double>() {

            @Override
            public Double apply(Numeric from) {
                Bounds bounds = from.getBounds();
                if (Double.compare(from.doubleValue(), bounds.getLowerBound()) < 0) {
                    return bounds.getLowerBound();
                } else if (Double.compare(from.doubleValue(), bounds.getUpperBound()) > 0) { // number > upper bound
                    return bounds.getUpperBound() - Maths.EPSILON;
                }
                return from.doubleValue();
            }
        });
       
        newEntity.getProperties().put(EntityType.Particle.VELOCITY, result);
        
        return newEntity;
    }
}
