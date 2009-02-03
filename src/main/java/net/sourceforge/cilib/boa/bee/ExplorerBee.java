/*
 * Copyright (C) 2003 - 2008
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.boa.bee;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.math.random.generator.Seeder;
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
	 * Default constructor. Creates a new instance of {@code ExplorerBee} with reasonable
	 * default values.
	 */
	public ExplorerBee() {
		random = new MersenneTwister(Seeder.getSeed());
		previousUpdatedIteration = -1;
		numberOfUpdates = 0;
		explorerBeeUpdateLimit = new ConstantControlParameter(1.0);
	}
	
	/**
	 * Copy constructor. Creates a copy of the provided instance.
	 * @param copy reference to explorer bee that deep copy is made of.
	 */
	public ExplorerBee(ExplorerBee copy) {
		this.random = copy.random;
		this.previousUpdatedIteration = copy.previousUpdatedIteration;
		this.numberOfUpdates = copy.numberOfUpdates;
		this.explorerBeeUpdateLimit = copy.explorerBeeUpdateLimit;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ExplorerBee getClone() {
		return new ExplorerBee(this);
	}

	/**
	 * Verifies it is allowed for a worker bee to convert to an explorer bee.
	 * @precondition  an algorithm is on the algorithm stack.
	 * @return whether the search is allowed.
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
	 * Returns a new random position.
	 * @precondition an algorithm is on the algorithm stack.
	 * @precondition the search is allowed.
	 * @param position random position with same dimension and bounds as given position.
	 * @return The new position.
	 */
	public Vector getNewPosition(Vector position) {
		previousUpdatedIteration = Algorithm.get().getIterations();
		numberOfUpdates++;

        Vector newPosition = position.getClone();
        newPosition.randomize();

        return newPosition;
	}

	public ControlParameter getExplorerBeeUpdateLimit() {
		return explorerBeeUpdateLimit;
	}

	public void setExplorerBeeUpdateLimit(ControlParameter explorerBeeUpdateLimit) {
		this.explorerBeeUpdateLimit = explorerBeeUpdateLimit;
	}

}
