/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.boa.bee;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.boa.ABC;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;

/**
 * A worker bee that forages for food and updates the hive information by dancing.
 */
public class WorkerBee extends AbstractBee {

    private static final long serialVersionUID = 3657591650621784765L;
    private ControlParameter forageLimit;
    private int failureCount;

    /**
     * Create a new instance with reasonable defaults set.
     */
    public WorkerBee() {
        failureCount = 0;
        this.forageLimit = ConstantControlParameter.of(500);
    }

    /**
     * Copy constructor. Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public WorkerBee(WorkerBee copy) {
        super(copy);
        failureCount = copy.failureCount;
        this.forageLimit = copy.forageLimit.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WorkerBee getClone() {
        return new WorkerBee(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePosition() {
        ABC algorithm = (ABC) AbstractAlgorithm.get();
        HoneyBee target = targetSelectionStrategy.on(algorithm.getWorkerBees()).select();

//        while (target == this) {
//            target = targetSelectionStrategy.select(algorithm.getWorkerBees());
//        }

        boolean success = this.positionUpdateStrategy.updatePosition(this, target);
        if (!success) {
            failureCount++;
            if (failureCount >= forageLimit.getParameter()) {
                failureCount = 0;
                ExplorerBee explorerBee = algorithm.getExplorerBee();
                if (explorerBee.searchAllowed(algorithm.getIterations())) {
                    this.setPosition(explorerBee.getNewPosition(algorithm.getIterations(), this.getPosition()));
                }
            }
        }
    }

    /**
     * Get the forage limit.
     * @return The {@linkplain ControlParameter} representing the forage limit.
     */
    public ControlParameter getForageLimit() {
        return forageLimit;
    }

    /**
     * Set the forage limit.
     * @param forageLimit The limit to set.
     */
    public void setForageLimit(ControlParameter forageLimit) {
        this.forageLimit = forageLimit;
    }

    /**
     * Gets the failure count.
     * @return the number of times the bee has failed to find a better position.
     */
    public int getFailureCount() {
        return failureCount;
    }

    /**
     * Sets the failure count.
     * @param failureCount the new number of times the bee has failed to find a better position.
     */
    public void setFailureCount(int failureCount) {
        this.failureCount = failureCount;
    }
}
