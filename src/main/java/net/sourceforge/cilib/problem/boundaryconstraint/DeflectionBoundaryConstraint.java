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
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * {@linkplain net.sourceforge.cilib.entity.Particle Particles} that overstep the
 * boundary get re-initialised to simulate a bouncing effect by flipping the
 * velocity (multiplying it with -1.0).
 */
public class DeflectionBoundaryConstraint implements BoundaryConstraint {

    private static final long serialVersionUID = 3678992841264721007L;
    private ControlParameter velocityDampingFactor;

    /**
     * Create a new instance. The {@code velocityDampingFactor} defaults to a
     * value of -1.0.
     */
    public DeflectionBoundaryConstraint() {
        this.velocityDampingFactor = ConstantControlParameter.of(-1.0);
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public DeflectionBoundaryConstraint(DeflectionBoundaryConstraint copy) {
        this.velocityDampingFactor = copy.velocityDampingFactor.getClone();
    }

    /**
     * {@inheritDo}
     */
    @Override
    public BoundaryConstraint getClone() {
        return new DeflectionBoundaryConstraint(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Entity enforce(Entity oldEntity) {
        Entity entity = oldEntity.getClone();
        StructuredType<?> structuredType = (StructuredType<?>) entity.getProperties().get(EntityType.Particle.VELOCITY);

        if (structuredType == null) {
            throw new UnsupportedOperationException("Cannot perform this boundary constrain on a "
                    + entity.getClass().getSimpleName());
        }

        Vector.Builder newVelocity = Vector.newBuilder();
        Vector.Builder newPosition = Vector.newBuilder();

        Iterator<?> pIterator = entity.getCandidateSolution().iterator();
        Iterator<?> vIterator = structuredType.iterator();

        while (pIterator.hasNext()) {
            Numeric position = (Numeric) pIterator.next();
            Numeric velocity = (Numeric) vIterator.next();
            Bounds bounds = position.getBounds();
            double desiredPosition = position.doubleValue() + velocity.doubleValue();

            if (Double.compare(position.doubleValue(), bounds.getLowerBound()) < 0) {
                newPosition.add(bounds.getLowerBound() + (bounds.getLowerBound() - desiredPosition) % bounds.getRange());
                newVelocity.add(velocity.doubleValue() * velocityDampingFactor.getParameter());
            } else if (Double.compare(position.doubleValue(), bounds.getUpperBound()) > 0) {
                newPosition.add(bounds.getUpperBound() - (desiredPosition - bounds.getUpperBound()) % bounds.getRange());
                newVelocity.add(velocity.doubleValue() * velocityDampingFactor.getParameter());
            } else {
                newPosition.add(position);
                newVelocity.add(velocity);
            }
        }

        entity.getProperties().put(EntityType.CANDIDATE_SOLUTION, newPosition.build());
        entity.getProperties().put(EntityType.Particle.VELOCITY, newVelocity.build());
        
        return entity;
    }

    /**
     * Get the current velocity dampening factor.
     * @return The {@code ControlParameter} representing the parameter.
     */
    public ControlParameter getVelocityDampingFactor() {
        return velocityDampingFactor;
    }

    /**
     * Set the control parameter to be used for the {@code velocity damping factor}.
     * @param velocityDampingFactor The {@code ControlParameter} to set.
     */
    public void setVelocityDampingFactor(ControlParameter velocityDampingFactor) {
        this.velocityDampingFactor = velocityDampingFactor;
    }
}
