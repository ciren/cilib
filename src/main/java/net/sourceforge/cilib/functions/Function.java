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
package net.sourceforge.cilib.functions;

import net.sourceforge.cilib.util.Cloneable;

/**
 * Function difinition. All functions apply some or other transformation
 * on a set of input variables and create an output that is representative of
 * the input.
 * @param <F> The "from" type.
 * @param <T> The "to" type.
 */
public interface Function<F, T> extends Cloneable {

    /**
     * Perfrom the evaluation of the input and return the result.
     * @param input The input for the function.
     * @return The result of the evaluation.
     */
    public T evaluate(F input);

    /**
     * The maximum of the function.
     * @return The function maximum.
     */
    public T getMaximum();

    /**
     * The minimum of the function.
     * @return The function minimum.
     */
    public T getMinimum();

    /**
     * @return The dimension of the function.
     */
    public int getDimension();

    /**
     * @return The domain {@linkplain String}.
     */
    public String getDomain();
    
}
