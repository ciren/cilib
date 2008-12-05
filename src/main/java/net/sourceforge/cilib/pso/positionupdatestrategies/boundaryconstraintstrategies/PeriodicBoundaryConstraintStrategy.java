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
 * If a particle oversteps the upper boundary it gets re-initialised and placed near the lower
 * boundary and vice versa. References:
 * @inproceedings{ZXB04, author = "W.-J. Zhang and X.-F. Xie and D.-C. Bi", title = "Handling
 *                       boundary constraints for numerical optimization by particle swarm flying in
 *                       periodic search space", booktitle = "IEEE Congress on Evolutionary
 *                       Computation", month = jun, year = {2004}, volume = "2", pages =
 *                       {2307--2311} }
 * @author Wiehann Matthysen
 */
public class PeriodicBoundaryConstraintStrategy implements BoundaryConstraintStrategy {
	private static final long serialVersionUID = 2777840531497602339L;

	public PeriodicBoundaryConstraintStrategy() {
	}

	public PeriodicBoundaryConstraintStrategy(PeriodicBoundaryConstraintStrategy copy) {
	}

	public PeriodicBoundaryConstraintStrategy getClone() {
		return new PeriodicBoundaryConstraintStrategy(this);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sourceforge.cilib.pso.positionupdatestrategies.boundaryconstraintstrategies.BoundaryConstraintStrategy#constrainLower(net.sourceforge.cilib.type.types.Numeric, net.sourceforge.cilib.type.types.Numeric)
	 */
	public void constrainLower(Numeric position, Numeric velocity) {
		double upper = position.getBounds().getUpperBound();
		double lower = position.getBounds().getLowerBound();
		double range = Math.abs(upper - lower);
		Numeric desiredPosition = position.getClone();

		desiredPosition.set(position.getReal() + velocity.getReal());
		position.set(upper - (lower - desiredPosition.getReal()) % range);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sourceforge.cilib.pso.positionupdatestrategies.boundaryconstraintstrategies.BoundaryConstraintStrategy#constrainUpper(net.sourceforge.cilib.type.types.Numeric, net.sourceforge.cilib.type.types.Numeric)
	 */
	public void constrainUpper(Numeric position, Numeric velocity) {
		double upper = position.getBounds().getUpperBound();
		double lower = position.getBounds().getLowerBound();
		double range = Math.abs(upper - lower);
		Numeric desiredPosition = position.getClone();

		desiredPosition.set(position.getReal() + velocity.getReal());
		position.set(lower + (desiredPosition.getReal() - upper) % range);
	}
}
