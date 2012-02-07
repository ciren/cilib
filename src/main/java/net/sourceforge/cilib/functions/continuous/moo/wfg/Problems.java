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
 * @author Wiehann Matthysen
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
