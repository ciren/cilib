package net.sourceforge.cilib.boa.positionupdatestrategies;

import net.sourceforge.cilib.boa.bees.HoneyBee;


/**
 * Interface for a bee position update strategy
 * 
 * @author Andrich
 */
public interface BeePositionUpdateStrategy {
	
	public BeePositionUpdateStrategy getClone();
	
	public boolean updatePosition(HoneyBee bee, HoneyBee other);

}
