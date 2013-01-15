/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.distancemeasure;

/**
 * Euclidean Distance is a special case of the {@linkplain MinkowskiMetric} with
 * <code>alpha = 2</code>.
 *
 */
public class EuclideanDistanceMeasure extends MinkowskiMetric {

    /**
     * Create an instance of the {@linkplain EuclideanDistanceMeasure}.
     */
    public EuclideanDistanceMeasure() {
        super(2);
    }
}
