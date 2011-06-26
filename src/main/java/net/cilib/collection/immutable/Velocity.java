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
import net.cilib.collection.Array;
import net.cilib.collection.LinearSeq;
import net.cilib.collection.SeqView;
import net.cilib.collection.Seq;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Sequence to represent a {@code Velocity}.
 *
 * @author gpampara
 */
public final class Velocity extends LinearSeq {
    private static final Velocity EMPTY = new Velocity(Array.empty());
    private final Array internal;

    /**
     * Create a velocity with length {@code size}, containing repeated
     * {@code element} values.
     *
     * @param element the value to repeat
     * @param n    the velocity size
     * @return {@code Velocity} instance containing {@code element} repeated
     *         {@code size} times.
     */
    public static Velocity replicate(final int n, final double element) {
        double[] values = new double[n];
        Arrays.fill(values, element);
        return new Velocity(Array.array(values));
    }

    /**
     * Create a new {@code Velocity} instance, based on the provided
     * {@code elements}.
     *
     * @param elements contents of {@code Velocity}.
     * @return a new {@code Velocity} instance.
     */
    public static Velocity copyOf(double... elements) {
        return new Velocity(Array.array(elements));
    }

    public static Velocity copyOf(Seq sequence) {
        List<Double> list = Lists.newArrayList(sequence.iterator());
        return new Velocity(Array.array(Doubles.toArray(list)));
    }

    private Velocity(Array array) {
        this.internal = array;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double[] toArray() {
        return internal.copyOfInternal();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return internal.length();
    }

    /**
     * Create an iterator view of the current {@code Velocity}.
     *
     * @return a new {@code SeqIterator} instance.
     */
    @Override
    public Iterator<Double> iterator() {
        return internal.iterator();
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

    public static Velocity empty() {
        return EMPTY;
    }

    @Override
    protected Array delegate() {
        return this.internal;
    }

    @Override
    public Seq plus(Seq other) {
        return new SeqView(internal.plus(other));
    }

    @Override
    public Seq subtract(Seq other) {
        return new SeqView(internal.subtract(other));
    }

    @Override
    public Velocity take(int n) {
        return new Velocity(internal.take(n));
    }

    @Override
    public Velocity drop(int n) {
        return new Velocity(internal.drop(n));
    }

    @Override
    public Velocity map(F<Double, Double> f) {
        return new Velocity(internal.map(f));
    }
}
