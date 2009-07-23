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
public class MultimodalFunction4 extends ContinuousFunction {
    private static final long serialVersionUID = -957215773660609565L;

    /**
     * Create a new instance of {@linkplain MultimodalFunction4}.
     */
    public MultimodalFunction4() {
        setDomain("R(0, 1)^1");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MultimodalFunction4 getClone() {
        return new MultimodalFunction4();
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
        double dResult = 0.0;
        for (int i = 0; i < getDimension(); i++) {
            double x = Math.pow(Math.sin(5.0 * Math.PI * (Math.pow(input.getReal(i), 0.75) - 0.05)), 6.0);
            double exp1 = -2.0 * Math.log(2);
            double exp2 = Math.pow((input.getReal(i) - 0.08) / 0.854, 2.0);
            double y = Math.exp(exp1 * exp2);
            dResult += x * y;
        }
        return dResult;
    }
}
