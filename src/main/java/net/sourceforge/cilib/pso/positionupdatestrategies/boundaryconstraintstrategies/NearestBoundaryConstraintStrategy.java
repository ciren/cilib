/*
 * NearestBoundaryConstraintStrategy.java
 *
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

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.math.MathUtil;
import net.sourceforge.cilib.type.types.Numeric;

/**
 * If a particle oversteps the boundary it gets re-initialised and placed on the overstepped
 * boundary. A terbulence probability gets specified to allow particles to escape the boundaries.
 * References:
 * @inproceedings{ZXB04, author = "W.-J. Zhang and X.-F. Xie and D.-C. Bi", title = "Handling
 *                       boundary constraints for numerical optimization by particle swarm flying in
 *                       periodic search space", booktitle = "IEEE Congress on Evolutionary
 *                       Computation", month = jun, year = {2004}, volume = "2", pages =
 *                       {2307--2311} }
 * @inproceedings{HW07, author = "S. Helwig and R. Wanka", title = "Particle Swarm Optimization in
 *                      High-Dimensional Bounded Search Spaces", booktitle = "Proceedings of the
 *                      2007 IEEE Swarm Intelligence Symposium", month = apr, year = {2007}, pages =
 *                      {198--205} }
 * @author Wiehann Matthysen
 */
public class NearestBoundaryConstraintStrategy implements BoundaryConstraintStrategy {
	private static final long serialVersionUID = 2444348297389576657L;

	private ControlParameter terbulenceProbability;

	public NearestBoundaryConstraintStrategy() {
		terbulenceProbability = new ConstantControlParameter();
		terbulenceProbability.setParameter(0.0);
	}

	public NearestBoundaryConstraintStrategy(NearestBoundaryConstraintStrategy copy) {
		terbulenceProbability = copy.terbulenceProbability.getClone();
	}

	public NearestBoundaryConstraintStrategy getClone() {
		return new NearestBoundaryConstraintStrategy(this);
	}

	public void setTerbulenceProbability(ControlParameter terbulenceProbability) {
		this.terbulenceProbability = terbulenceProbability;
	}

	public ControlParameter getTerbulenceProbability() {
		return terbulenceProbability;
	}

	/**
	 * {@inheritDoc}
	 */
	public void constrainLower(Numeric position, Numeric velocity) {
		double upper = position.getUpperBound();
		double lower = position.getLowerBound();
		double range = Math.abs(upper - lower);
		Numeric previousPosition = position.getClone();

		position.set(lower);	// lower boundary is inclusive
		if (MathUtil.random() < terbulenceProbability.getParameter()) {
			position.set(position.getReal() + MathUtil.random() * range);
		}

		velocity.set(position.getReal() - previousPosition.getReal());
	}

	/**
	 * {@inheritDoc}
	 */
	public void constrainUpper(Numeric position, Numeric velocity) {
		double upper = position.getUpperBound();
		double lower = position.getLowerBound();
		double range = Math.abs(upper - lower);
		Numeric previousPosition = position.getClone();

		position.set(upper - INFIMUM);	// upper boundary is exclusive
		if (MathUtil.random() < terbulenceProbability.getParameter()) {
			position.set(position.getReal() - MathUtil.random() * range);
		}

		velocity.set(position.getReal() - previousPosition.getReal());
	}
}
