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
 * <p><b>Booth Function</b></p>.
 *
 * <p><b>Reference:</b> S.K. Mishra, <i>Some New Test Functions
 * for Global Optimization and Performance of Repulsive Particle Swarm Methods</i>
 * North-Eastern Hill University, India, 2002</p>
 *
 * <p>Minimum:
 * <ul>
 * <li> &fnof;(<b>x</b>*) = 0 </li>
 * <li> <b>x</b>* = (1,3)</li>
 * <li> for x<sub>1</sub>, x<sub>2</sub> in [-10,10]</li>
 * </ul>
 * </p>
 *
 * <p>Characteristics:
 * <ul>
 * <li>Only defined for 2 dimensions</li>
 * <li>Unimodal</li>
 * <li>Seperable</li>
 * <li>Regular</li>
 * </ul>
 * </p>
 *
 * @author Andries Engelbrecht
 *
 */
public class Booth extends ContinuousFunction {
    private static final long serialVersionUID = -7803711986955989075L;

    /**
     * Create an instance of the Booth function. The domain is set to be
     * R(-10.0,10.0)^2 by default.
     */
    public Booth() {
        setDomain("R(-10,10)^2");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Booth getClone() {
        return new Booth();
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
        double x1 = input.getReal(0);
        double x2 = input.getReal(1);

        return (x1+2*x2-7)*(x1+2*x2-7) + (2*x1+x2-5)*(2*x1+x2-5);
    }
}
