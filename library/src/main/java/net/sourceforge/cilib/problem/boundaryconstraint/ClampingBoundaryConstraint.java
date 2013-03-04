/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.boundaryconstraint;

import com.google.common.base.Function;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.Maths;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;

/**
 * Prevent any {@link Entity} from over-shooting the problem search space. Any
 * Entity that passes outside the search space is placed on the boundaries of
 * the search space.
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
        StructuredType<?> candidateSolution = entity.getCandidateSolution();
        Vector result = Vectors.transform((Vector) candidateSolution, new Function<Numeric, Double>() {

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
        entity.setCandidateSolution(result);
    }
}
