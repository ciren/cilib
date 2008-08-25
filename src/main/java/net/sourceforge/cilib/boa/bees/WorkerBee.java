package net.sourceforge.cilib.boa.bees;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.boa.ABC;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;

/**
 * A worker bee that forages for food and updates the hive information by dancing
 * @author Andrich
 */
public class WorkerBee extends AbstractBee implements HoneyBee {
	private ControlParameter forageLimit;
	private int failureCount;

	public WorkerBee() {
		failureCount = 0;
		this.forageLimit = new ConstantControlParameter(500);
	}

	public WorkerBee(WorkerBee copy) {
		super(copy);
		failureCount = copy.failureCount;
		this.forageLimit = copy.forageLimit.getClone();
	}

	@Override
	public WorkerBee getClone() {
		return new WorkerBee(this);
	}

	@Override
	public void updatePosition() {
		ABC algorithm = (ABC) Algorithm.get();
		HoneyBee target = targetSelectionStrategy.select(algorithm.getWorkerTopology());
		
		while (target == this) {
			target = targetSelectionStrategy.select(algorithm.getWorkerTopology());
		}
		
		boolean success = this.positionUpdateStrategy.updatePosition(this,target);
		if (!success) {
			failureCount++;
			if (failureCount >= forageLimit.getParameter())
			{
				failureCount = 0;
				ExplorerBee explorerBee = ((ABC)Algorithm.get()).getExplorerBee();
				if (explorerBee.searchAllowed()) {
					this.setPosition(explorerBee.getNewPosition(this.getPosition()));
				}
			}
		}
	}
	
	public ControlParameter getForageLimit() {
		return forageLimit;
	}

	public void setForageLimit(ControlParameter forageLimit) {
		this.forageLimit = forageLimit;
	}

	private static final long serialVersionUID = 3657591650621784765L;
}
