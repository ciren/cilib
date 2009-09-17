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

import java.io.Serializable;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * <p><b>Bukin 4 Function.</b></p>
 *
 * <p><b>Reference:</b> S.K. Mishra, <i>Some New Test Functions
 * for Global Optimization and Performance of Repulsive Particle Swarm Methods</i>
 * North-Eastern Hill University, India, 2002</p>
 *
 * <p>Minimum:
 * <ul>
 * <li> &fnof;(<b>x</b>*) = 0.0 </li>
 * <li> <b>x</b>* = (-10,0)</li>
 * <li> for x<sub>1</sub> in [-15,-5], x<sub>2</sub> in [-3,3]</li>
 * </ul>
 * </p>
 *
 * <p>Characteristics:
 * <ul>
 * <li>Only defined for 2 dimensions</li>
 * <li>Seperable</li>
 * <li>Nonregular</li>
 * </ul>
 * </p>
 *
 * @author Andries Engelbrecht
 *
 */

public class Bukin4 extends ContinuousFunction implements Serializable {
    private static final long serialVersionUID = -7860070866440205636L;

    public Bukin4() {
        setDomain("R(-15,-5),R(-3,3)");
    }

    @Override
    public Bukin4 getClone() {
        return new Bukin4();
    }

    public Double getMinimum() {
        return 0.0;
    }

    /* (non-Javadoc)
     * @see net.sourceforge.cilib.functions.redux.ContinuousFunction#evaluate(net.sourceforge.cilib.type.types.container.Vector)
     */
    @Override
    public Double evaluate(Vector input) {
        double x1 = input.getReal(0);
        double x2 = input.getReal(1);

        return 100*x2*x2 + 0.01*Math.abs(x1+10);
    }
}
