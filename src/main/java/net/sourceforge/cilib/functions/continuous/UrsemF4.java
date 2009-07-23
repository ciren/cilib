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
 * UrsemF4 function.
 *
 * <p>
 * <p>Title: CILib</p>
 * <p>Description: CILib (Computational Intelligence Library)</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Clive Naicker
 * @version 1.0
 */
public class UrsemF4 extends ContinuousFunction {
    private static final long serialVersionUID = 6177837410317967257L;

    public UrsemF4() {
        //constraint.add(new DimensionValidator(2));
        setDomain("R(-2, 2)^2");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UrsemF4 getClone() {
        return new UrsemF4();
    }

    /**
     * Get the minimum of the function. It is defined to be a value of <code>1.5</code>.
     * @return The function minimum value.
     */
    public Double getMinimum() {
        return 1.5;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double evaluate(Vector input) {
        double x = input.getReal(0);
        double y = input.getReal(1);
        double result = 3.0*Math.sin(0.5*Math.PI*x + 0.5*Math.PI)*(2.0 - Math.sqrt(x*x + y*y)/4.0);
        return result;
    }
}
