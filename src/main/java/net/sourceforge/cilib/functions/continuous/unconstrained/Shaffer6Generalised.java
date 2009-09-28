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
 *
 * @author leo
 *
 */
public class Shaffer6Generalised extends ContinuousFunction {

    private static final long serialVersionUID = -8608101733772510291L;

    public Shaffer6Generalised() {
        setDomain("R(-100, 100)^30");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shaffer6Generalised getClone() {
        return new Shaffer6Generalised();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getMinimum() {
        return new Double(0.0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double evaluate(Vector x) {
        double sum = 0;
        for(int i = 0; i < x.size() - 1; ++i){
            double xi = x.getReal(i);
            double xj = x.getReal(i + 1);
            double sinSquared = Math.sin(Math.sqrt((100 * (xi*xi)) + (xj*xj)));
            sinSquared *= sinSquared;

            double squaredVal = (xi*xi) - (2*xi*xj) + (xj*xj);
            squaredVal *= squaredVal;

            sum += 0.5 + ((sinSquared - 0.5) / (1 + (0.001 * squaredVal)));
        }
        return sum;
    }
}
