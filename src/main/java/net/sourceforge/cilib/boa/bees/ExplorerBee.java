package net.sourceforge.cilib.boa.bees;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.Seeder;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.Cloneable;

/**
 * Represents the explorer bee in the algorithm. To emulate the functionality of the explorer bee in the hive, a random search
 * position is generated upon request if it is allowed. Keeps track of how many updates have occurred via numberOfUpdates and which iteration
 * the previous update occurred.
 * 
 * @author Andrich
 * 
 */
public class ExplorerBee implements Cloneable {
	private static final long serialVersionUID = 1068799535328234923L;
	
	private MersenneTwister random;			//generates a random position
	private int previousUpdatedIteration;	//used to check whether the algorithm has entered a new iteration
	private int numberOfUpdates;			//how many have occured in current iteration
	private ControlParameter explorerBeeUpdateLimit;

	/**
	 * Default constructor
	 */
	public ExplorerBee() {
		random = new MersenneTwister(Seeder.getSeed());
		previousUpdatedIteration = -1;
		numberOfUpdates = 0;
		explorerBeeUpdateLimit = new ConstantControlParameter(1.0);
	}
	
	/**
	 * Copy constructor
	 * @param copy reference to explorer bee that deep copy is made of
	 */
	public ExplorerBee(ExplorerBee copy) {
		this.random = copy.random;
		this.previousUpdatedIteration = copy.previousUpdatedIteration;
		this.numberOfUpdates = copy.numberOfUpdates;
		this.explorerBeeUpdateLimit = copy.explorerBeeUpdateLimit;
	}
	
	public ExplorerBee getClone() {
		return new ExplorerBee(this);
	}

	/**
	 * Verifies it is allowed for a worker bee to convert to an explorer bee
	 * @precondition  an algorithm is on the algorithm stack
	 * @return whether the search is allowed
	 */
	public boolean searchAllowed() {
		int currentIteration = Algorithm.get().getIterations();
		if (previousUpdatedIteration == currentIteration) {
			//TODO: Add variable number of updates allowed 
			if (Double.compare(numberOfUpdates, explorerBeeUpdateLimit.getParameter()) < 0)
				return true;
			return false;
		}
		else {
			numberOfUpdates = 0;
		}
		return true;
	}

	/**
	 * Returns a new random position
	 * @precondition an algorithm is on the algorithm stack
	 * @precondition the search is allowed
	 * @param position random position with same dimension and bounds as given position
	 * @return
	 */
	public Vector getNewPosition(Vector position) {
		previousUpdatedIteration = Algorithm.get().getIterations();
		numberOfUpdates++;
		Vector newPosition = new Vector(position.size());
		for (int i = 0; i < position.size(); i++) {
			double lowerBound = ((Real)position.get(i)).getLowerBound();
			double upperBound = ((Real)position.get(i)).getUpperBound();
			Real real = new Real((upperBound-lowerBound)*random.nextDouble() + lowerBound);
			real.setLowerBound(lowerBound);
			real.setUpperBound(upperBound);
			newPosition.add(real);
		}
		return newPosition;
	}

	public ControlParameter getExplorerBeeUpdateLimit() {
		return explorerBeeUpdateLimit;
	}

	public void setExplorerBeeUpdateLimit(ControlParameter explorerBeeUpdateLimit) {
		this.explorerBeeUpdateLimit = explorerBeeUpdateLimit;
	}

}
