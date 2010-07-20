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
package net.sourceforge.cilib.functions;

/**
 * Function definition. All functions apply some or other transformation
 * on a set of input variables and create an output that is representative of
 * the input.
 * @param <F> The "from" type.
 * @param <T> The "to" type.
 */
public interface Function<F, T> {

    /**
     * Perform the evaluation of the input and return the result.
     * @param input The input for the function.
     * @return The result of the evaluation.
     */
    T apply(F input);
}
