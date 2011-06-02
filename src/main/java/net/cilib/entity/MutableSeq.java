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
package net.cilib.entity;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.primitives.Doubles;
import fj.F;
import fj.F2;
import fj.data.Array;
import fj.data.Stream;

import java.util.Iterator;
import java.util.List;

/**
 * A mutable sequence with mutation methods defined.
 *
 * @author gpampara
 */
public final class MutableSeq implements Seq {
    private final Array<Double> internal;

    public MutableSeq(final LinearSeq seq) {
        List<Double> list = Doubles.asList(seq.toArray());
        this.internal = Array.iterableArray(list);
    }

    private MutableSeq(final Array<Double> stream) {
        this.internal = stream;
    }

    public MutableSeq subtract(Seq seq) {
        return zipWith(seq.toMutableSeq().internal, new F2<Double, Double, Double>() {
            @Override
            public Double f(Double a, Double b) {
                return a - b;
            }
        });
    }

    private final MutableSeq zipWith(Array<Double> s, F2<Double, Double, Double> f) {
        return new MutableSeq(internal.zipWith(s, f));
    }

    public MutableSeq plus(Seq seq) {
        return zipWith(seq.toMutableSeq().internal, new F2<Double, Double, Double>() {
            @Override
            public Double f(Double a, Double b) {
                return a + b;
            }
        });
    }

    @Override
    public final Iterator<Double> iterator() {
        return this.internal.iterator(); // This delegates to the safe iterator in Stream#toCollection()
    }

    @Override
    public final MutableSeq toMutableSeq() {
        return this;
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

    // Helpers -> Should these be in Predef?

    /**
     * Multiply each component within the provided mutable sequence by the provided
     * scalar constant.
     *
     * @param scalar the value to apply to each component.
     * @param seq    the sequence to multiply the scalar value into.
     * @return the altered mutable sequence.
     */
    public static final MutableSeq multiply(double scalar, Seq seq) {
        return multiply(Suppliers.ofInstance(scalar), seq);
    }

    /**
     * Multiply each component within the provided mutable sequence by the value
     * provided by the given supplier instance.
     *
     * @param supplier source of scalar values.
     * @param seq      the sequence to multiple the scalar value into.
     * @return the altered mutable sequence.
     */
    public static final MutableSeq multiply(final Supplier<Double> supplier, final Seq seq) {
        return new MutableSeq(seq.toMutableSeq().internal.map(new F<Double, Double>() {
            @Override
            public Double f(Double a) {
                return a * supplier.get();
            }
        }));
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
    public static final MutableSeq divide(double scalar, Seq seq) {
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
    public static final MutableSeq divide(final Supplier<Double> supplier, Seq seq) {
        final CheckingSupplier check = new CheckingSupplier(supplier);
        return new MutableSeq(seq.toMutableSeq().internal.map(new F<Double, Double>() {
            @Override
            public Double f(Double a) {
                return a / check.get();
            }
        }));
    }
}
