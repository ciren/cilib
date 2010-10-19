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
import java.util.Arrays;

/**
 *
 * @author gpampara
 */
public final class Velocity implements LinearSeq {//, HasFunctionalOperations<Seq.Builder> {

    private final TDoubleArrayList internal;

    public static Velocity copyOf(double... elements) {
        return new Velocity(new TDoubleArrayList(elements));
    }

    private Velocity(TDoubleArrayList list) {
        this.internal = list;
    }

    @Override
    public double[] toArray() {
        return internal.toNativeArray();
    }

    @Override
    public int size() {
        return internal.size();
    }

    @Override
    public double get(int index) {
        return internal.get(index);
    }

    @Override
    public MutableSeq toMutableSeq() {
        return new MutableSeq(this);
    }

    public SeqIterator iterator() {
        final double[] local = Arrays.copyOf(internal.toNativeArray(), internal.size());
        return new SeqIterator() {
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

    @Override
    public String toString() {
        return Objects.toStringHelper(this).addValue(internal).toString();
    }
}
