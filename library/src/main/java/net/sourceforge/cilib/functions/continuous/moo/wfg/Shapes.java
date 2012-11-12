/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.moo.wfg;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 */
public final class Shapes {

    private Shapes() {
    }

    public static Vector WFG_create_A(int M, boolean degenerate) {
        Preconditions.checkArgument(M >= 2);
        if (degenerate) {
            List<Double> A = Lists.newArrayList(Collections.nCopies(M - 1, 0.0));
            A.set(0, 1.0);
            return Vector.copyOf(A);
        } else {
            return Vector.copyOf(Collections.nCopies(M - 1, 1.0));
        }
    }

    public static Vector WFG_calculate_f(Vector x, Vector h) {
        Preconditions.checkArgument(Misc.vector_in_01(x));
        Preconditions.checkArgument(Misc.vector_in_01(h));
        Preconditions.checkArgument(x.size() == h.size());

        int M = h.size();

        Vector.Builder S = Vector.newBuilder();

        for (int m = 1; m <= M; m++) {
            S.add(m * 2.0);
        }

        return FrameworkFunctions.calculate_f(1.0, x, h, S.build());
    }

    public static Vector WFG1_shape(Vector t_p) {
        Preconditions.checkArgument(Misc.vector_in_01(t_p));
        Preconditions.checkArgument(t_p.size() >= 2);

        int M = t_p.size();

        Vector A = WFG_create_A(M, false);
        Vector x = FrameworkFunctions.calculate_x(t_p, A);

        Vector.Builder h = Vector.newBuilder();

        for (int m = 1; m <= M - 1; m++) {
            h.add(ShapeFunctions.convex(x, m));
        }
        h.add(ShapeFunctions.mixed(x, 5, 1.0));

        return WFG_calculate_f(x, h.build());
    }

    public static Vector WFG2_shape(Vector t_p) {
        Preconditions.checkArgument(Misc.vector_in_01(t_p));
        Preconditions.checkArgument(t_p.size() >= 2);

        int M = t_p.size();

        Vector A = WFG_create_A(M, false);
        Vector x = FrameworkFunctions.calculate_x(t_p, A);

        Vector.Builder h = Vector.newBuilder();

        for (int m = 1; m <= M - 1; m++) {
            h.add(ShapeFunctions.convex(x, m));
        }
        h.add(ShapeFunctions.disc(x, 5, 1.0, 1.0));

        return WFG_calculate_f(x, h.build());
    }

    public static Vector WFG3_shape(Vector t_p) {
        Preconditions.checkArgument(Misc.vector_in_01(t_p));
        Preconditions.checkArgument(t_p.size() >= 2);

        int M = t_p.size();

        Vector A = WFG_create_A(M, true);
        Vector x = FrameworkFunctions.calculate_x(t_p, A);

        Vector.Builder h = Vector.newBuilder();

        for (int m = 1; m <= M; m++) {
            h.add(ShapeFunctions.linear(x, m));
        }

        return WFG_calculate_f(x, h.build());
    }

    public static Vector WFG4_shape(Vector t_p) {
        Preconditions.checkArgument(Misc.vector_in_01(t_p));
        Preconditions.checkArgument(t_p.size() >= 2);

        int M = t_p.size();

        Vector A = WFG_create_A(M, false);
        Vector x = FrameworkFunctions.calculate_x(t_p, A);

        Vector.Builder h = Vector.newBuilder();

        for (int m = 1; m <= M; m++) {
            h.add(ShapeFunctions.concave(x, m));
        }

        return WFG_calculate_f(x, h.build());
    }
}
