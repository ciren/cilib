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

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.UnmodifiableIterator;

import java.util.Iterator;

import net.sourceforge.cilib.math.random.generator.RandomProvider;

/**
 * A range contains an ordered sequence of values, delimited by a starting and
 * and ending value. Range instances only maintain the starting and ending
 * values, with values within the range not being maintained.
 *
 * @author gpampara
 */
public final class Range implements Iterable<Double> {
    private final double begin;
    private final double end;
    private final double increment;
    
    /**
     * Create a {@code Range} instance, with the given {@code begin} and
     * {@code end} values.
     *
     * @param begin the range start.
     * @param end   the range end.
     * @return the range instance.
     */
    public static Range of(double begin, double end) {
        return ofWithIncrement(begin, end, 1.0);
    }

    public static Range ofWithIncrement(double begin, double end, double increment) {
        Preconditions.checkState(begin < end, "Invalid range: " + begin + " > " + end);
        return new Range(begin, end, increment);
    }

    private Range(double begin, double end, double increment) {
        this.begin = begin;
        this.end = end;
        this.increment = increment;
    }

    /**
     * The defined starting value of the range.
     *
     * @return starting value.
     */
    public double begin() {
        return begin;
    }

    /**
     * The defined ending value for the range.
     *
     * @return ending value.
     */
    public double end() {
        return end;
    }

    /**
     * Create a {@link Supplier} from the current {@code Range}, provided
     * a {@code RandomProvider}.
     *
     * @param random the random number generator to use.
     * @return a random number generator, within the current range.
     */
    Supplier<Double> toRandomSupplier(final RandomProvider random) {
        return new Supplier<Double>() {
            private final RandomProvider provider = random;
            private final Range range = Range.this;

            @Override
            public Double get() {
                return provider.nextDouble() * range.end + range.begin;
            }
        };
    }

    /**
     * This API is still up for debate.... Speed implications for the boxing
     * might not be favorable.
     *
     * @return An iterator over the range of the current range.
     */
    @Override
    public Iterator<Double> iterator() {
        return new UnmodifiableIterator<Double>() {
            private double current = begin;
            private double i = increment;
            private double e = end;

            @Override
            public boolean hasNext() {
                return current <= e; // Inclusive?
            }

            @Override
            public Double next() {
                current += i;
                return current;
            }
        };
    }
}
