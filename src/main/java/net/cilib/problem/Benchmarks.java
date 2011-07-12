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
import fj.P2;
import fj.data.List;
import fj.function.Doubles;

/**
 * @author gpampara
 */
public final class Benchmarks {

    private Benchmarks() {
        throw new UnsupportedOperationException();
    }

    public static final F<Integer, Integer> succ = new F<Integer, Integer>() {
        public Integer f(Integer a) {
            return a + 1;
        }
    };

    public static final F<Integer, Integer> pred = new F<Integer, Integer>() {
        public Integer f(Integer a) {
            return a - 1;
        }
    };

    /**
     * Lifted identity function.
     */
    public static final F<Double, Double> identity = new F<Double, Double>() {
        public Double f(Double a) {
            return a;
        }
    };

    public static final F<Double, Double> abs = new F<Double, Double>() {
        public Double f(Double a) {
            return Math.abs(a);
        }
    };

    public static final F<Double, Double> square = new F<Double, Double>() {
        public Double f(Double a) {
            return a * a;
        }
    };

    public static final F<Double, Double> sqrt = new F<Double, Double>() {
        public Double f(Double a) {
            return Math.sqrt(a);
        }
    };

    public static final F<Double, Double> cos = new F<Double, Double>() {
        public Double f(Double a) {
            return Math.cos(a);
        }
    };

    public static final F<Double, Double> sin = new F<Double, Double>() {
        public Double f(Double a) {
            return Math.sin(a);
        }
    };

    public static final F<Double, Double> ceil = new F<Double, Double>() {
        public Double f(Double a) {
            return Math.ceil(a);
        }
    };

    public static final F<Double, Double> floor = new F<Double, Double>() {
        public Double f(Double a) {
            return Math.floor(a);
        }
    };

    // More complex functions - methods or instances?
    public static final F<Double, Double> alpine = new F<Double, Double>() {
        public Double f(Double a) {
            return abs.f(Doubles.multiply.f(a).f(sin.f(a))) + (0.1 * a);
        }
    };

    public static final F<Double, Double> quartic = square.o(square);

    /**
     * Generalized Rastrigin function. Although the function is "generalized" the
     * resulting implementation is definitely not "general". It is very specific.
     */
    public static final F<List<Double>, Double> rastrigin = new F<List<Double>, Double>() {
        public Double f(final List<Double> a) {
            return 1 + Doubles.sum(a.map(square)) * (1.0 / 4000)
                    - Doubles.product(a.zipIndex().map(new F<P2<Double, Integer>, Double>() {
                @Override
                public Double f(P2<Double, Integer> a) {
                    return cos.f(a._1() / sqrt.f(succ.f(a._2()).doubleValue()));
                }
            }));
        }
    };
}
