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
		position.set(position.getLowerBound());	// lower boundary is inclusive
	}

	/*
	 * (non-Javadoc)
	 * @see net.sourceforge.cilib.pso.positionupdatestrategies.boundaryconstraintstrategies.BoundaryConstraintStrategy#constrainUpper(net.sourceforge.cilib.type.types.Numeric, net.sourceforge.cilib.type.types.Numeric)
	 */
	public void constrainUpper(Numeric position, Numeric velocity) {
		position.set(position.getUpperBound() - INFIMUM);	// upper boundary is exclusive
	}
}
