package net.sourceforge.cilib.entity.operators;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.util.Cloneable;

/**
 * 
 * @author gpampara
 *
 */
public interface Operator extends Cloneable {
	
	public Operator getClone();

	public void performOperation(Topology<? extends Entity> topology, Topology<Entity> offspring);

}
