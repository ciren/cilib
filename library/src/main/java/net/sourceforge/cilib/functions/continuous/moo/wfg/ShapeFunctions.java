/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
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
