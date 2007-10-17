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
		velocityDampingFactor = copy.velocityDampingFactor.clone();
	}

	public DeflectionBoundaryConstraintStrategy clone() {
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
		double upper = position.getUpperBound();
		double lower = position.getLowerBound();
		double range = Math.abs(upper - lower);
		Numeric desiredPosition = position.clone();

		desiredPosition.set(position.getReal() + velocity.getReal());
		position.set(lower + (lower - desiredPosition.getReal()) % range);
		velocity.set(velocity.getReal() * velocityDampingFactor.getParameter());
	}

	/*
	 * (non-Javadoc)
	 * @see net.sourceforge.cilib.pso.positionupdatestrategies.boundaryconstraintstrategies.BoundaryConstraintStrategy#constrainUpper(net.sourceforge.cilib.type.types.Numeric, net.sourceforge.cilib.type.types.Numeric)
	 */
	public void constrainUpper(Numeric position, Numeric velocity) {
		double upper = position.getUpperBound();
		double lower = position.getLowerBound();
		double range = Math.abs(upper - lower);
		Numeric desiredPosition = position.clone();

		desiredPosition.set(position.getReal() + velocity.getReal());
		position.set(upper - (desiredPosition.getReal() - upper) % range);
		velocity.set(velocity.getReal() * velocityDampingFactor.getParameter());
	}
}
