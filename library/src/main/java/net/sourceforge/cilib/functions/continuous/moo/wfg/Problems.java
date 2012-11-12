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
public final class Problems {

    private Problems() {
    }

    public static boolean ArgsOK(Vector z, int k, int M) {
        int n = z.size();
        return k >= 1 && k < n && M >= 2 && k % (M - 1) == 0;
    }

    public static Vector WFG_normalise_z(Vector z) {
        Vector.Builder result = Vector.newBuilder();

        for (int i = 0; i < z.size(); i++) {
            double bound = 2.0 * (i + 1);
            Preconditions.checkArgument(z.doubleValueOf(i) >= 0.0);
            Preconditions.checkArgument(z.doubleValueOf(i) <= bound);
            result.add(z.doubleValueOf(i) / bound);
        }

        return result.build();
    }

    public static Vector WFG1(Vector z, int k, int M) {
        Preconditions.checkArgument(ArgsOK(z, k, M));

        Vector y = WFG_normalise_z(z);

        y = Transitions.WFG1_t1(y, k);
        y = Transitions.WFG1_t2(y, k);
        y = Transitions.WFG1_t3(y);
        y = Transitions.WFG1_t4(y, k, M);

        return Shapes.WFG1_shape(y);
    }

    public static Vector WFG2(Vector z, int k, int M) {
        Preconditions.checkArgument(ArgsOK(z, k, M));
        Preconditions.checkArgument(((z.size()) - k) % 2 == 0);

        Vector y = WFG_normalise_z(z);

        y = Transitions.WFG1_t1(y, k);
        y = Transitions.WFG2_t2(y, k);
        y = Transitions.WFG2_t3(y, k, M);

        return Shapes.WFG2_shape(y);
    }

    public static Vector WFG3(Vector z, int k, int M) {
        Preconditions.checkArgument(ArgsOK(z, k, M));
        Preconditions.checkArgument(((z.size()) - k) % 2 == 0);

        Vector y = WFG_normalise_z(z);

        y = Transitions.WFG1_t1(y, k);
        y = Transitions.WFG2_t2(y, k);
        y = Transitions.WFG2_t3(y, k, M);

        return Shapes.WFG3_shape(y);
    }

    public static Vector WFG4(Vector z, int k, int M) {
        Preconditions.checkArgument(ArgsOK(z, k, M));

        Vector y = WFG_normalise_z(z);

        y = Transitions.WFG4_t1(y);
        y = Transitions.WFG2_t3(y, k, M);

        return Shapes.WFG4_shape(y);
    }

    public static Vector WFG5(Vector z, int k, int M) {
        Preconditions.checkArgument(ArgsOK(z, k, M));

        Vector y = WFG_normalise_z(z);

        y = Transitions.WFG5_t1(y);
        y = Transitions.WFG2_t3(y, k, M);

        return Shapes.WFG4_shape(y);
    }

    public static Vector WFG6(Vector z, int k, int M) {
        Preconditions.checkArgument(ArgsOK(z, k, M));

        Vector y = WFG_normalise_z(z);

        y = Transitions.WFG1_t1(y, k);
        y = Transitions.WFG6_t2(y, k, M);

        return Shapes.WFG4_shape(y);
    }

    public static Vector WFG7(Vector z, int k, int M) {
        Preconditions.checkArgument(ArgsOK(z, k, M));

        Vector y = WFG_normalise_z(z);

        y = Transitions.WFG7_t1(y, k);
        y = Transitions.WFG1_t1(y, k);
        y = Transitions.WFG2_t3(y, k, M);

        return Shapes.WFG4_shape(y);
    }

    public static Vector WFG8(Vector z, int k, int M) {
        Preconditions.checkArgument(ArgsOK(z, k, M));

        Vector y = WFG_normalise_z(z);

        y = Transitions.WFG8_t1(y, k);
        y = Transitions.WFG1_t1(y, k);
        y = Transitions.WFG2_t3(y, k, M);

        return Shapes.WFG4_shape(y);
    }

    public static Vector WFG9(Vector z, int k, int M) {
        Preconditions.checkArgument(ArgsOK(z, k, M));

        Vector y = WFG_normalise_z(z);

        y = Transitions.WFG9_t1(y);
        y = Transitions.WFG9_t2(y, k);
        y = Transitions.WFG6_t2(y, k, M);

        return Shapes.WFG4_shape(y);
    }
}
