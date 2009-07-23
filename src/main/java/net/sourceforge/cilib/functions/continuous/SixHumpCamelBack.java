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
package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * SixHumpCamelBack function.
 *
 * <p>Title: CILib</p>
 * <p>Description: CILib (Computational Intelligence Library)</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 *
 * Characteristics:
 * <ul>
 * <li>Multimodal</li>
 * <li>Continuous</li>
 * <li>Non Separable</li>
 * </ul>
 *
 * f(x) = -1.0316; x = (-0.0898, 0.1726); x = (0.0898, -0.1726)
 * x_1 e [-3, 3]; x_2 e [-2, 2]
 *
 * @author Clive Naicker
 * @version 1.0
 */
public class SixHumpCamelBack extends ContinuousFunction {
    private static final long serialVersionUID = -3834640752316926216L;

    public SixHumpCamelBack() {
        //constraint.add(new DimensionValidator(2));
        setDomain("R(-3,3),R(-2,2)");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SixHumpCamelBack getClone() {
        return new SixHumpCamelBack();
    }

    /**
     * Get the minimum of the function. It is defined to be a value of <code>-1.0316</code>.
     * @return The function minimum value.
     */
    public Double getMinimum() {
        return -1.0316;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double evaluate(Vector input) {
        double x1 = input.getReal(0);
        double x2 = input.getReal(1);

        double result = (4 - 2.1*x1*x1 + Math.pow(x1, 4.0)/3.0)*x1*x1 + x1*x2 + 4*(x2*x2 -1)*x2*x2;
        return result;
    }

}
