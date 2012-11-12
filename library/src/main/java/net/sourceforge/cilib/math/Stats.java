/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.math;

import com.google.common.base.Function;
import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Vectors;

/**
 * Some simple methods for determining some statistics.
 *
 */
public final class Stats {

    private Stats() {
    }

    /**
     *
     * @param vector
     * @return
     */
    public static double mean(Vector vector) {
        double sum = 0.0;
        for (int i = 0; i < vector.size(); i++) {
            sum += vector.doubleValueOf(i);
        }

        double xbar = sum / vector.size();
        double correction = 0.0;
        for (int i = 0; i < vector.size(); i++) {
            correction += (vector.doubleValueOf(i) - xbar);
        }

        return xbar + (correction / vector.size());
    }


    /**
     *
     * @param vector
     * @return
     */
    public static double variance(final Vector vector) {
        double mean = mean(vector);
        double summation = 0.0;

        for (int i = 0; i < vector.size(); i++) {
            summation += (vector.doubleValueOf(i) - mean) * (vector.doubleValueOf(i) - mean);
        }

        return summation / vector.size();
    }

    /**
     *
     * @param vector
     * @return
     */
    public static double stdDeviation(final Vector vector) {
        return Math.sqrt(variance(vector));
    }

    public static double stdDeviation(Number... values) {
        return Math.sqrt(variance(Vector.of(values)));
    }
}
