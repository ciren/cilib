package net.sourceforge.cilib.pso.iterationstrategies;

import java.io.Serializable;

import net.sourceforge.cilib.entity.Entity;

public interface BoundaryConstraint extends Serializable {
	
	public void enforce(Entity entity);

}
