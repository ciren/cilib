/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Iterator;
import java.util.List;

/**
 * A {@code Sequence} is defined as a grouping of numbers.
 */
public final class Sequence implements Iterable<Number> {

    private final List<Number> internalSequence;

    /**
     * Generate a range of numbers starting at {@code from} and continuing
     * to {@code to} (inclusive).
     * @param from starting point
     * @param to end point
     * @return {@code Sequence} representing the defined range.
     */
    public static Sequence range(final int from, final int to) {
        List<Integer> ints = Lists.newArrayList();
        for (int i = from; i <= to; i++) {
            ints.add(i);
        }
        return new Sequence(ImmutableList.<Number>copyOf(ints));
    }

    /**
     * Generate a sequence of numbers that are identical in value. The generated
     * sequence will contain {@code count} elements, all with the value provided by
     * {@code item}.
     * @param item the desired value.
     * @param count number of values that the {@code Sequence} will contain.
     * @return A new {@code Sequence} instance containing {@code count} elements
     *         with the value defined by {@code item}.
     */
    public static Sequence repeat(final Number item, final int count) {
        List<Number> items = Lists.newArrayList();
        for (int i = 0; i < count; i++) {
            items.add(item);
        }
        return new Sequence(ImmutableList.<Number>copyOf(items));
    }

    private Sequence(List<Number> values) {
        this.internalSequence = values;
    }

    /**
     * Returns an iterator over the current {@code Sequence}.
     * <p>
     * The {@link Iterator} that is returned is unmodifiable and it is
     * <b>not</b> possible to invoke {@link Iterator#remove()}, doing so will
     * result in an exception being thrown.
     *
     * @return An {@code Iterator} over the current {@code Sequence}.
     * @throws UnsupportedOperationException if {@link Iterator#remove()} is invoked.
     */
    @Override
    public Iterator<Number> iterator() {
        final Iterator<Number> internal = internalSequence.iterator();
        return new UnmodifiableIterator<Number>() {

            @Override
            public boolean hasNext() {
                return internal.hasNext();
            }

            @Override
            public Number next() {
                return internal.next();
            }
        };
    }

    /**
     * Transform the current {@code Sequence} by applying a filter to the current
     * elements. The filter will remove all elements that are not found when isolating
     * the current indexes of the {@code Sequence}.
     * @param increment size of the jump between elements.
     * @return A new {@code Sequence} containing the filtered elements.
     */
    public Sequence by(final int increment) {
        Preconditions.checkArgument(increment >= 1);
        if (increment == 1) {
            return this;
        }
        ImmutableList.Builder<Number> newList = ImmutableList.builder();
        for (int i = 0, n = internalSequence.size(); i < n; i += increment) {
            newList.add(internalSequence.get(i));
        }
        return new Sequence(newList.build());
    }
}
