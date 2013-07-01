/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.measurement.single;

import net.cilib.algorithm.Algorithm;
import net.cilib.measurement.Measurement;
import net.cilib.type.types.Bounds;
import net.cilib.type.types.Numeric;
import net.cilib.type.types.Real;
import net.cilib.type.types.container.Vector;

/**
 * Checks of the best position in the current population violates
 * boundary constraints.
 *
 */
public class BestPositionBoundViolations implements Measurement<Real> {
    private static final long serialVersionUID = 8707987903689437725L;

    /**
     * {@inheritDoc}
     */
    @Override
    public BestPositionBoundViolations getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        Vector gbest = (Vector) algorithm.getBestSolution().getPosition();

        double numberOfViolations = 0.0;
        for (Numeric position : gbest) {
            Bounds bounds = position.getBounds();

            if (!bounds.isInsideBounds(position.doubleValue())) {
                numberOfViolations++;
                break;
            }
        }

        return Real.valueOf(numberOfViolations);
    }

}
