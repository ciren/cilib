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
package net.sourceforge.cilib.type.types;

/**
 * Definition of the <tt>Numeric</tt> type.
 *
 * @author Gary Pampara
 */
public interface Numeric extends Type, BoundedType, Resetable, Comparable<Numeric>, Randomizable {

    /**
     * {@inheritDoc}
     */
    @Override
    Numeric getClone();

    /**
     * Get the value of this {@linkplain Numeric}.
     * @return The value of this {@linkplain Numeric} as a {@literal boolean}.
     */
    boolean booleanValue();

    /**
     * Set the bit value with the provided value.
     * @param value The value to set.
     */
//    void valueOf(boolean value);

    /**
     * Get the value of this {@linkplain Numeric}.
     * @return The value of this {@linkplain Numeric} as a {@literal int}.
     */
    int intValue();

    /**
     * Set the integer value with the provided value.
     * @param value The value to set.
     */
//    void valueOf(int value);

    /**
     * Get the value of this {@linkplain Numeric}.
     * @return The value of this {@linkplain Numeric} as a {@literal double}.
     */
    double doubleValue();

    /**
     * Set the real value with the provided value.
     * @param value The value to set.
     */
//    void valueOf(double value);

    String getRepresentation();

}
