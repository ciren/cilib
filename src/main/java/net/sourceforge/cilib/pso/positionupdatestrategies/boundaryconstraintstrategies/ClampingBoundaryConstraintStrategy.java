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
 * Particles that overstep the boundary is placed on that boundary. The velocity is not affected.
 * @author Wiehann Matthysen
 */
public class ClampingBoundaryConstraintStrategy implements BoundaryConstraintStrategy {
	private static final long serialVersionUID = -3439755620178365634L;

	public ClampingBoundaryConstraintStrategy() {
	}

	public ClampingBoundaryConstraintStrategy(ClampingBoundaryConstraintStrategy copy) {
	}

	public ClampingBoundaryConstraintStrategy getClone() {
		return new ClampingBoundaryConstraintStrategy(this);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sourceforge.cilib.pso.positionupdatestrategies.boundaryconstraintstrategies.BoundaryConstraintStrategy#constrainLower(net.sourceforge.cilib.type.types.Numeric, net.sourceforge.cilib.type.types.Numeric)
	 */
	public void constrainLower(Numeric position, Numeric velocity) {
		position.set(position.getBounds().getLowerBound());	// lower boundary is inclusive
	}

	/*
	 * (non-Javadoc)
	 * @see net.sourceforge.cilib.pso.positionupdatestrategies.boundaryconstraintstrategies.BoundaryConstraintStrategy#constrainUpper(net.sourceforge.cilib.type.types.Numeric, net.sourceforge.cilib.type.types.Numeric)
	 */
	public void constrainUpper(Numeric position, Numeric velocity) {
		position.set(position.getBounds().getUpperBound() - INFIMUM);	// upper boundary is exclusive
	}
}
