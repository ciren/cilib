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

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.primitives.Doubles;
import java.util.Arrays;

/**
 * A mutable sequence with mutation methods defined.
 *
 * @author gpampara
 */
public final class MutableSeq implements Seq {
    private final double[] internal;

    public MutableSeq(final LinearSeq seq) {
        this.internal = seq.toArray();
    }

    /**
     * Adds a {@code Seq} to this mutable instance. The addition is applied
     * component-wise.
     * @param that The sequence to add
     * @return the altered mutable sequence
     */
    public MutableSeq plus(Seq that) {
        final double[] thatSolution = that.toArray();
        Preconditions.checkState(internal.length == thatSolution.length);
        for (int i = 0, n = thatSolution.length; i < n; i++) {
            internal[i] += thatSolution[i];
        }
        return this;
    }

    /**
     * Subtracts a {@code Seq} from this mutable instance. The subtraction is
     * applied component-wise.
     * @param that The sequence to subtract
     * @return the altered mutable sequence
     */
    public MutableSeq subtract(Seq that) {
        final double[] thatSolution = that.toArray();
        Preconditions.checkState(internal.length == thatSolution.length);
        for (int i = 0, n = thatSolution.length; i < n; i++) {
            internal[i] -= thatSolution[i];
        }
        return this;
    }

    /**
     * Multiply each component within the mutable sequence by the provided
     * scalar constant.
     * @param scalar the value to apply to each component.
     * @return the altered mutable sequence.
     */
    public MutableSeq multiply(double scalar) {
        return multiply(Suppliers.ofInstance(scalar));
    }

    /**
     * Multiply each component within the mutable sequence by the value
     * provided by the given supplier instance.
     * @param supplier source of scalar values.
     * @return the altered mutable sequence.
     */
    public MutableSeq multiply(final Supplier<Double> supplier) {
        for (int i = 0, n = internal.length; i < n; i++) {
            internal[i] *= supplier.get();
        }
        return this;
    }

    /**
     * Divide each component within the mutable sequence by the provided
     * scalar constant. Division by {@code 0.0} is checked and an exception
     * is then raised, if needed.
     * @param scalar the value to apply to each component.
     * @return the altered mutable sequence.
     */
    public MutableSeq divide(double scalar) {
        return divide(Suppliers.ofInstance(scalar));
    }

    /**
     * Divide each component within the mutable sequence by the values provided
     * from the given {@code Supplier}. Division by {@code 0.0} is checked,
     * resulting in a {@link ArithmeticException} when violated.
     * @param supplier source of scalar values.
     * @return the altered mutable sequence.
     */
    public MutableSeq divide(final Supplier<Double> supplier) {
        final CheckingSupplier check = new CheckingSupplier(supplier);
        for (int i = 0, n = internal.length; i < n; i++) {
            internal[i] /= check.get();
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return internal.length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double[] toArray() {
        return Arrays.copyOf(internal, internal.length);
    }

    private static final class CheckingSupplier implements Supplier<Double> {
        private final Supplier<Double> supplier;

        private CheckingSupplier(Supplier<Double> supplier) { // Accept a function / closure
            this.supplier = supplier;
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
