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
package net.cilib.collection;

import fj.F;

/**
 * A sequence of values.
 *
 * @author gpampara
 */
public abstract class Seq implements Iterable<Double> {

    protected abstract Array delegate();

    public abstract Seq plus(Seq other);

    public abstract Seq subtract(Seq other);

    public abstract Seq map(F<Double, Double> f);

    /**
     * A {@code builder} interface to build up a {@code Seq} instance.
     */
    public interface Buffer {

        /**
         * Add an element to the builder.
         *
         * @param element to be added.
         * @return the current modified builder instance.
         */
        Buffer add(double element);

        /**
         * Create a {@code Seq} instance from the {@code Builder} contents.
         *
         * @return a new {@code Seq} instance.
         */
        Seq build();
    }
}
