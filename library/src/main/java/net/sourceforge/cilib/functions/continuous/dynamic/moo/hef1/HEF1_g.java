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
package net.sourceforge.cilib.functions.continuous.dynamic.moo.hef1;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This function is the g function of the adapted F1 problem defined in the following paper:
 * H. Li and Q. Zhang. Multiobjective optimization problems with complicated Pareto sets,
 * MOEA/D and NSGA-II, IEEE Transactions on Evolutionary Computation, 13(2):284-302, 2009.
 *
 * The problem has been adapted by Helbig and Engelbrecht to make it a DMOOP.
 *
 * @author Marde Greeff
 */
public class HEF1_g implements ContinuousFunction {
    
    /**
     * Evaluates the function     
     */
    @Override
    public Double apply(Vector input) {
        double value = Math.abs(input.doubleValueOf(0));
        double sum = 0.0;

        int index = 0;
        for (int k=1; k < input.size(); k+=2) {
            double power = 0.5*(1.0+3.0*(k-2)/(input.size()-2));
            double val = Math.pow(value, power);
            sum += Math.pow(input.doubleValueOf(k)-(double)val, 2);
            index++;
        }
        sum *= 2.0/index;
        sum += 2.0-Math.sqrt(value);
        return sum;
    }
}
