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
package net.sourceforge.cilib.type.types;

import static com.google.common.base.Preconditions.checkArgument;

/**
 *
 */
public final class Bounds {
    private final double lowerBound;
    private final double upperBound;

    public Bounds(double lowerBound, double upperBound) {
        checkArgument(lowerBound <= upperBound, "Bounds range violation, "
            + lowerBound + " should be less than " + upperBound);

        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public double getLowerBound() {
        return this.lowerBound;
    }

    public double getUpperBound() {
        return this.upperBound;
    }

    public double getRange() {
        return Math.abs(upperBound - lowerBound);
    }

    public boolean isInsideBounds(double value) {
        if (Double.compare(value, upperBound) <= 0 && Double.compare(value, lowerBound) >= 0) {
            return true;
        }

        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Bounds other = (Bounds) obj;
        if (this.lowerBound != other.lowerBound) {
            return false;
        }
        if (this.upperBound != other.upperBound) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.lowerBound) ^ (Double.doubleToLongBits(this.lowerBound) >>> 32));
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.upperBound) ^ (Double.doubleToLongBits(this.upperBound) >>> 32));
        return hash;
    }

    @Override
    public String toString() {
        return new StringBuilder()
            .append("(").append(lowerBound).append(":").append(upperBound).append(")")
            .toString();
    }

}
