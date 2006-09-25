package net.sourceforge.cilib.pso.iterationstrategies;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.type.DomainParser;

public class ReinitilisationBoundary implements BoundaryConstraint {

	public void enforce(Entity entity) {
		
		if (!DomainParser.getInstance().isInsideBounds(entity.get())) {
			entity.reinitialise();
		}

	}

}
