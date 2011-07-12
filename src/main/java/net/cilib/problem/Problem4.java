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

import fj.F4;
import fj.data.List;
import fj.data.Option;
import fj.function.Doubles;

/**
 * This is not pretty :/ but I cannot see another way of doing it
 * @author gpampara
 */
final class Problem4 implements Evaluatable {

    private final F4<Double, Double, Double, Double, Double> f;

    public Problem4(F4<Double, Double, Double, Double, Double> f) {
        this.f = f;
    }

    @Override
    public final Option<Double> eval(List<Double> a) {
        try {
            final List<List<Double>> params = a.partition(3);
            final List.Buffer<Double> bs = List.Buffer.empty();
            for (List<Double> param : params) {
                List<Double> xs = param;
                Double _a = xs.head(); xs = xs.tail();
                Double _b = xs.head(); xs = xs.tail();
                Double _c = xs.head(); xs = xs.tail();
                Double _d = xs.head();
                bs.snoc(f.f(_a, _b, _c, _d));
            }
            return Option.some(bs.toList().foldLeft(Doubles.add, 0.0));
        } catch (Exception e) {
        }
        return Option.none();
    }
}
