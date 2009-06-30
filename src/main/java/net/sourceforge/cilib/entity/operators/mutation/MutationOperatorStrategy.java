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
package net.sourceforge.cilib.entity.operators.mutation;

import net.sourceforge.cilib.type.types.Numeric;

/**
 *
 * @author Gary Pampara
 */
public interface MutationOperatorStrategy {

    /**
     * Perform the evaluation based on the given parameters.
     * @param t1 The first parameter: {@see net.sourceforge.cilib.type.types.Numeric}.
     * @param t2 The second parameter: {@see net.sourceforge.cilib.type.types.Numeric}.
     * @return The result of the evaluation with parameter t1 and t2.
     */
    public double evaluate(Numeric t1, Numeric t2);

    /**
     * Perform the evaluation based on the given parameters.
     * @param t1 The first parameter: {@see net.sourceforge.cilib.type.types.Numeric}.
     * @param value The second parameter: double.
     * @return The result of the evaluation with parameter t1 and t2.
     */
    public double evaluate(Numeric t1, double value);

    /**
     * Perform the evaluation based on the given parameters.
     * @param value The first parameter: double.
     * @param t1 The second parameter: {@see net.sourceforge.cilib.type.types.Numeric}.
     * @return The result of the evaluation with parameter value and t2.
     */
    public double evaluate(double value, Numeric t1);

    /**
     * Perform the evaluation based on the given parameters.
     * @param t1 The first parameter: double.
     * @param t2 The second parameter: double.
     * @return The result of the evaluation with parameter t1 and t2.
     */
    public double evaluate(double t1, double t2);

}
