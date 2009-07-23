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
 * UrsemF1 function.
 *
 * <p>Title: CILib</p>
 * <p>Description: CILib (Computational Intelligence Library)</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Clive Naicker
 * @version 1.0
 */
public class UrsemF1 extends ContinuousFunction {
    private static final long serialVersionUID = -2595919942608678319L;

    public UrsemF1() {
        //constraint.add(new DimensionValidator(2));
        setDomain("R(-2.5, 3)^2");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UrsemF1 getClone() {
        return new UrsemF1();
    }

    public Double getMinimum() {
        return 4.81681;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double evaluate(Vector input) {
        double x = input.getReal(0);
        double y = input.getReal(1);

        double result = Math.sin(2.0*x - 0.5*Math.PI) + 3.0*Math.cos(y) + 0.5*x;
        return result;
    }

}
