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
package net.cilib.predef;

import com.google.common.collect.Lists;
import fj.F;
import fj.P1;
import fj.data.Either;
import fj.data.List;
import fj.data.Option;
import fj.function.Doubles;

/**
 * Utility class of predefined helpers.
 */
public final class Predef {

    private Predef() {
        throw new UnsupportedOperationException();
    }

    /**
     * Create a "<i>fitness</i>". In this case a "fitness" is nothing more than
     * a simple {@link Option} that has a value.
     * <p/>
     * This factory method is purely for convenience and has questionable value.
     *
     * @param value fitness value
     * @return an {@code Option} containing the fitness value.
     */
    public static Option<Double> fitness(double value) {
        return Option.some(value);
    }

//    /**
//     * Create an "<i>inferior fitness</i>". In this case a "fitness" is nothing
//     * more than the none option type.
//     * <p/>
//     * This factory method is purely for convenience and has questionable value.
//     *
//     * @return an {@code Option} representing an unspecified fitness value.
//     */
////    public static Option<Double> inferior() {
////        return Option.none();
////    }

    public static List<Double> solution(final double first, final double... rest) {
        java.util.List<Double> tmp = Lists.newArrayList(first);
        for (Double d : rest) {
            tmp.add(d);
        }
        return List.iterableList(tmp);
    }

    public static List<Double> velocity(final double first, final double... rest) {
        return solution(first, rest);
    }

    public static List<Double> plus(List<Double> la, List<Double> lb) {
        return la.zipWith(lb, Doubles.add);
    }

    public static List<Double> subtract(List<Double> a, List<Double> b) {
        return a.zipWith(b, Doubles.subtract);
    }

    public static List<Double> multiply(final P1<Double> p, final List<Double> a) {
        return a.map(Doubles.multiply.andThen(new F<F<Double, Double>, Double>() {
            @Override
            public Double f(F<Double, Double> a) {
                return a.f(p._1());
            }
        }));
    }

    public static List<Double> multiply(final Double constant, final List<Double> a) {
        return a.map(Doubles.multiply.f(constant));
    }

    /**
     * Divide each component within the given mutable sequence by the provided
     * scalar constant. Division by {@code 0.0} is checked and an exception
     * is then raised, if needed.
     *
     * @param scalar the value to apply to each component.
     * @param a      the sequence to divide.
     * @return the altered mutable sequence.
     */
    public static List<Double> divide(double scalar, List<Double> a) {
        Either<String, Double> z = divide(1.0, scalar)._1();
        if (z.isLeft()) {
            throw new Error(z.left().value());
        }
        return a.map(Doubles.multiply.f(z.right().value()));
    }

    private static P1<Either<String, Double>> divide(final double x, final double y) {
        return new P1<Either<String, Double>>() {
            @Override
            public Either<String, Double> _1() {
                double z = x / y;
                if (Double.isInfinite(z) || Double.isNaN(z)) {
                    return Either.left("Division by 0.0 not allowed.");
                }
                else {
                    return Either.right(z);
                }
            }
        };
    }
}
