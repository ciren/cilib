/*
 * Copyright (C) 2003 - 2008
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
package net.sourceforge.cilib.pso.positionupdatestrategies.boundaryconstraintstrategies;

import net.sourceforge.cilib.type.types.Numeric;

/**
 * Instead of making use of <i>reactive</i> Boundary Constraints that reinitialise an entire
 * {@linkplain Particle} (or components thereof), this class is a <b>proactive</b> approach to
 * prevent the {@linkplain Particle} from moving outside of the domain. The component of the
 * {@linkplain Particle} that will be outside of the domain is placed on the boundary of the domain
 * and the corresponding velocity component is recalculated (inverting the direction), effectively
 * making the {@linkplain Particle} bounce off the sides of the domain. The effect achieved is a
 * skewed type of reflection with built-in velocity damping.
 */
public class BouncingBoundaryConstraintStrategy implements BoundaryConstraintStrategy {
    private static final long serialVersionUID = -2085380951650975909L;

    public BouncingBoundaryConstraintStrategy() {
    }

    public BouncingBoundaryConstraintStrategy(BouncingBoundaryConstraintStrategy rhs) {
    }

    public BouncingBoundaryConstraintStrategy getClone() {
        return new BouncingBoundaryConstraintStrategy(this);
    }

    /*
     * (non-Javadoc)
     * @see net.sourceforge.cilib.pso.positionupdatestrategies.boundaryconstraintstrategies.BoundaryConstraintStrategy#constrainLower(net.sourceforge.cilib.type.types.Numeric,
     *      net.sourceforge.cilib.type.types.Numeric)
     */
    public void constrainLower(Numeric position, Numeric velocity) {
        Numeric previousPosition = position.getClone();

        position.set(position.getBounds().getLowerBound());    // lower boundary is inclusive
        velocity.set(previousPosition.getReal() - position.getReal());
    }

    /*
     * (non-Javadoc)
     * @see net.sourceforge.cilib.pso.positionupdatestrategies.boundaryconstraintstrategies.BoundaryConstraintStrategy#constrainUpper(net.sourceforge.cilib.type.types.Numeric,
     *      net.sourceforge.cilib.type.types.Numeric)
     */
    public void constrainUpper(Numeric position, Numeric velocity) {
        Numeric previousPosition = position.getClone();

        position.set(position.getBounds().getUpperBound() - INFIMUM); // upper boundary is exclusive
        velocity.set(previousPosition.getReal() - position.getReal());
    }
}
