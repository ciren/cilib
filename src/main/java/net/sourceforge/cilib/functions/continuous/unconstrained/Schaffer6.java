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
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p><b>Schaffer's F6 generalised, also referred to as the Pathological Function.</b></p>
 *
 * <p><b>Reference:</b> S. Rahnamayan, H. R. Tizhoosh, M. M. A. Salama <i>A novel population initialization method for accelerating evolutionary algorithms</i>,
 * Computers and Mathematics with Applications, 2007</p>
 *
 * <p>Minimum:
 * <ul>
 * <li> &fnof;(<b>x</b>*) = 0</li>
 * <li> <b>x</b>* = (0, 0, ...., 0)</li>
 * <li> for x<sub>i</sub> in [-100, 100]</li>
 * </ul>
 * </p>
 *
 * <p>Characteristics:
 * <ul>
 * </ul>
 * </p>
 */
public class Schaffer6 extends ContinuousFunction {
    private static final long serialVersionUID = 4959662717057274057L;

    /**
     * Constructor. Initialise the function to the initial domain of R(-100.0,100.0)^2
     */
    public Schaffer6() {
        setDomain("R(-100.0,100.0)^30");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schaffer6 getClone() {
        return this;
    }


    /**
     * Initialise the function minimum value.
     * @return The minimum value as a <tt>Double</tt> object with value of 0.0
     */
    @Override
    public Double getMinimum() {
        return 0.0;
    }


    /**
     * Evaluate the function and return the evaluation.
     *
     * @param input The input vector to the function
     * @return A double value representing the function evaluation
     */
    @Override
    public Double evaluate(Vector input) {
        double sum = 0;
        
        for (int i = 0; i < input.size()-1; i++) {
            double xi = input.getReal(i);
            double xj = input.getReal(i + 1);
            double sinSquared = Math.sin(Math.sqrt((100 * (xi*xi)) + (xj*xj)));
            sinSquared *= sinSquared;

            double squaredVal = (xi*xi) - (2*xi*xj) + (xj*xj);
            squaredVal *= squaredVal;

            sum += 0.5 + ((sinSquared - 0.5) / (1 + (0.001 * squaredVal)));
        }

        return sum;
    }

}
