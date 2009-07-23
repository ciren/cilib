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
 * Generalised Griewank function.
 *
 * <p>
 * Characteristics:
 * <ul>
 * <li>Multi-modal</li>
 * <li>Non seperable</li>
 * <li>Regular</li>
 * </ul>
 *
 * f(x) = 0; x = (0,0,...,0);
 * x_i e (-600,600)
 *
 * @author  Edwin Peer
 */
public class Griewank extends ContinuousFunction {
    private static final long serialVersionUID = 1095225532651577254L;

    /**
     * Create an instance of {@code Griewank}. The default domain is set to R(-600, 600)^30
     */
    public Griewank() {
        setDomain("R(-600, 600)^30");
    }

    /**
     * {@inheritDoc}
     */
    public Griewank getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public Double getMinimum() {
        return 0.0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double evaluate(Vector input) {
        double sumsq = 0;
        double prod = 1;
        for (int i = 0; i < getDimension(); ++i) {
            sumsq += input.getReal(i) * input.getReal(i);
            prod *= Math.cos(input.getReal(i) / Math.sqrt(i + 1));
        }
        return 1 + sumsq * (1.0/4000.0) - prod;
    }

}
