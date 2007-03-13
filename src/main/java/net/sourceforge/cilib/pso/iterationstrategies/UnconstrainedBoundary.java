package net.sourceforge.cilib.pso.iterationstrategies;

import net.sourceforge.cilib.entity.Entity;

/**
 * 
 * @author gpampara
 *
 */
public class UnconstrainedBoundary implements BoundaryConstraint {

	private static final long serialVersionUID = -6672863576480662484L;

	public void enforce(Entity entity) {
		// Do nothing as there is no boudary contraint to enforce
	}

}
