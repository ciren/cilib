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
 * <p><b>Bohachevsky 1</b></p>.
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
 * @author Andries Engelbrecht
 *
 */
public class Bohachevsky1 extends ContinuousFunction {

    private static final long serialVersionUID = 44382638223225638L;

    /**
     * Creates a new instance of Bohachevsky1. The domain is set to R(-100.0, 100.0)^2 by
     * default.
     */
    public Bohachevsky1() {
        //constraint.add(new DimensionValidator(2));
        setDomain("R(-100, 100)^2");
    }

    /**
     * {@inheritDoc}
     */
    public Bohachevsky1 getClone() {
        return new Bohachevsky1();
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
        return input.getReal(0)*input.getReal(0) + 2*input.getReal(1)*input.getReal(1) - 0.3*Math.cos(3*Math.PI*input.getReal(0)) - 0.4*Math.cos(4*Math.PI*input.getReal(1))+0.7;
    }

}
