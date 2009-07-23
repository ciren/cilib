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
 *
 * @author  engel
 */
public class Neumaier3 extends ContinuousFunction {
    private static final long serialVersionUID = 192809046725649930L;

    /**
     * Creates a new instance of Neumaier. Domain defaults to R(-900, 900)^30
     */
    public Neumaier3() {
        // TODO: Fix this constraint
        // constraint.add(new ContentValidator(new NeumaierValidator()));
        setDomain("R(-900, 900)^30");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Neumaier3 getClone() {
        return new Neumaier3();
    }

    /**
     * {@inheritDoc}
     */
    public Double getMinimum() {
        double dimension = getDimension();
        return (dimension * (dimension + 4.0) * (dimension - 1.0)) / 6.0;
    }

    /**
     * {@inheritDoc}
     */
    public Double evaluate(Vector input) {
        double tmp1 = 0;
        double tmp2 = 0;
        for (int i = 0; i < getDimension(); ++i) {
            tmp1 += (input.getReal(i) - 1) * (input.getReal(i) - 1);
        }
        for (int i = 1; i < getDimension(); ++i) {
            tmp2 += input.getReal(i) * input.getReal(i - 1);
        }
        return tmp1 - tmp2;
    }

}
