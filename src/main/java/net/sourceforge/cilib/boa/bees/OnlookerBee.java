package net.sourceforge.cilib.boa.bees;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.boa.ABC;


/**
 * Represents an onlooker bee in the hive.
 * @author Andrich
 *
 */
public class OnlookerBee extends AbstractBee implements HoneyBee {
	
	/**
	 * Default constructor
	 */
	public OnlookerBee() {
		
	}
	
	/**
	 * Copy constructor
	 * @param copy reference that is deep copied
	 */
	public OnlookerBee(AbstractBee copy) {
		super(copy);
	}

	@Override
	public OnlookerBee getClone() {
		return new OnlookerBee(this);
	}

	@Override
	public void updatePosition() {
		ABC algorithm = (ABC) Algorithm.get();
		HoneyBee target = targetSelectionStrategy.select(algorithm.getWorkerTopology());
		
		while (target == this) {
			target = targetSelectionStrategy.select(algorithm.getWorkerTopology());
		}
		this.positionUpdateStrategy.updatePosition(this,target);
	}
	
	private static final long serialVersionUID = -4714791530850285930L;

}
