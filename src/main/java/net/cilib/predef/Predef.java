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

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import fj.F;
import fj.F2;
import fj.data.List;
import fj.data.Option;

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

    /**
     * Create an "<i>inferior fitness</i>". In this case a "fitness" is nothing
     * more than the none option type.
     * <p/>
     * This factory method is purely for convenience and has questionable value.
     *
     * @return an {@code Option} representing an unspecified fitness value.
     */
    public static Option<Double> inferior() {
        return Option.none();
    }

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
        F2<Double, Double, Double> plus = new F2<Double, Double, Double>() {

            @Override
            public Double f(Double a, Double b) {
                return a + b;
            }
        };
        return la.zipWith(lb, plus);
    }

    public static List<Double> subtract(List<Double> a, List<Double> b) {
        F2<Double, Double, Double> subtract = new F2<Double, Double, Double>() {

            @Override
            public Double f(Double a, Double b) {
                return a - b;
            }
        };
        return a.zipWith(b, subtract);
    }

    public static List<Double> multiply(final Supplier<Double> supplier, final List<Double> a) {
        return a.map(new F<Double, Double>() {

            @Override
            public Double f(Double a) {
                return a * supplier.get();
            }
        });
    }

    public static List<Double> multiply(final Double constant, final List<Double> a) {
        return multiply(Suppliers.ofInstance(constant), a);
    }

    /**
     * Divide each component within the given mutable sequence by the provided
     * scalar constant. Division by {@code 0.0} is checked and an exception
     * is then raised, if needed.
     *
     * @param scalar the value to apply to each component.
     * @param seq    the sequence to divide.
     * @return the altered mutable sequence.
     */
    public static List<Double> divide(double scalar, List<Double> seq) {
        return divide(Suppliers.ofInstance(scalar), seq);
    }

    /**
     * Divide each component within the given mutable sequence by the values provided
     * from the given {@code Supplier}. Division by {@code 0.0} is checked,
     * resulting in a {@link ArithmeticException} when violated.
     *
     * @param supplier source of scalar values.
     * @param seq      the sequence to divide.
     * @return the altered mutable sequence.
     */
    public static List<Double> divide(final Supplier<Double> supplier, List<Double> seq) {
        final CheckingSupplier check = new CheckingSupplier(supplier);
        return seq.map(new F<Double, Double>() {

            @Override
            public Double f(Double a) {
                return a / check.get();
            }
        });
    }

    private static final class CheckingSupplier implements Supplier<Double> {

        private final Supplier<Double> supplier;

        private CheckingSupplier(Supplier<Double> supplier) { // Accept a function / closure
            this.supplier = Suppliers.memoize(supplier);
        }

        @Override
        public Double get() {
            double scalar = supplier.get();
            if (Doubles.compare(scalar, 0.0) == 0) {
                throw new ArithmeticException("Cannot divide with a 0.0!");
            }
            return scalar;
        }
    }
}
