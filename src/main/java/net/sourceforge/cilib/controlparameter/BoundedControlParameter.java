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
package net.sourceforge.cilib.controlparameter;

/**
 * A {@linkplain net.sourceforge.cilib.controlparameter.ControlParameter control parameter} instance
 * that is defined to operate within a specific range of values. The range is defined as a domain string.
 * Any time the parameter exceeds the bounded range, it will be clamped and brought back to remain on
 * the edges of the range specified.
 *
 * @author Gary Pampara
 */
public interface BoundedControlParameter extends ControlParameter {

    /**
     * Get the lower bound of the
     * {@linkplain net.sourceforge.cilib.controlparameter.ControlParameter control paramter}.
     * @return The lower bound value.
     */
    double getLowerBound();

    /**
     * Set the value of the lower bound.
     * @param lower The value to set.
     */
    void setLowerBound(double lower);

    /**
     * Get the upper bound for the
     * {@linkplain net.sourceforge.cilib.controlparameter.ControlParameter control parameter}.
     * @return The upper bound value.
     */
    double getUpperBound();

    /**
     * Set the value for the upper bound.
     * @param value The value to set.
     */
    void setUpperBound(double value);

    void setRange(String range);
}
