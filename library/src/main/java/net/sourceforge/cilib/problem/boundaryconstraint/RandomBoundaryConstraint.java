/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.boundaryconstraint;

import java.util.Iterator;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
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
    }

    /**
     * Constrain the position.
     * @param position The {@linkplain Numeric} representing the position.
     * @param velocity The {@linkplain Numeric} representing the velocity.
     */
    private void constrain(Numeric position, Numeric velocity, Vector.Builder newPosition, Vector.Builder newVelocity) {
        double previousPosition = position.doubleValue();
        Numeric pos = position.getClone();
        pos.randomise();
        newPosition.add(pos);
        newVelocity.add(position.doubleValue() - previousPosition);
    }
}
