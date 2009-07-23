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
 * @author  Edwin Peer
 */
public class NastyBenchmark extends ContinuousFunction {
    private static final long serialVersionUID = 6848836780892359015L;

    /**
     * Create a new instance of {@linkplain NastyBenchmark}.
     */
    public NastyBenchmark() {
        setDomain("R(-500, 500)^30");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NastyBenchmark getClone() {
        return new NastyBenchmark();
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
        double tmp = 0;
        for (int i = 0; i < getDimension(); ++i) {
            double factor = (input.getReal(i) - (i + 1));
            tmp += factor * factor;
        }
        return tmp;
    }

}
