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

import com.google.common.base.Preconditions;
import com.google.common.primitives.Doubles;
import java.util.Arrays;

/**
 *
 * @author gpampara
 */
public final class MutableSeq implements Seq {

    private final double[] internal;

    public MutableSeq(final LinearSeq seq) {
        this.internal = seq.toArray();
    }

    public MutableSeq plus(Seq that) {
        final double[] thatSolution = that.toArray();
        Preconditions.checkState(internal.length == thatSolution.length);
        for (int i = 0, n = thatSolution.length; i < n; i++) {
            internal[i] += thatSolution[i];
        }
        return this;
    }

    public MutableSeq subtract(Seq that) {
        final double[] thatSolution = that.toArray();
        Preconditions.checkState(internal.length == thatSolution.length);
        for (int i = 0, n = thatSolution.length; i < n; i++) {
            internal[i] -= thatSolution[i];
        }
        return this;
    }

    public MutableSeq multiply(double scalar) {
        for (int i = 0, n = internal.length; i < n; i++) {
            internal[i] *= scalar;
        }
        return this;
    }

    public MutableSeq divide(double scalar) {
        Preconditions.checkArgument(Doubles.compare(scalar, 0.0) != 0, "Cannot divide with a 0.0!");
        for (int i = 0, n = internal.length; i < n; i++) {
            internal[i] /= scalar;
        }
        return this;
    }

    @Override
    public int size() {
        return internal.length;
    }

    @Override
    public double[] toArray() {
        return Arrays.copyOf(internal, internal.length);
    }
}
