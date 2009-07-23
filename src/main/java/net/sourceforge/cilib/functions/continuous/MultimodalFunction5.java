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
 * TODO: Complete this javadoc.
 */
public class MultimodalFunction5 extends ContinuousFunction {
    private static final long serialVersionUID = -8704025552791904890L;

    /**
     * Create a new instance of {@linkplain MultimodalFunction5}.
     */
    public MultimodalFunction5() {
        setDomain("R(-6, 6)^2");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MultimodalFunction5 getClone() {
        return new MultimodalFunction5();
    }

    /**
     * Get the minimum of the function. It is defined to be a value of <code>0.0</code>.
     * @return The function minimum value.
     */
    public Double getMinimum() {
        return 0.0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double evaluate(Vector input) {
        double x = input.getReal(0);
        double y = input.getReal(1);
        double result;

        result = 200 - Math.pow((x*x + y - 11), 2) - Math.pow((x + y*y - 7), 2);

        return result;
    }

}
