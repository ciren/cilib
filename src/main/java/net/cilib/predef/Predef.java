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
package net.cilib.predef;

import fj.data.Option;

/**
 * Utility class of predefined helpers.
 */
public final class Predef {

    private Predef() {
        throw new UnsupportedOperationException();
    }

    /**
     * Create a "<i>fitness</i>". In this case a "fitness" is nothing more than
     * a simple {@link Option} that has a value.
     * <p>
     * This factory method is purely for convenience and is questionable.
     * @param value fitness value
     * @return an {@code Option} containing the fitness value.
     */
    public static Option<Double> fitness(double value) {
        return Option.some(value);
    }

    /**
     * Create an "<i>inferior fitness</i>". In this case a "fitness" is nothing
     * more than the none option type.
     * <p>
     * This factory method is purely for convenience and is questionable.
     * @return an {@code Option} representing an unspecified fitness value.
     */
    public static Option<Double> inferior() {
        return Option.<Double>none();
    }
}
