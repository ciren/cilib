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
public final class ShapeFunctions {

    private ShapeFunctions() {
    }

    public static boolean shape_args_ok(Vector x, int m) {
        int M = x.size();
        return Misc.vector_in_01(x) && m >= 1 && m <= M;
    }

    public static double linear(Vector x, int m) {
        Preconditions.checkArgument(shape_args_ok(x, m));

        int M = x.size();
        double result = 1.0;

        for (int i = 1; i <= M - m; i++) {
            result *= x.doubleValueOf(i - 1);
        }

        if (m != 1) {
            result *= 1 - x.doubleValueOf(M - m);
        }

        return Misc.correct_to_01(result);
    }

    public static double convex(Vector x, int m) {
        Preconditions.checkArgument(shape_args_ok(x, m));

        int M = x.size();
        double result = 1.0;

        for (int i = 1; i <= M - m; i++) {
            result *= 1.0 - Math.cos(x.doubleValueOf(i - 1) * Math.PI / 2.0);
        }

        if (m != 1) {
            result *= 1.0 - Math.sin(x.doubleValueOf(M - m) * Math.PI / 2.0);
        }

        return Misc.correct_to_01(result);
    }

    public static double concave(Vector x, int m) {
        Preconditions.checkArgument(shape_args_ok(x, m));

        int M = x.size();
        double result = 1.0;

        for (int i = 1; i <= M - m; i++) {
            result *= Math.sin(x.doubleValueOf(i - 1) * Math.PI / 2.0);
        }

        if (m != 1) {
            result *= Math.cos(x.doubleValueOf(M - m) * Math.PI / 2.0);
        }

        return Misc.correct_to_01(result);
    }

    public static double mixed(Vector x, int A, double alpha) {
        Preconditions.checkArgument(Misc.vector_in_01(x));
        Preconditions.checkArgument(!x.isEmpty());
        Preconditions.checkArgument(A >= 1);
        Preconditions.checkArgument(alpha > 0.0);

        double tmp = 2.0 * A * Math.PI;

        return Misc.correct_to_01(Math.pow(1.0 - x.doubleValueOf(0) - Math.cos(tmp * x.doubleValueOf(0) + Math.PI / 2.0) / tmp, alpha));
    }

    public static double disc(Vector x, int A, double alpha, double beta) {
        Preconditions.checkArgument(Misc.vector_in_01(x));
        Preconditions.checkArgument(!x.isEmpty());
        Preconditions.checkArgument(A >= 1);
        Preconditions.checkArgument(alpha > 0.0);
        Preconditions.checkArgument(beta > 0.0);

        double tmp1 = A * Math.pow(x.doubleValueOf(0), beta) * Math.PI;
        return Misc.correct_to_01(1.0 - Math.pow(x.doubleValueOf(0), alpha) * Math.pow(Math.cos(tmp1), 2.0));
    }
}
