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
import fj.Monoid;
import fj.P2;
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

    /**
     * Lift the provided {@link F} into a {@code Evaluatable}.
     * @param f the function to lift.
     * @param reducer the monoid instance to apply in the evaluation
     * @return an optional value representing the fitness
     */
    public static Evaluatable lift(final F<Double, Double> f, final Monoid<Double> reducer) {
        return new Evaluatable() {
            @Override
            public Option<Double> evaluate(List<Double> a) {
                double d = reducer.sumLeft(a.map(f));
                return Double.isInfinite(d) || Double.isNaN(d) ? Option.<Double>none() : Option.some(d);
            }
        };
    }

    public static Evaluatable lift(final F2<Double, Double, Double> f, final Monoid<Double> reducer) {
        return new Evaluatable() {
            @Override
            public Option<Double> evaluate(List<Double> a) {
                List<P2<Double, Double>> zipped = a.zip(a.tail());
                double d = reducer.sumLeft(zipped.map(f.tuple()));
                return Double.isInfinite(d) || Double.isNaN(d) ? Option.<Double>none() : Option.some(d);
            }
        };
    }

    public static Evaluatable lift(final F3<Double, Double, Double, Double> f, final Monoid<Double> reducer) {
        return new Evaluatable() {
            @Override
            public Option<Double> evaluate(List<Double> a) {
                List<Double> b = a.tail();
                List<P2<P2<Double, Double>, Double>> zipped = a.zip(b).zip(b.tail());
                double d = reducer.sumLeft(zipped.map(new F<P2<P2<Double, Double>, Double>, Double>() {
                    @Override
                    public Double f(P2<P2<Double, Double>, Double> a) {
                        return f.f(a._1()._1(), a._1()._2(), a._2());
                    }
                }));
                return Double.isInfinite(d) || Double.isNaN(d) ? Option.<Double>none() : Option.some(d);
            }
        };
    }

    public static Evaluatable lift(final F4<Double, Double, Double, Double, Double> f, final Monoid<Double> reducer) {
        return new Evaluatable() {
            @Override
            public Option<Double> evaluate(List<Double> a) {
                List<Double> b = a.tail();
                List<Double> c = b.tail();
                List<P2<P2<P2<Double, Double>,Double>, Double>> zipped = a.zip(b).zip(c).zip(c.tail());
                double d = reducer.sumLeft(zipped.map(new F<P2<P2<P2<Double, Double>, Double>, Double>, Double>() {
                    @Override
                    public Double f(P2<P2<P2<Double, Double>, Double>, Double> a) {
                        return f.f(a._1()._1()._1(), a._1()._1()._2(), a._1()._2(), a._2());
                    }

                }));
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
