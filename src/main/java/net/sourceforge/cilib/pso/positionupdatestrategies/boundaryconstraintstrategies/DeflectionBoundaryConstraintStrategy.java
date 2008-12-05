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

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.type.types.Numeric;

/**
 * Particles that overstep the boundary gets re-initialised to simulate a bouncing effect by
 * flipping the velocity (multiplying it with -1.0).
 * @author Wiehann Matthysen
 */
public class DeflectionBoundaryConstraintStrategy implements BoundaryConstraintStrategy {
	private static final long serialVersionUID = -1108623232830737053L;

	private ControlParameter velocityDampingFactor;

	public DeflectionBoundaryConstraintStrategy() {
		velocityDampingFactor = new ConstantControlParameter();
		velocityDampingFactor.setParameter(-1.0);
	}

	public DeflectionBoundaryConstraintStrategy(DeflectionBoundaryConstraintStrategy copy) {
		velocityDampingFactor = copy.velocityDampingFactor.getClone();
	}

	public DeflectionBoundaryConstraintStrategy getClone() {
		return new DeflectionBoundaryConstraintStrategy(this);
	}

	public void setVelocityDampingFactor(ControlParameter velocityDampingFactor) {
		this.velocityDampingFactor = velocityDampingFactor;
	}

	public ControlParameter getVelocityDampingFactor() {
		return velocityDampingFactor;
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
		position.set(lower + (lower - desiredPosition.getReal()) % range);
		velocity.set(velocity.getReal() * velocityDampingFactor.getParameter());
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
		position.set(upper - (desiredPosition.getReal() - upper) % range);
		velocity.set(velocity.getReal() * velocityDampingFactor.getParameter());
	}
}
