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
package net.sourceforge.cilib.functions.continuous.unconstrained;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p><b>The Salomon Function.</b></p>
 *
 * <p><b>Reference:</b> R. Salomon <i>Re-evaluating genetic algorithm performance under coordinate rotation of benchmark functions. A survey of some
 * theoretical and practical aspects of genetic algorithms</i>,
 * Biosystems, 1996</p>
 *
 * <p>Minimum:
 * <ul>
 * <li> &fnof;(<b>x</b>*) = 0</li>
 * <li> <b>x</b>* = (0, 0, ...., 0)</li>
 * <li> for x<sub>i</sub> in [-600, 600]</li>
 * </ul>
 * </p>
 * <p>
 * Characteristics:
 * <ul>
 * <li>Multimodal</li>
 * <li>Nonseperable</li>
 * </ul>
 * f(x) = 0; x = (0,0,...,0); x_i e (-600,600)
 * @author Olusegun Olorunda
 */
public class Salomon extends ContinuousFunction {
    private static final long serialVersionUID = -6002240316648057218L;

    /**
     * Create an instance of the function. Default domain is set to R(-600, 600)^30
     */
    public Salomon() {
        setDomain("R(-600, 600)^30");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Salomon getClone() {
        return new Salomon();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getMinimum() {
        return 0.0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double sumSquares = 0.0;

        for (int i = 0; i < input.getDimension(); i++) {
            sumSquares += input.getReal(i) * input.getReal(i);
        }

        return -(Math.cos(2 * Math.PI * Math.sqrt(sumSquares))) + (0.1 * Math.sqrt(sumSquares)) + 1;
    }
}
