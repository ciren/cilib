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
 * <p>Generalised Rosenbrock function.</p>
 *
 * <p><b>Reference:</b> X. Yao, Y. Liu, G. Liu, <i>Evolutionary Programming
 * Made Faster</i>,  IEEE Transactions on Evolutionary Computation,
 * 3(2):82--102, 1999</p>
 *
 * Characteristics:
 * <ul>
 * <li>Unimodal</li>
 * <li>Non Separable</li>
 * <li>Continuous</li>
 * <li>Regular</li>
 * </ul>
 *
 * f(x) = 0; x = 1
 *
 * x e [-2.048,2.048]
 *
 * @author  Edwin Peer
 */
public class Rosenbrock extends ContinuousFunction {
    private static final long serialVersionUID = -5850480295351224196L;

    /**
     * Create an instance of {@linkplain Rosenbrock}. Domain is set to R(-2.048, 2.048)^30.
     */
    public Rosenbrock() {
        setDomain("R(-2.048, 2.048)^30");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Rosenbrock getClone() {
        return new Rosenbrock();
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
    public Double evaluate(Vector input) {
        double tmp = 0;

        for (int i = 0; i < input.getDimension()-1; ++i) {
            double a = input.getReal(i);
            double b = input.getReal(i+1);

            tmp += ((100 * (b-a*a) * (b-a*a)) + ((a-1.0) * (a-1.0)));
        }

        return tmp;
    }

}
