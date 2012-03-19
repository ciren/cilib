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

import java.util.Iterator;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.RandomProvider;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p>
 * If a particle oversteps the boundary it gets randomly re-initialised within the boundary and its
 * velocity gets updated.
 * </p>
 * <p>
 * References:
 * </p>
 * <pre>
 * {@literal @}inproceedings{ZXB04, author = "W.-J. Zhang and X.-F. Xie and D.-C. Bi",
 *                      title = "Handling boundary constraints for numerical optimization by
 *                      particle swarm flying in periodic search space",
 *                      booktitle = "IEEE Congress on Evolutionary Computation", month = jun,
 *                      year = {2004}, volume = "2", pages = {2307--2311} }
 * {@literal @}inproceedings{HW07, author = "S. Helwig and R. Wanka",
 *                      title = "Particle Swarm Optimization in High-Dimensional Bounded Search Spaces",
 *                      booktitle = "Proceedings of the 2007 IEEE Swarm Intelligence Symposium", month = apr,
 *                      year = {2007}, pages = {198--205} }
 * </pre>
 */
public class RandomBoundaryConstraint implements BoundaryConstraint {

    private static final long serialVersionUID = -4090871319456989303L;
    private RandomProvider random;

    public RandomBoundaryConstraint() {
        this.random = new MersenneTwister();
    }

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
    public Entity enforce(Entity oldEntity) {
        Entity entity = oldEntity.getClone();
        StructuredType<?> velocity = (StructuredType<?>) entity.getProperties().get(EntityType.Particle.VELOCITY);

        if (velocity == null) {
            throw new UnsupportedOperationException("Cannot perform this boundary constrain on a "
                    + entity.getClass().getSimpleName());
        }

        Vector.Builder newPosition = Vector.newBuilder();
        Vector.Builder newVelocity = Vector.newBuilder();

        Iterator<?> pIterator = entity.getCandidateSolution().iterator();
        Iterator<?> vIterator = velocity.iterator();
        
        while (pIterator.hasNext()) {
            Numeric pos = (Numeric) pIterator.next();
            Numeric vel = (Numeric) vIterator.next();
            Bounds bounds = pos.getBounds();
            
            if (Double.compare(pos.doubleValue(), bounds.getLowerBound()) < 0) {
                constrain(pos, vel, newPosition, newVelocity);
            } else if (Double.compare(pos.doubleValue(), bounds.getUpperBound()) > 0) {
                constrain(pos, vel, newPosition, newVelocity);
            } else {
                newPosition.add(pos);
                newVelocity.add(vel);
            }
        }

        entity.getProperties().put(EntityType.CANDIDATE_SOLUTION, newPosition.build());
        entity.getProperties().put(EntityType.Particle.VELOCITY, newVelocity.build());
        
        return entity;
    }

    /**
     * Constrain the position.
     * @param position The {@linkplain Numeric} representing the position.
     * @param velocity The {@linkplain Numeric} representing the velocity.
     */
    private void constrain(Numeric position, Numeric velocity, Vector.Builder newPosition, Vector.Builder newVelocity) {
        double previousPosition = position.doubleValue();
        Numeric pos = position.getClone();
        pos.randomize(random);
        newPosition.add(pos);
        newVelocity.add(position.doubleValue() - previousPosition);
    }
}
