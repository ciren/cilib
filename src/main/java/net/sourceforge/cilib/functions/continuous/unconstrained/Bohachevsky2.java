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
 * <p><b>Bohachevsky 2.</b></p>
 *
 * <p><b>Reference:</b> Global Optimization Meta-Heuristics Website,
 * http://www-optima.amp.i.kyoto-u.ac.jp/member/student/hedar/Hedar_files/go.htm</p>
 *
 * <p>Minimum:
 * <ul>
 * <li> &fnof;(<b>x</b>*) = 0 </li>
 * <li> <b>x</b>* = (0, 0)</li>
 * <li> for x<sub>1</sub>, x<sub>2</sub> in [-100, 100]</li>
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
 * R(-100, 100)^2
 *
 * @author Andries Engelbrecht
 */
public class Bohachevsky2 implements ContinuousFunction {

    private static final long serialVersionUID = -1572998736995724677L;

    /** Creates a new instance of Bohachevsky2. */
    public Bohachevsky2() {
        //constraint.add(new DimensionValidator(2));
    }

    /**
     * {@inheritDoc}
     */
    public Bohachevsky2 getClone() {
        return new Bohachevsky2();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double apply(Vector input) {
        double x = input.doubleValueOf(0);
        double y = input.doubleValueOf(1);
        return x*x + 2*y*y - 0.3*Math.cos(3*Math.PI*x) * Math.cos(4*Math.PI*y)+0.3;
    }
}
