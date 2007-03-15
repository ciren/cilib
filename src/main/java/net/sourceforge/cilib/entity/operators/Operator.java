package net.sourceforge.cilib.entity.operators;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;

/**
 * 
 * @author gpampara
 *
 */
public interface Operator {
	
	public Operator clone();

	public void performOperation(Topology<? extends Entity> topology, Topology<Entity> offspring);

}
