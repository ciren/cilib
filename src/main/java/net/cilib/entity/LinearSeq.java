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

import java.util.RandomAccess;

/**
 * A {@code Seq} arranged in a linear fashion, providing random access to
 * elements.
 * @author gpampara
 */
public interface LinearSeq extends Seq, RandomAccess, Iterable<Double> {

    /**
     * Get the value at the provided {@code index}.
     * @param index the location within the {@code LinearSeq}.
     * @return the requested value
     * @throws
     */
    double get(int index);

    /**
     * Convert the instance to a mutable version. The mutable version contains
     * a copy of the data contained within the {@code LinearSeq} instance.
     * @return a {@code MutableSeq} instance.
     */
    MutableSeq toMutableSeq();
}
