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
package net.cilib.problem;

import fj.F;
import fj.F2;
import fj.F3;
import fj.F4;
import fj.F5;
import fj.data.List;

/**
 * TODO: These methods need a manner in which the desired strategy for partitioning
 * should be provided.
 * 
 * @author gpampara
 */
public final class Evaluators {

    private Evaluators() {
        throw new UnsupportedOperationException();
    }

    public static Evaluatable create1(F<Double, Double> f) {
        return new Problem1(f);
    }

    public static Evaluatable create2(F2<Double, Double, Double> f) {
        return new Problem2(f);
    }

    public static Evaluatable create3(F3<Double, Double, Double, Double> f) {
        return new Problem3(f);
    }

    public static Evaluatable create4(F4<Double, Double, Double, Double, Double> f) {
        return new Problem4(f);
    }

    public static Evaluatable create5(F5<Double, Double, Double, Double, Double, Double> f) {
        return new Problem5(f);
    }

    public static Evaluatable createL(F<List<Double>, Double> f) {
        return new ProblemDirect(f);
    }
}
