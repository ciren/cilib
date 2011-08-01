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
import fj.Monoid;
import fj.data.List;
import fj.data.Option;

/**
 *
 * @author gpampara
 */
public abstract class Evaluatable {

    /**
     * Evaluate the provided <code>List</code>.
     * @param a list to evaluate
     * @return
     */
    public abstract Option<Double> evaluate(List<Double> a);

    public static Evaluatable lift(final F<Double, Double> f, final Monoid<Double> reducer) {
        return new Evaluatable() {
            @Override
            public Option<Double> evaluate(List<Double> a) {
                double d = reducer.sumLeft(a.map(f));
                return Double.isInfinite(d) || Double.isNaN(d) ? Option.<Double>none() : Option.some(d);
            }
        };
    }

    public static Evaluatable lift(final ListF f, final Monoid<Double> reducer) {
        return new Evaluatable() {
            @Override
            public Option<Double> evaluate(List<Double> a) {
                double d = reducer.sum(f.f(a), reducer.zero());
                return Double.isInfinite(d) || Double.isNaN(d) ? Option.<Double>none() : Option.some(d);
            }
        };
    }
}
