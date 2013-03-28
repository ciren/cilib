/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.distancemeasure;

import java.util.Collection;
import java.util.Iterator;
import net.sourceforge.cilib.type.types.Numeric;

/**
 * Chebyshev Distance is a special case of the {@link MinkowskiMetric} with
 * 'alpha' := infinity. It calculates the distance between to vectors as the
 * largest coordinate difference between both vectors.
 */
public class ChebyshevDistanceMeasure extends MinkowskiMetric {

    /**
     * Create an instance of the {@linkplain ChebyshevDistanceMeasure}.
     */
    public ChebyshevDistanceMeasure() {
        // alpha cannot be directly instantiated to infinity :-)
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double distance(Collection<? extends Numeric> x, Collection<? extends Numeric> y) {
        /*
         * TODO: Consider re-implementing for different sized vectors, especially as everything is
         * equivalent relative to infinity
         */
        if (x.size() != y.size()) {
            throw new IllegalArgumentException("Cannot calculate Chebyshev Metric for vectors of different dimensions");
        }

        Iterator<? extends Numeric> xIterator = x.iterator();
        Iterator<? extends Numeric> yIterator = y.iterator();

        double maxDistance = 0.0;
        for (int i = 0; i < x.size(); ++i) {
            Numeric xElement = (Numeric) xIterator.next();
            Numeric yElement = (Numeric) yIterator.next();

            double distance = Math.abs(xElement.doubleValue() - yElement.doubleValue());
            if (distance > maxDistance) {
                maxDistance = distance;
            }
        }

        return maxDistance;
    }

    /**
     * {@inheritDoc}
     */
    public void setAlpha(int a) {
        throw new IllegalArgumentException("The 'alpha' parameter of the Chebyshev Distance Measure cannot be set directly");
    }
}
