/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.boa.bee;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Cloneable;

/**
 * Represents the explorer bee in the algorithm. To emulate the functionality of
 * the explorer bee in the hive, a random search position is generated upon
 * request if it is allowed. Keeps track of how many updates have occurred via
 * {@link #getNumberOfUpdates()} and which iteration the previous update occurred.
 */
public class ExplorerBee implements Cloneable {

    private static final long serialVersionUID = 1068799535328234923L;
    private int previousUpdatedIteration;    //used to check whether the algorithm has entered a new iteration
    private int numberOfUpdates;            //how many have occurred in current iteration
    private ControlParameter explorerBeeUpdateLimit;

    /**
     * Default constructor. Creates a new instance of {@code ExplorerBee} with reasonable
     * default values.
     */
    public ExplorerBee() {
        previousUpdatedIteration = -1;
        numberOfUpdates = 0;
        explorerBeeUpdateLimit = ConstantControlParameter.of(1.0);
    }

    /**
     * Copy constructor. Creates a copy of the provided instance.
     * @param copy reference to explorer bee that deep copy is made of.
     */
    public ExplorerBee(ExplorerBee copy) {
        this.previousUpdatedIteration = copy.previousUpdatedIteration;
        this.numberOfUpdates = copy.numberOfUpdates;
        this.explorerBeeUpdateLimit = copy.explorerBeeUpdateLimit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExplorerBee getClone() {
        return new ExplorerBee(this);
    }

    /**
     * Verifies it is allowed for a worker bee to convert to an explorer bee.
     * @param currentIteration the current iteration of the algorithm on the stack.
     * @return whether the search is allowed.
     */
    public boolean searchAllowed(int currentIteration) {
        if (previousUpdatedIteration == currentIteration) {
            //TODO: Add variable number of updates allowed
            if (Double.compare(numberOfUpdates, explorerBeeUpdateLimit.getParameter()) < 0) {
                return true;
            }
            return false;
        } else {
            numberOfUpdates = 0;
        }
        return true;
    }

    /**
     * Returns a new random position.
     *
     * @param currentIteration  the current iteration of the algorithm on the
     *                          stack.
     * @param position          random position with same dimension and bounds
     *                          as given position.
     * @return                  the new position.
     */
    public Vector getNewPosition(int currentIteration, Vector position) {
        previousUpdatedIteration = currentIteration;
        numberOfUpdates++;

        return Vector.newBuilder().copyOf(position).buildRandom();
    }

    /**
     * Gets the explorer bee update limit.
     * @return the explorer bee update limit.
     */
    public ControlParameter getExplorerBeeUpdateLimit() {
        return explorerBeeUpdateLimit;
    }

    /**
     * Sets the explorer bee update limit.
     * @param explorerBeeUpdateLimit the new explorer bee update limit.
     */
    public void setExplorerBeeUpdateLimit(ControlParameter explorerBeeUpdateLimit) {
        this.explorerBeeUpdateLimit = explorerBeeUpdateLimit;
    }

    /**
     * Gets the number of updates done for the current iteration.
     * @return the number of iterations done for the current iteration.
     */
    public int getNumberOfUpdates() {
        return numberOfUpdates;
    }

    /**
     * Sets the number of updates done for the current iteration.
     * @param numberOfUpdates the new number of iterations done for the current iteration.
     */
    public void setNumberOfUpdates(int numberOfUpdates) {
        this.numberOfUpdates = numberOfUpdates;
    }

    /**
     * Gets the last iteration an update was done.
     * @return the last iteration an update was done.
     */
    public int getPreviousUpdatedIteration() {
        return previousUpdatedIteration;
    }

    /**
     * Sets the last iteration an update was done.
     * @param previousUpdatedIteration the last iteration an update was done.
     */
    public void setPreviousUpdatedIteration(int previousUpdatedIteration) {
        this.previousUpdatedIteration = previousUpdatedIteration;
    }
}
