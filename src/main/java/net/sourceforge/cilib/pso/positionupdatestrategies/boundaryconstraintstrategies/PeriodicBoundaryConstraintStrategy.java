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

	public PeriodicBoundaryConstraintStrategy clone() {
		return new PeriodicBoundaryConstraintStrategy(this);
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
		position.set(upper - (lower - desiredPosition.getReal()) % range);
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
		position.set(lower + (desiredPosition.getReal() - upper) % range);
	}
}
