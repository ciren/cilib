package net.sourceforge.cilib.boa.bees;

import net.sourceforge.cilib.boa.positionupdatestrategies.BeePositionUpdateStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Super interface for all types of bees in the artificial bee algorithm
 * @author Andrich
 *
 */
public interface HoneyBee extends Entity {
	
	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.entity.Entity#getClone()
	 */
	public HoneyBee getClone();
	
	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.entity.Entity#getFitness()
	 */
	public Fitness getFitness();
	
	/**
	 * Updates the position of the bee based on the neighbouring nectar content
	 */
	public void updatePosition();
	
	/**
	 * Gets the bee's position (contents)
	 * @return the position
	 */
	public Vector getPosition();
	
	/**
	 * Sets the bee's position (contents)
	 * @return the position
	 */
	public void setPosition(Vector position);
	
	/**
	 * Getter method for the position update strategy
	 * @return the position update strategy
	 */
	public BeePositionUpdateStrategy getPositionUpdateStrategy();
	
}
