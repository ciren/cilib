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

import java.util.Arrays;

/**
 * Sequence to represent a {@code Velocity}.
 *
 * @author gpampara
 */
public final class Velocity implements LinearSeq {
    private final double[] internal;

    /**
     * Create a velocity with length {@code size}, containing repeated
     * {@code element} values.
     *
     * @param element the value to repeat
     * @param size    the velocity size
     * @return {@code Velocity} instance containing {@code element} repeated
     *         {@code size} times.
     */
    public static Velocity fill(double element, int size) {
        double[] values = new double[size];
        Arrays.fill(values, element);
        return new Velocity(values);
    }

    /**
     * Create a new {@code Velocity} instance, based on the provided
     * {@code elements}.
     *
     * @param elements contents of {@code Velocity}.
     * @return a new {@code Velocity} instance.
     */
    public static Velocity copyOf(double... elements) {
        return new Velocity(elements);
    }

    private Velocity(double[] list) {
        this.internal = list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double[] toArray() {
        return Arrays.copyOf(internal, internal.length);
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
    public double get(int index) {
        return internal[index];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MutableSeq toMutableSeq() {
        return new MutableSeq(this);
    }

    /**
     * Create an iterator view of the current {@code Velocity}.
     *
     * @return a new {@code SeqIterator} instance.
     */
    public SeqIterator iterator() {
        return new SeqIterator() {
            private final double[] local = Arrays.copyOf(internal, internal.length);
            private int count = 0;

            @Override
            public boolean hasNext() {
                return count < local.length;
            }

            @Override
            public double next() {
                double result = local[count];
                count++;
                return result;
            }
        };
    }

    /**
     * Get a {@code String} representation of the current {@code Velocity}.
     *
     * @return the {@code String} representation.
     */
    @Override
    public String toString() {
        return Objects.toStringHelper(this).addValue(internal).toString();
    }
}
