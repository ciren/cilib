package net.sourceforge.cilib.pso.iterationstrategies;

import net.sourceforge.cilib.entity.Entity;

public interface BoundaryConstraint {
	
	public void enforce(Entity entity);

}
