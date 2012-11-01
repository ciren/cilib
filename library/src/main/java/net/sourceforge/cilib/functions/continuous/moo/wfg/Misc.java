/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.moo.wfg;

import com.google.common.base.Preconditions;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 */
public final class Misc {

    private Misc() {
    }

    public static double correct_to_01(double a, double epsilon) {
        Preconditions.checkArgument(epsilon >= 0.0);

        double min = 0.0;
        double max = 1.0;

        double min_epsilon = min - epsilon;
        double max_epsilon = max + epsilon;

        if (a <= min && a >= min_epsilon) {
            return min;
        } else if (a >= max && a <= max_epsilon) {
            return max;
        } else {
            return a;
        }
    }

    public static double correct_to_01(double a) {
        return correct_to_01(a, 1.0e-10);
    }

    public static boolean vector_in_01(Vector x) {
        for (int i = 0; i < x.size(); i++) {
            if (x.doubleValueOf(i) < 0.0 || x.doubleValueOf(i) > 1.0) {
                return false;
            }
        }
        return true;
    }
}
