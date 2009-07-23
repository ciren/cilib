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
 * Shubert function.
 *
 * <p>Title: CILib</p>
 * <p>Description: CILib (Computational Intelligence Library)</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author Clive Naicker
 * @version 1.0
 */
public class Shubert extends ContinuousFunction {
    private static final long serialVersionUID = 3213789483391643466L;

    /**
     * Create an instance of {@linkplain Shubert}. Domain is set to R(-10,10)^2 by default.
     */
    public Shubert() {
        setDomain("R(-10, 10)^2");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shubert getClone() {
        return new Shubert();
    }

    /**
     * {@inheritDoc}
     */
    public Double getMinimum() {
        return -186.7309088;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double evaluate(Vector input) {
        double result = 1.0;
        for (int i=0; i < getDimension(); ++i) {
            double result2 = 0.0;
            for (int j=1; j<=5; j++) {
                result2 += j*Math.cos((j+1)*input.getReal(i) + j);
            }
            result *= result2;
        }
        return result;
    }
}
