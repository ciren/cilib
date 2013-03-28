/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.distancemeasure;

/**
 * Manhattan Distance is a special case of the {@link MinkowskiMetric} with
 * 'alpha' = 1.
 */
public class ManhattanDistanceMeasure extends MinkowskiMetric {

    /**
     * Create an instance of the {@linkplain ManhattanDistanceMeasure}.
     */
    public ManhattanDistanceMeasure() {
        super(1);
    }
}
