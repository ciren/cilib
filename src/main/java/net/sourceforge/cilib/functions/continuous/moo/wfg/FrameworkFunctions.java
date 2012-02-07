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
public final class FrameworkFunctions {

    private FrameworkFunctions() {
    }

    public static Vector normalise_z(Vector z, Vector z_max) {
        Vector.Builder result = Vector.newBuilder();
        for (int i = 0; i < z.size(); i++) {
            Preconditions.checkArgument(z.doubleValueOf(i) >= 0.0);
            Preconditions.checkArgument(z.doubleValueOf(i) <= z_max.doubleValueOf(i));
            Preconditions.checkArgument(z_max.doubleValueOf(i) > 0.0);
            result.add(z.doubleValueOf(i) / z_max.doubleValueOf(i));
        }
        return result.build();
    }

    public static Vector calculate_x(Vector t_p, Vector A) {
        Preconditions.checkArgument(Misc.vector_in_01(t_p));
        Preconditions.checkArgument(!t_p.isEmpty());
        Preconditions.checkArgument(A.size() == t_p.size() - 1);

        Vector.Builder result = Vector.newBuilder();

        for (int i = 0; i < t_p.size() - 1; i++) {
            Preconditions.checkArgument(A.doubleValueOf(i) == 0 || A.doubleValueOf(i) == 1);
            double tmp1 = Math.max(t_p.doubleValueOf(t_p.size() - 1), A.doubleValueOf(i));
            result.add(tmp1 * (t_p.doubleValueOf(i) - 0.5) + 0.5);
        }

        result.add(t_p.doubleValueOf(t_p.size() - 1));

        return result.build();
    }

    public static Vector calculate_f(double D, Vector x, Vector h, Vector S) {
        Preconditions.checkArgument(D > 0.0);
        Preconditions.checkArgument(Misc.vector_in_01(x));
        Preconditions.checkArgument(Misc.vector_in_01(h));
        Preconditions.checkArgument(x.size() == h.size());
        Preconditions.checkArgument(h.size() == S.size());

        Vector.Builder result = Vector.newBuilder();

        for (int i = 0; i < h.size(); i++) {
            Preconditions.checkArgument(S.doubleValueOf(i) > 0.0);
            result.add(D * x.doubleValueOf(x.size() - 1) + S.doubleValueOf(i) * h.doubleValueOf(i));
        }

        return result.build();
    }
}
