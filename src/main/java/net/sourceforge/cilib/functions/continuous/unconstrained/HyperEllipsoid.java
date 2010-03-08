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
 * <p><b>HyperEllipsoid.</b></p>
 *
 * <p>
 * Minimum:
 * <ul>
 * <li> &fnof;(<b>x</b>*) = 0</li>
 * <li> <b>x</b>* = (0, 0, ...., 0)</li>
 * <li> for x<sub>i</sub> in [-5.12,5.12]</li>
 * </ul>
 * </p>
 *
 * <p>
 * Characteristics:
 * <ul>
 * <li>Unimodal</li>
 * <li>Continuous</li>
 * <li>Convex</li>
 * </ul>
 *
 * @author  engel
 */
public class HyperEllipsoid extends ContinuousFunction {
    private static final long serialVersionUID = 813261964413884141L;

    /** Creates a new instance of HyperEllipsoid. Default domain is set to R(-5.12, 5.12)^30 */
    public HyperEllipsoid() {
        setDomain("R(-5.12,5.12)^30");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HyperEllipsoid getClone() {
        return new HyperEllipsoid();
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
        double tmp = 0;
        for (int i = 0; i < getDimension(); ++i) {
            tmp += (i + 1) * input.getReal(i) * input.getReal(i);
        }
        return tmp;
    }

}
