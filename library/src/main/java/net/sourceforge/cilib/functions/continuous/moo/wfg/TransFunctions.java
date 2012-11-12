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
public final class TransFunctions {

    private TransFunctions() {
    }

    public static double b_poly(double y, double alpha) {
        Preconditions.checkArgument(y >= 0.0);
        Preconditions.checkArgument(y <= 1.0);
        Preconditions.checkArgument(alpha > 0.0);
        Preconditions.checkArgument(alpha != 1.0);

        return Misc.correct_to_01(Math.pow(y, alpha));
    }

    public static double b_flat(double y, double A, double B, double C) {
        Preconditions.checkArgument(y >= 0.0);
        Preconditions.checkArgument(y <= 1.0);
        Preconditions.checkArgument(A >= 0.0);
        Preconditions.checkArgument(A <= 1.0);
        Preconditions.checkArgument(B >= 0.0);
        Preconditions.checkArgument(B <= 1.0);
        Preconditions.checkArgument(C >= 0.0);
        Preconditions.checkArgument(C <= 1.0);
        Preconditions.checkArgument(B < C);
        Preconditions.checkArgument(B != 0.0 || A == 0.0);
        Preconditions.checkArgument(B != 0.0 || C != 1.0);
        Preconditions.checkArgument(C != 1.0 || A == 1.0);
        Preconditions.checkArgument(C != 1.0 || B != 0.0);

        double tmp1 = Math.min(0.0, Math.floor(y - B)) * A * (B - y) / B;
        double tmp2 = Math.min(0.0, Math.floor(C - y)) * (1.0 - A) * (y - C) / (1.0 - C);

        return Misc.correct_to_01(A + tmp1 - tmp2);
    }

    public static double b_param(double y, double u, double A, double B, double C) {
        Preconditions.checkArgument(y >= 0.0);
        Preconditions.checkArgument(y <= 1.0);
        Preconditions.checkArgument(u >= 0.0);
        Preconditions.checkArgument(u <= 1.0);
        Preconditions.checkArgument(A > 0.0);
        Preconditions.checkArgument(A < 1.0);
        Preconditions.checkArgument(B > 0.0);
        Preconditions.checkArgument(B < C);

        double v = A - (1.0 - 2.0 * u) * Math.abs(Math.floor(0.5 - u) + A);

        return Misc.correct_to_01(Math.pow(y, B + (C - B) * v));
    }

    public static double s_linear(double y, double A) {
        Preconditions.checkArgument(y >= 0.0);
        Preconditions.checkArgument(y <= 1.0);
        Preconditions.checkArgument(A > 0.0);
        Preconditions.checkArgument(A < 1.0);

        return Misc.correct_to_01(Math.abs(y - A) / Math.abs(Math.floor(A - y) + A));
    }

    public static double s_decept(double y, double A, double B, double C) {
        Preconditions.checkArgument(y >= 0.0);
        Preconditions.checkArgument(y <= 1.0);
        Preconditions.checkArgument(A > 0.0);
        Preconditions.checkArgument(A < 1.0);
        Preconditions.checkArgument(B > 0.0);
        Preconditions.checkArgument(B < 1.0);
        Preconditions.checkArgument(C > 0.0);
        Preconditions.checkArgument(C < 1.0);
        Preconditions.checkArgument(A - B > 0.0);
        Preconditions.checkArgument(A + B < 1.0);

        double tmp1 = Math.floor(y - A + B) * (1.0 - C + (A - B) / B) / (A - B);
        double tmp2 = Math.floor(A + B - y) * (1.0 - C + (1.0 - A - B) / B) / (1.0 - A - B);

        return Misc.correct_to_01(1.0 + (Math.abs(y - A) - B) * (tmp1 + tmp2 + 1.0 / B));
    }

    public static double s_multi(double y, int A, double B, double C) {
        Preconditions.checkArgument(y >= 0.0);
        Preconditions.checkArgument(y <= 1.0);
        Preconditions.checkArgument(A >= 1);
        Preconditions.checkArgument(B >= 0.0);
        Preconditions.checkArgument((4.0 * A + 2.0) * Math.PI >= 4.0 * B);
        Preconditions.checkArgument(C > 0.0);
        Preconditions.checkArgument(C < 1.0);

        double tmp1 = Math.abs(y - C) / (2.0 * (Math.floor(C - y) + C));
        double tmp2 = (4.0 * A + 2.0) * Math.PI * (0.5 - tmp1);

        return Misc.correct_to_01((1.0 + Math.cos(tmp2) + 4.0 * B * Math.pow(tmp1, 2.0)) / (B + 2.0));
    }

    public static double r_sum(Vector y, Vector w) {
        Preconditions.checkArgument(!y.isEmpty());
        Preconditions.checkArgument(w.size() == y.size());
        Preconditions.checkArgument(Misc.vector_in_01(y));

        double numerator = 0.0;
        double denominator = 0.0;

        for (int i = 0; i < y.size(); i++) {
            Preconditions.checkArgument(w.doubleValueOf(i) > 0.0);

            numerator += w.doubleValueOf(i) * y.doubleValueOf(i);
            denominator += w.doubleValueOf(i);
        }

        return Misc.correct_to_01(numerator / denominator);
    }

    public static double r_nonsep(Vector y, int A) {
        int y_len = y.size();

        Preconditions.checkArgument(y_len != 0);
        Preconditions.checkArgument(Misc.vector_in_01(y));
        Preconditions.checkArgument(A >= 1);
        Preconditions.checkArgument(A <= y_len);
        Preconditions.checkArgument(y.size() % A == 0);

        double numerator = 0.0;

        for (int j = 0; j < y_len; j++) {
            numerator += y.doubleValueOf(j);

            for (int k = 0; k <= A - 2; k++) {
                numerator += Math.abs(y.doubleValueOf(j) - y.doubleValueOf((j + k + 1) % y_len));
            }
        }

        double tmp = Math.ceil(A / 2.0);
        double denominator = y_len * tmp * (1.0 + 2.0 * A - 2.0 * tmp) / A;

        return Misc.correct_to_01(numerator / denominator);
    }
}
