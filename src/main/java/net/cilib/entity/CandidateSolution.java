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

import com.google.common.base.Objects;
import gnu.trove.TDoubleArrayList;

/**
 * Immutable candidate solution.
 *
 * @since 0.8
 * @author gpampara
 */
public final class CandidateSolution implements LinearSeq {

    private final TDoubleArrayList internal;

    /**
     * Returns an immutable candidate solution, which is a copy of the given
     * argument.
     * @param solution the candidate solution to copy.
     * @return an immutable copy of a provided new candidate solution.
     */
    public static CandidateSolution copyOf(CandidateSolution solution) {
        return new CandidateSolution(new TDoubleArrayList(solution.toArray()));
    }

    /**
     * Returns an immutable candidate solution containing the given elements,
     * in order.
     * @param solution the array of values, representing the candidate solution.
     * @return an immutable candidate solution representing the given values.
     */
    public static CandidateSolution copyOf(double... solution) {
        return new CandidateSolution(new TDoubleArrayList(solution));
    }

    private CandidateSolution(TDoubleArrayList list) {
        this.internal = list;
    }

    /**
     * Get the value of the candidate solution at the given {@code index}.
     * @param index position of the value
     * @return the value within the candidate solution at the given
     *  {@code index}.
     */
    @Override
    public double get(int index) {
        return internal.get(index);
    }

    /**
     * Returns the size of this {@code CandidateSolution}.
     * @return the candidate solution size.
     */
    @Override
    public int size() {
        return internal.size();
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
        return internal.toNativeArray();
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).addValue(internal).toString();
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
    public MutableSeq toMutableSeq() {
        return new MutableSeq(this);
    }

    public SeqIterator iterator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Builder to create {@link CandidateSolution} instances. After the builder
     * has created the {@link CandidateSolution}, the builder is reset to an
     * empty state.
     */
    public static class Builder implements Seq.Builder {

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
                return CandidateSolution.copyOf(target);
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

        public Builder copyOf(CandidateSolution candidateSolution) {
            final double[] solution = candidateSolution.toArray();
            internal = new double[solution.length];
            System.arraycopy(solution, 0, internal, 0, solution.length);
            return this;
        }
    }
}
