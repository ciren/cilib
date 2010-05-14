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

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Iterator;
import java.util.List;

/**
 * A {@code Sequence} is defined as a grouping of numbers.
 */
public final class Sequence implements Supplier<Number> {

    private final Iterator<Number> internalSequence;

    public static Sequence of(Number value) {
        return new Sequence(ImmutableList.<Number>of(value));
    }

    public static Sequence copyOf(Iterable<Number> iterable) {
        return new Sequence(ImmutableList.copyOf(iterable));
    }

    private Sequence(ImmutableList<Number> values) {
        this.internalSequence = new CyclicIterator(values);
    }

    /**
     * Generate a range of numbers starting at {@code from} and continuing
     * to {@code to} (inclusive).
     * @param from starting point
     * @param to end point
     * @return {@code Sequence} representing the defined range.
     */
    public static Iterable<Number> finiteRange(final int from, final int to) {
        List<Number> range = Lists.newArrayListWithCapacity(to - from);
        for (int i = from; i <= to; i++) {
            range.add(i);
        }
        return new FiniteSequence(Sequence.copyOf(range), range.size());
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
    public static Iterable<Number> repeat(final Number item, final int count) {
        List<Number> items = Lists.newArrayList();
        for (int i = 0; i < count; i++) {
            items.add(item);
        }
        return new FiniteSequence(Sequence.copyOf(items), count);
    }

    public Iterable<Number> withFiniteSizeOf(int size) {
        return new FiniteSequence(this, size);
    }

//    /**
//     * Transform the current {@code Sequence} by applying a filter to the current
//     * elements. The filter will remove all elements that are not found when isolating
//     * the current indexes of the {@code Sequence}.
//     * @param increment size of the jump between elements.
//     * @return A new {@code Sequence} containing the filtered elements.
//     */
//    public Sequence by(final int increment) {
//        Preconditions.checkArgument(increment >= 1);
//        if (increment == 1) {
//            return this;
//        }
//        ImmutableList.Builder<Number> newList = ImmutableList.builder();
//        for (int i = 0, n = internalSequence.size(); i < n; i += increment) {
//            newList.add(internalSequence.get(i));
//        }
//        return new Sequence(newList.build());
//    }
    @Override
    public Number get() {
        return internalSequence.next();
    }

    private static class FiniteSequence implements Iterable<Number> {

        private final Sequence sequence;
        private final int size;

        FiniteSequence(Sequence sequence, int size) {
            this.sequence = sequence;
            this.size = size;
        }

        @Override
        public Iterator<Number> iterator() {
            return new UnmodifiableIterator<Number>() {

                private int current = 0;

                @Override
                public boolean hasNext() {
                    return current < size;
                }

                @Override
                public Number next() {
                    current++;
                    return sequence.get();
                }
            };
        }
    }

    private static class CyclicIterator extends UnmodifiableIterator<Number> {

        private final ImmutableList<Number> values;
        private long current;

        private CyclicIterator(ImmutableList<Number> values) {
            this.values = values;
            current = 0;
        }

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public Number next() {
            int index = Long.valueOf(current++ % values.size()).intValue();
            return values.get(index); // Cop
        }
    }
}
