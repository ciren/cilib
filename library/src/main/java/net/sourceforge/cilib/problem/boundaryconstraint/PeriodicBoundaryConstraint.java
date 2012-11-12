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
 * If a particle oversteps the upper boundary it gets re-initialised and placed near the lower
 * boundary and vice versa.
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
 * </pre>
 */
public class PeriodicBoundaryConstraint implements BoundaryConstraint {

    private static final long serialVersionUID = 6381401553771951793L;

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
            throw new UnsupportedOperationException("Cannot apply a ["
                    + this.getClass().getSimpleName()
                    + "] to an Entity that is not a Particle");
        }

        Vector.Builder positionBuilder = Vector.newBuilder();

        Iterator<?> i = entity.getCandidateSolution().iterator();
        Iterator<?> velocityIterator = velocity.iterator();

        for (; i.hasNext();) {
            Numeric v = (Numeric) velocityIterator.next();
            Numeric p = (Numeric) i.next();
            Bounds bounds = p.getBounds();
            double desiredPosition = p.doubleValue() + v.doubleValue();

            if (Double.compare(p.doubleValue(), bounds.getLowerBound()) < 0) {
                positionBuilder.add(bounds.getUpperBound() - (bounds.getLowerBound() - desiredPosition) % bounds.getRange());
            } else if (Double.compare(p.doubleValue(), bounds.getUpperBound()) > 0) {
                positionBuilder.add(bounds.getLowerBound() + (desiredPosition - bounds.getUpperBound()) % bounds.getRange());
            } else {
                positionBuilder.add(p);
            }
        }
        entity.getProperties().put(EntityType.CANDIDATE_SOLUTION, positionBuilder.build());
    }
}
