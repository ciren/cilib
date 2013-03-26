/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
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
 * Particles that overstep the boundary get re-initialised to simulate
 * a bouncing effect by flipping the velocity (multiplying it with -1.0).
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
     * {@inheritDoc}
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
