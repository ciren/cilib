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
 * The continuous Step function. It is the same as the normal step function, however,
 * it is continuous and not discrete.
 *
 * <p>
 * The default domain of the function is defined to be R(-100.0, 100.0)^30
 *
 * @author Andries Engelbrecht
 */
public class ContinuousStep extends ContinuousFunction {
    private static final long serialVersionUID = 4962101545621686038L;

    /** Creates a new instance of Step. */
    public ContinuousStep() {
        setDomain("R(-100.0, 100.0)^30");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContinuousStep getClone() {
        return new ContinuousStep();
    }

    /**
     * Get the defined minimum of the {@linkplain Function}. The minimum is defined to
     * be the value of 0.0.
     * @return An {@code Object} containing the value 0.0.
     */
    public Double getMinimum() {
        return 0.0;
    }

    /**
     * Get the defined maximum of the {@linkplain Function}. The maximum value is defined to
     * be the value of 55.0.
     * @return An {@code Object} containing the value 55.0.
     */
    public Double getMaximum() {
        return 55.0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double evaluate(Vector input) {
        double sum = 0.0;
        for (int i = 0; i < getDimension(); ++i) {
            sum += (input.getReal(i) + 0.5) * (input.getReal(i) + 0.5);
        }
        return sum;
    }

}
