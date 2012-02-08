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
