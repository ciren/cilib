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
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.primitives.Doubles;

import java.util.Arrays;
import java.util.Iterator;

/**
 * A mutable sequence with mutation methods defined.
 *
 * @author gpampara
 */
public final class MutableSeq implements LinearSeq {
    private final double[] internal;

    public MutableSeq(final LinearSeq seq) {
        this.internal = seq.toArray();
    }

    // ???? @TODO: This ok?
    public MutableSeq(double[] contents) {
        this.internal = contents;
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

    @Override
    public double get(int index) {
        return this.internal[index];
    }

    /**
     * Returns the current {@code MutableSeq}.
     *
     * @return the current instance.
     */
    @Override
    public MutableSeq toMutableSeq() {
        return this;
    }

    public MutableSeq subtract(Seq seq) {
        return subtract(this, seq);
    }

    public MutableSeq plus(Seq seq) {
        return plus(this, seq);
    }

    @Override
    public Iterator<Double> iterator() {
        final double[] local = Arrays.copyOf(internal, internal.length);
        return new UnmodifiableIterator<Double>() {
            private int count = 0;

            @Override
            public boolean hasNext() {
                return count < local.length;
            }

            @Override
            public Double next() {
                return local[count++];
            }
        };
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

    // Helpers -> Should these be in Predef?

    /**
     * Adds a {@code Seq} to a provided mutable instance. The addition is applied
     * component-wise.
     *
     * @param current The sequence.
     * @param other   The sequence to add
     * @return the altered mutable sequence
     */
    public static MutableSeq plus(Seq current, Seq other) {
        final double[] currentSolution = current.toArray();
        final double[] thatSolution = other.toArray();
        Preconditions.checkState(currentSolution.length == thatSolution.length);
        for (int i = 0, n = thatSolution.length; i < n; i++) {
            currentSolution[i] += thatSolution[i];
        }
        return new MutableSeq(currentSolution);
    }

    /**
     * Subtracts a {@code Seq} from a provided mutable instance. The subtraction is
     * applied component-wise.
     *
     * @param current The sequence to based the operation on.
     * @param other   The sequence to subtract
     * @return the altered mutable sequence
     */
    public static MutableSeq subtract(Seq current, Seq other) {
        final double[] currentSolution = current.toArray();
        final double[] thatSolution = other.toArray();
        Preconditions.checkState(currentSolution.length == thatSolution.length);
        for (int i = 0, n = thatSolution.length; i < n; i++) {
            currentSolution[i] -= thatSolution[i];
        }
        return new MutableSeq(currentSolution);
    }

    /**
     * Multiply each component within the provided mutable sequence by the provided
     * scalar constant.
     *
     * @param scalar the value to apply to each component.
     * @param seq    the sequence to multiply the scalar value into.
     * @return the altered mutable sequence.
     */
    public static MutableSeq multiply(double scalar, Seq seq) {
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
    public static MutableSeq multiply(Supplier<Double> supplier, Seq seq) {
        double[] d = seq.toArray();
        for (int i = 0; i < d.length; i++) {
            d[i] *= supplier.get();
        }
        return new MutableSeq(d);
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
    public static MutableSeq divide(double scalar, Seq seq) {
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
    public static MutableSeq divide(final Supplier<Double> supplier, Seq seq) {
        final CheckingSupplier check = new CheckingSupplier(supplier);
        final double[] internal = seq.toArray();
        for (int i = 0, n = internal.length; i < n; i++) {
            internal[i] /= check.get();
        }
        return new MutableSeq(internal);
    }
}
