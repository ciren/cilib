/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.functions.continuous.moo.wfg;

import com.google.common.base.Preconditions;
import java.util.Collections;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 */
public final class Transitions {

    private Transitions() {
    }

    public static Vector WFG1_t1(Vector y, int k) {
        int n = y.size();

        Preconditions.checkArgument(Misc.vector_in_01(y));
        Preconditions.checkArgument(k >= 1);
        Preconditions.checkArgument(k < n);

        Vector.Builder t = Vector.newBuilder();

        for (int i = 0; i < k; i++) {
            t.add(y.doubleValueOf(i));
        }

        for (int i = k; i < n; i++) {
            t.add(TransFunctions.s_linear(y.doubleValueOf(i), 0.35));
        }

        return t.build();
    }

    public static Vector WFG1_t2(Vector y, int k) {
        int n = y.size();

        Preconditions.checkArgument(Misc.vector_in_01(y));
        Preconditions.checkArgument(k >= 1);
        Preconditions.checkArgument(k < n);

        Vector.Builder t = Vector.newBuilder();

        for (int i = 0; i < k; i++) {
            t.add(y.doubleValueOf(i));
        }

        for (int i = k; i < n; i++) {
            t.add(TransFunctions.b_flat(y.doubleValueOf(i), 0.8, 0.75, 0.85));
        }

        return t.build();
    }

    public static Vector WFG1_t3(Vector y) {
        int n = y.size();

        Preconditions.checkArgument(Misc.vector_in_01(y));

        Vector.Builder t = Vector.newBuilder();

        for (int i = 0; i < n; i++) {
            t.add(TransFunctions.b_poly(y.doubleValueOf(i), 0.02));
        }

        return t.build();
    }

    public static Vector WFG1_t4(Vector y, int k, int M) {
        int n = y.size();

        Preconditions.checkArgument(Misc.vector_in_01(y));
        Preconditions.checkArgument(k >= 1);
        Preconditions.checkArgument(k < n);
        Preconditions.checkArgument(M >= 2);
        Preconditions.checkArgument(k % (M - 1) == 0);

        Vector.Builder w = Vector.newBuilder();

        for (int i = 1; i <= n; i++) {
            w.add(2.0 * i);
        }

        Vector w_vec = w.build();

        Vector.Builder t = Vector.newBuilder();

        for (int i = 1; i <= M - 1; i++) {
            int head = (i - 1) * k / (M - 1);
            int tail = i * k / (M - 1);

            Vector y_sub = y.copyOfRange(head, tail);
            Vector w_sub = w_vec.copyOfRange(head, tail);

            t.add(TransFunctions.r_sum(y_sub, w_sub));
        }

        Vector y_sub = y.copyOfRange(k, n);
        Vector w_sub = w_vec.copyOfRange(k, n);

        t.add(TransFunctions.r_sum(y_sub, w_sub));

        return t.build();
    }

    public static Vector WFG2_t2(Vector y, int k) {
        int n = y.size();
        int l = n - k;

        Preconditions.checkArgument(Misc.vector_in_01(y));
        Preconditions.checkArgument(k >= 1);
        Preconditions.checkArgument(k < n);
        Preconditions.checkArgument(l % 2 == 0);

        Vector.Builder t = Vector.newBuilder();

        for (int i = 0; i < k; i++) {
            t.add(y.doubleValueOf(i));
        }

        for (int i = k + 1; i <= k + l / 2; i++) {
            int head = k + 2 * (i - k) - 2;
            int tail = k + 2 * (i - k);

            t.add(TransFunctions.r_nonsep(y.copyOfRange(head, tail), 2));
        }

        return t.build();
    }

    public static Vector WFG2_t3(Vector y, int k, int M) {
        int n = y.size();

        Preconditions.checkArgument(Misc.vector_in_01(y));
        Preconditions.checkArgument(k >= 1);
        Preconditions.checkArgument(k < n);
        Preconditions.checkArgument(M >= 2);
        Preconditions.checkArgument(k % (M - 1) == 0);

        Vector w = Vector.copyOf(Collections.nCopies(n, 1.0));

        Vector.Builder t = Vector.newBuilder();

        for (int i = 1; i <= M - 1; i++) {
            int head = (i - 1) * k / (M - 1);
            int tail = i * k / (M - 1);

            Vector y_sub = y.copyOfRange(head, tail);
            Vector w_sub = w.copyOfRange(head, tail);

            t.add(TransFunctions.r_sum(y_sub, w_sub));
        }

        Vector y_sub = y.copyOfRange(k, n);
        Vector w_sub = w.copyOfRange(k, n);

        t.add(TransFunctions.r_sum(y_sub, w_sub));

        return t.build();
    }

    public static Vector WFG4_t1(Vector y) {
        int n = y.size();

        Preconditions.checkArgument(Misc.vector_in_01(y));

        Vector.Builder t = Vector.newBuilder();

        for (int i = 0; i < n; i++) {
            t.add(TransFunctions.s_multi(y.doubleValueOf(i), 30, 10, 0.35));
        }

        return t.build();
    }

    public static Vector WFG5_t1(Vector y) {
        int n = y.size();

        Preconditions.checkArgument(Misc.vector_in_01(y));

        Vector.Builder t = Vector.newBuilder();

        for (int i = 0; i < n; i++) {
            t.add(TransFunctions.s_decept(y.doubleValueOf(i), 0.35, 0.001, 0.05));
        }

        return t.build();
    }

    public static Vector WFG6_t2(Vector y, int k, int M) {
        int n = y.size();

        Preconditions.checkArgument(Misc.vector_in_01(y));
        Preconditions.checkArgument(k >= 1);
        Preconditions.checkArgument(k < n);
        Preconditions.checkArgument(M >= 2);
        Preconditions.checkArgument(k % (M - 1) == 0);

        Vector.Builder t = Vector.newBuilder();

        for (int i = 1; i <= M - 1; i++) {
            int head = (i - 1) * k / (M - 1);
            int tail = i * k / (M - 1);

            Vector y_sub = y.copyOfRange(head, tail);

            t.add(TransFunctions.r_nonsep(y_sub, k / (M - 1)));
        }

        Vector y_sub = y.copyOfRange(k, n);

        t.add(TransFunctions.r_nonsep(y_sub, n - k));

        return t.build();
    }

    public static Vector WFG7_t1(Vector y, int k) {
        int n = y.size();

        Preconditions.checkArgument(Misc.vector_in_01(y));
        Preconditions.checkArgument(k >= 1);
        Preconditions.checkArgument(k < n);

        Vector w = Vector.copyOf(Collections.nCopies(n, 1.0));

        Vector.Builder t = Vector.newBuilder();

        for (int i = 0; i < k; i++) {
            Vector y_sub = y.copyOfRange(i + 1, n);
            Vector w_sub = w.copyOfRange(i + 1, n);

            double u = TransFunctions.r_sum(y_sub, w_sub);

            t.add(TransFunctions.b_param(y.doubleValueOf(i), u, 0.98 / 49.98, 0.02, 50));
        }

        for (int i = k; i < n; i++) {
            t.add(y.doubleValueOf(i));
        }

        return t.build();
    }

    public static Vector WFG8_t1(Vector y, int k) {
        int n = y.size();

        Preconditions.checkArgument(Misc.vector_in_01(y));
        Preconditions.checkArgument(k >= 1);
        Preconditions.checkArgument(k < n);

        Vector w = Vector.copyOf(Collections.nCopies(n, 1.0));

        Vector.Builder t = Vector.newBuilder();

        for (int i = 0; i < k; i++) {
            t.add(y.doubleValueOf(i));
        }

        for (int i = k; i < n; i++) {
            Vector y_sub = y.copyOfRange(0, i);
            Vector w_sub = w.copyOfRange(0, i);

            double u = TransFunctions.r_sum(y_sub, w_sub);

            t.add(TransFunctions.b_param(y.doubleValueOf(i), u, 0.98 / 49.98, 0.02, 50));
        }

        return t.build();
    }

    public static Vector WFG9_t1(Vector y) {
        int n = y.size();

        Preconditions.checkArgument(Misc.vector_in_01(y));

        Vector w = Vector.copyOf(Collections.<Double>nCopies(n, 1.0));

        Vector.Builder t = Vector.newBuilder();

        for (int i = 0; i < n - 1; i++) {
            Vector y_sub = y.copyOfRange(i + 1, n);
            Vector w_sub = w.copyOfRange(i + 1, n);

            double u = TransFunctions.r_sum(y_sub, w_sub);

            t.add(TransFunctions.b_param(y.doubleValueOf(i), u, 0.98 / 49.98, 0.02, 50));
        }

        t.add(y.doubleValueOf(y.size() - 1));

        return t.build();
    }

    public static Vector WFG9_t2(Vector y, int k) {
        int n = y.size();

        Preconditions.checkArgument(Misc.vector_in_01(y));
        Preconditions.checkArgument(k >= 1);
        Preconditions.checkArgument(k < n);

        Vector.Builder t = Vector.newBuilder();

        for (int i = 0; i < k; i++) {
            t.add(TransFunctions.s_decept(y.doubleValueOf(i), 0.35, 0.001, 0.05));
        }

        for (int i = k; i < n; i++) {
            t.add(TransFunctions.s_multi(y.doubleValueOf(i), 30, 95, 0.35));
        }

        return t.build();
    }
}
