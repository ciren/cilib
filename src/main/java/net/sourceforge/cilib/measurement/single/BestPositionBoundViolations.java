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
package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Checks of the best position in the current population violates
 * boundary constraints.
 *
 * @author  Andries Engelbrecht
 */
public class BestPositionBoundViolations implements Measurement<Real> {
    private static final long serialVersionUID = 8707987903689437725L;

    /**
     * {@inheritDoc}
     */
    @Override
    public BestPositionBoundViolations getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDomain() {
        return "R";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Real getValue(Algorithm algorithm) {
        Vector gbest = (Vector) algorithm.getBestSolution().getPosition();

        double numberOfViolations = 0.0;
        for (Numeric position : gbest) {
            Bounds bounds = position.getBounds();

            if (!bounds.isInsideBounds(position.getReal())) {
                numberOfViolations++;
                break;
            }
        }

        return new Real(numberOfViolations);
    }

}
