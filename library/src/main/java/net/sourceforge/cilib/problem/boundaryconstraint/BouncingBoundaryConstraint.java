/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
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
    public void enforce(Entity entity) {
        StructuredType<?> structuredType = (StructuredType<?>) entity.getProperties().get(EntityType.Particle.VELOCITY);

        if (structuredType == null) {
            throw new UnsupportedOperationException("Cannot perform this boundary constrain on a " + entity.getClass().getSimpleName());
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
        entity.getProperties().put(EntityType.Particle.VELOCITY, result);
    }
}
