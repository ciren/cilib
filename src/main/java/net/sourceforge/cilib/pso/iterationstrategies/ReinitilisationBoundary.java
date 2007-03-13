package net.sourceforge.cilib.pso.iterationstrategies;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.type.DomainParser;

public class ReinitilisationBoundary implements BoundaryConstraint {

	private static final long serialVersionUID = -512973040124015665L;

	public void enforce(Entity entity) {
		
		if (!DomainParser.getInstance().isInsideBounds(entity.get())) {
			entity.reinitialise();
		}

	}

}
