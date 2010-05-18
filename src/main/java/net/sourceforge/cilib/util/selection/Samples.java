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
package net.sourceforge.cilib.util.selection;

/**
 *
 * @author gpampara
 */
public final class Samples {

    private Samples() {}

    public static <T> SamplePredicate<T> all() {
        return (SamplePredicate<T>) All.INSTANCE;
    }

    /*
     * A new instance is returned each time because some state needs to be
     * managed within the instance.
     */
    public static <T> SamplePredicate<T> first(final int number) {
        return new SamplePredicate<T>() {
            private int counter = 0;

            @Override
            public boolean apply(final T input, int capacity) {
                if (counter <= number) {
                    counter++;
                    return true;
                }

                return false;
            }

            @Override
            public boolean isDone() {
                return counter > number;
            }
        };
    }

    public static <T> SamplePredicate<T> first() {
        return new SamplePredicate<T>() {
            private boolean returned = false;
            @Override
            public boolean apply(T input, int capacity) {
                if (!returned) {
                    returned = true;
                }
                return returned;
            }

            @Override
            public boolean isDone() {
                return returned;
            }
        };
    }

    public static <T> SamplePredicate<T> last() {
        return new SamplePredicate<T>() {
            private int counter = 0;
            private boolean done = false;
            @Override
            public boolean apply(T input, int capacity) {
                if (counter == capacity-1) {
                    done = true;
                    return true;
                }

                counter++;
                return false;
            }

            @Override
            public boolean isDone() {
                return done;
            }
        };
    }

    public static <T> SamplePredicate<T> last(final int number) {
        return new SamplePredicate<T>() {
            private int counter = 0;
            private boolean done = false;
            @Override
            public boolean apply(T input, int capacity) {
                if (counter == capacity)
                    done = true;

                if (counter >= capacity-number)
                    return true;

                counter++;
                return false;
            }

            @Override
            public boolean isDone() {
                return done;
            }
        };
    }

    enum All implements SamplePredicate<Object> {
        INSTANCE;

        @Override
        public boolean apply(Object input, int capacity) {
            return true;
        }

        @Override
        public boolean isDone() {
            return false;
        }
    }
}
