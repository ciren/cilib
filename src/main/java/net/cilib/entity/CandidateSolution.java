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

/**
 * Immutable candidate solution.
 *
 * @since 0.8
 * @author gpampara
 */
public class CandidateSolution {

    public static CandidateSolution copyOf(CandidateSolution solution) {
        return new CandidateSolution(new TDoubleArrayList(solution.toArray()));
    }

    public static CandidateSolution copyOf(double... solution) {
        return new CandidateSolution(new TDoubleArrayList(solution));
    }
    private final TDoubleArrayList internal;

    private CandidateSolution(TDoubleArrayList list) {
        this.internal = list;
    }

    public double get(int index) {
        return internal.get(index);
    }

    public int size() {
        return internal.size();
    }

    public double[] toArray() {
        return internal.toNativeArray();
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).addValue(internal).toString();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        private int pos;
        private double[] contents;

        private Builder() {
            pos = 0;
            contents = new double[20];
        }

        public void add(double value) {
            updateSize();
            contents[pos++] = value;
        }

        public CandidateSolution build() {
            TDoubleArrayList result = new TDoubleArrayList(pos);
            for (int i = 0; i < pos; i++) {
                result.add(contents[i]);
            }
            return new CandidateSolution(result);
        }

        private void updateSize() {
            if (pos + 1 >= contents.length) {
                double[] tmp = new double[contents.length + 10];
                System.arraycopy(contents, 0, tmp, 0, contents.length);
                contents = tmp;
            }
        }
    }
}
