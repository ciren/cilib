package net.sourceforge.cilib.pso.positionupdatestrategies.boundaryconstraintstrategies;

import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.type.types.Numeric;

/**
 * Instead of making use of <i>reactive</i> Boundary Constraints that reinitialise an entire
 * {@linkplain Particle} (or components thereof), this class is a <b>proactive</b> approach to
 * prevent the {@linkplain Particle} from moving outside of the domain. The component of the
 * {@linkplain Particle} that will be outside of the domain is placed on the boundary of the domain
 * and the corresponding velocity component is recalculated (inverting the direction), effectively
 * making the {@linkplain Particle} bounce off the sides of the domain. The effect achieved is a
 * skewed type of reflection with built-in velocity damping.
 * @author Theuns Cloete
 */
public class BouncingBoundaryConstraintStrategy implements BoundaryConstraintStrategy {
	private static final long serialVersionUID = -2085380951650975909L;

	public BouncingBoundaryConstraintStrategy() {
	}

	public BouncingBoundaryConstraintStrategy(BouncingBoundaryConstraintStrategy rhs) {
	}

	public BouncingBoundaryConstraintStrategy clone() {
		return new BouncingBoundaryConstraintStrategy(this);
	}

	/*
	 * (non-Javadoc)
	 * @see net.sourceforge.cilib.pso.positionupdatestrategies.boundaryconstraintstrategies.BoundaryConstraintStrategy#constrainLower(net.sourceforge.cilib.type.types.Numeric,
	 *      net.sourceforge.cilib.type.types.Numeric)
	 */
	public void constrainLower(Numeric position, Numeric velocity) {
		Numeric previousPosition = position.clone();

		position.set(position.getLowerBound());	// lower boundary is inclusive
		velocity.set(previousPosition.getReal() - position.getReal());
	}

	/*
	 * (non-Javadoc)
	 * @see net.sourceforge.cilib.pso.positionupdatestrategies.boundaryconstraintstrategies.BoundaryConstraintStrategy#constrainUpper(net.sourceforge.cilib.type.types.Numeric,
	 *      net.sourceforge.cilib.type.types.Numeric)
	 */
	public void constrainUpper(Numeric position, Numeric velocity) {
		Numeric previousPosition = position.clone();

		position.set(position.getUpperBound() - INFIMUM); // upper boundary is exclusive
		velocity.set(previousPosition.getReal() - position.getReal());
	}
}
