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
package net.cilib.collection.immutable;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import fj.F;
import fj.F2;
import fj.Function;
import fj.Unit;
import net.cilib.collection.LinearSeq;
import net.cilib.collection.SeqView;
import net.cilib.collection.Seq;

import java.util.Iterator;
import java.util.List;
import net.cilib.collection.Array;

/**
 * Immutable candidate solution. A candidate solution is the representation
 * of a solution within a given problem domain.
 *
 * @since 0.8
 * @author gpampara
 */
public final class CandidateSolution extends LinearSeq {

    private final static CandidateSolution EMPTY = new CandidateSolution(Array.empty());
    private final Array internal;

    /**
     * Return an empty {@code CandidateSolution}. The same instance will be
     * returned on repetitive calls to this method.
     * @return the empty {@code CandidateSolution}.
     */
    public static CandidateSolution empty() {
        return EMPTY;
    }

    /**
     * Returns an immutable candidate solution, which is a copy of the given
     * argument.
     * @param solution the candidate solution to copy.
     * @return an immutable copy of a provided new candidate solution.
     */
    public static CandidateSolution copyOf(final Seq solution) {
        List<Double> list = Lists.newArrayList(solution);
        double[] a = Doubles.toArray(list);
        return new CandidateSolution(Array.array(a));
    }

    /**
     * Returns an immutable candidate solution containing the given elements,
     * in order.
     * @param solution the array of values, representing the candidate solution.
     * @return an immutable candidate solution representing the given values.
     */
    public static CandidateSolution solution(final double first, final double... rest) {
        int size = rest.length + 1;
        double[] contents = new double[size];
        contents[0] = first;
        System.arraycopy(rest, 0, contents, 1, rest.length);
        return new CandidateSolution(Array.array(contents));
    }

    /**
     * Create a {@code CandidateSolution} instance, filled up with the
     * provided {@code item} for {@code size} dimensions.
     * @param n the number of items to include within the
     *        {@code CandidateSolution}
     * @param item used to fill the {@code CandidateSolution}
     * @return A newly created {@code CandidateSolution} of filled
     *         {@code item}s.
     */
    public static CandidateSolution replicate(final int n, final double item) {
        return replicate(n, Function.<Unit, Double>constant(item));
    }

    /**
     * Create a {@code CandidateSolution} instance, filled up with the
     * provided function for {@code size} dimensions.
     * @param n the number of items to include within the
     *        {@code CandidateSolution}
     * @param generator function applied for value.
     * @return A newly created {@code CandidateSolution} of filled
     *         {@code item}s.
     */
    public static CandidateSolution replicate(final int n, final F<Unit, Double> generator) {
        Builder builder = newBuilder();
        for (int i = 0; i < n; i++) {
            builder.add(generator.f(Unit.unit()));
        }
        return builder.build();
    }

    private CandidateSolution(Array a) {
        this.internal = a;
    }

    /**
     * Returns the size of this {@code CandidateSolution}.
     * @return the candidate solution size.
     */
    @Override
    public int size() {
        return internal.length();
    }

    /**
     * Convert the {@code CandidateSolution} into a primitive array. The
     * returned array is copy of the contents of the {@code CandidateSolution}.
     *
     * @return a copy of the internal representation for this candidate
     * solution.
     */
    @Override
    public double[] toArray() {
        return internal.copyOfInternal();
    }

    /**
     * Obtain a {@code String} representation of the {@code CandidateSolution}.
     * @return {@code String} representation.
     */
    @Override
    public String toString() {
        return Objects.toStringHelper(this).addValue(internal).toString();
    }

    /**
     * Create an iterator to traverse the contents of the
     * {@code CandidateSolution}. For the iterator, a defensive copy of the
     * current {@code CandidateSolution} representation is made.
     * @return {@code SeqIterator} instance for the iteration.
     */
    @Override
    public Iterator<Double> iterator() {
        return internal.iterator();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CandidateSolution)) {
            return false;
        }

        CandidateSolution other = (CandidateSolution) obj;
        return internal.equals(other.internal);
    }

    @Override
    public int hashCode() {
        return internal.hashCode();
    }

    /**
     * Creates an instance of the internal {@linkplain Builder builder} for
     * creation of candidate solutions.
     * @return a new instance of the builder.
     */
    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public Seq plus(Seq other) {
        return new SeqView(internal.plus(other));
    }

    @Override
    protected Array delegate() {
        return this.internal;
    }

    @Override
    public Seq subtract(Seq other) {
        return new SeqView(internal.subtract(other));
    }

    @Override
    public CandidateSolution take(int n) {
        return new CandidateSolution(internal.take(n));
    }

    @Override
    public CandidateSolution drop(int n) {
        return new CandidateSolution(internal.drop(n));
    }

    @Override
    public CandidateSolution map(F<Double, Double> f) {
        return new CandidateSolution(internal.map(f));
    }

    public CandidateSolution zipWith(CandidateSolution target, F<Double, F<Double, Double>> f) {
        return new CandidateSolution(internal.zipWith(target.internal, f));
    }

    public CandidateSolution zipWith(CandidateSolution target, F2<Double, Double, Double> f) {
        return zipWith(target, Function.curry(f));
    }

    /**
     * Builder to create {@link CandidateSolution} instances. After the builder
     * has created the {@link CandidateSolution}, the builder is reset to an
     * empty state.
     */
    public static class Builder implements Seq.Buffer {

        private int current;
        private double[] internal;

        private Builder() {
            current = 0;
            internal = new double[]{};
        }

        /**
         * Adds a {@code value} to the {@code CandidateSolution}.
         * @param value the value to add.
         * @return the current {@code Builder}.
         */
        @Override
        public Builder add(double value) {
            updateSize();
            internal[current] = value;
            current += 1;
            return this;
        }

        /**
         * Returns a newly created {@code CandidateSolution} based on the
         * contents of the {@code Builder}.
         * @return the newly created {@code CandidateSolution}.
         */
        @Override
        public CandidateSolution build() {
            try {
                double[] target = new double[current];
                System.arraycopy(internal, 0, target, 0, current);
                return new CandidateSolution(Array.array(target));
            } finally {
                current = 0;
                internal = new double[]{};
            }
        }

        private void updateSize() {
            if (current + 1 >= internal.length) {
                double[] tmp = new double[internal.length + 10];
                System.arraycopy(internal, 0, tmp, 0, internal.length);
                internal = tmp;
            }
        }

        /**
         * Create a copy of the provided {@code CandidateSolution} as the data
         * for the {@code Builder}. Note that this method is <b>destructive.</b>
         * Any data that is currently within the {@code Builder} will be
         * replaced by the data within the provided {@code CandidateSolution}.
         * @param candidateSolution sequence to seed the {@code Builder} with.
         * @return the {@code Builder} for method chaining.
         */
        public Builder copyOf(CandidateSolution candidateSolution) {
            final double[] solution = candidateSolution.toArray();
            internal = new double[solution.length];
            System.arraycopy(solution, 0, internal, 0, solution.length);
            return this;
        }
    }
}
