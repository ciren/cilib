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
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.StructuredType;

/**
 * {@linkplain net.sourceforge.cilib.entity.Particle Particles} that overstep the
 * boundary get re-initialised to simulate a bouncing effect by flipping the
 * velocity (multiplying it with -1.0).
 * @author Wiehann Matthysen
 */
public class DeflectionBoundaryConstraint implements BoundaryConstraint {
    private static final long serialVersionUID = 3678992841264721007L;

    private ControlParameter velocityDampingFactor;

    /**
     * Create a new instance. The {@code velocityDampingFactor} defaults to a
     * value of -1.0.
     */
    public DeflectionBoundaryConstraint() {
        this.velocityDampingFactor = new ConstantControlParameter(-1.0);
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
            double desiredPosition = position.getReal() + velocity.getReal();

            if (Double.compare(position.getReal(), bounds.getLowerBound()) < 0) {
                position.set(bounds.getLowerBound() + (bounds.getLowerBound() - desiredPosition) % bounds.getRange());
                velocity.set(velocity.getReal() * velocityDampingFactor.getParameter());
            }
            else if (Double.compare(position.getReal(), bounds.getUpperBound()) > 0) {
                position.set(bounds.getUpperBound() - (desiredPosition - bounds.getUpperBound()) % bounds.getRange());
                velocity.set(velocity.getReal() * velocityDampingFactor.getParameter());
            }
        }
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
