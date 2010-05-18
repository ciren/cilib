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
     * Set the value of the {@linkplain Numeric}.
     * @param value The {@linkplain String} value to be parsed.
     */
    void set(String value);

    /**
     * Set the value of the {@linkplain Numeric}.
     * @param value The {@literal boolean} value representing the value for this {@linkplain Type}.
     */
    void set(boolean value);

    /**
     * Set the value of the {@linkplain Numeric}.
     * @param value The {@literal int} value representing the value for this {@linkplain Type}.
     */
    void set(int value);

    /**
     * Set the value of the {@linkplain Numeric}.
     * @param value The {@literal double} value representing the value for this {@linkplain Type}.
     */
    void set(double value);

    /**
     * Get the value of this {@linkplain Numeric}.
     * @return The value of this {@linkplain Numeric} as a {@literal boolean}.
     */
    boolean getBit();

    /**
     * Set the bit value with the provided value.
     * @param value The value to set.
     */
    void setBit(boolean value);

    /**
     * Set the bit value with the provided value.
     * @param value The value to set.
     */
    void setBit(String value);

    /**
     * Get the value of this {@linkplain Numeric}.
     * @return The value of this {@linkplain Numeric} as a {@literal int}.
     */
    int getInt();

    /**
     * Set the integer value with the provided value.
     * @param value The value to set.
     */
    void setInt(int value);

    /**
     * Set the integer value with the provided value.
     * @param value The value to set.
     */
    void setInt(String value);

    /**
     * Get the value of this {@linkplain Numeric}.
     * @return The value of this {@linkplain Numeric} as a {@literal double}.
     */
    double getReal();

    /**
     * Set the real value with the provided value.
     * @param value The value to set.
     */
    void setReal(double value);

    /**
     * Set the real value with the provided value.
     * @param value The value to set.
     */
    void setReal(String value);

    String getRepresentation();

}
