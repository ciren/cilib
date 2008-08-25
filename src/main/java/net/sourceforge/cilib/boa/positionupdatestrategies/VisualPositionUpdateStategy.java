package net.sourceforge.cilib.boa.positionupdatestrategies;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.boa.bees.HoneyBee;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Represents the visual exploration strategy used by bees to choose a their next
 * forage patch close by.
 * @author Andrich
 *
 */
public class VisualPositionUpdateStategy implements BeePositionUpdateStrategy {

	@Override
	public VisualPositionUpdateStategy getClone() {
		return new VisualPositionUpdateStategy();
	}

	@Override
	public boolean updatePosition(HoneyBee bee, HoneyBee otherBee) {
		MersenneTwister twister = new MersenneTwister();
		int j = twister.nextInt(bee.getDimension());
		
		Vector newPosition = (Vector)bee.getPosition();
		Vector oldPosition = (Vector)bee.getPosition().getClone();
		Vector otherPosition = (Vector)otherBee.getPosition();
		double value = ((Real)newPosition.get(j)).getReal();
		double other = ((Real)otherPosition.get(j)).getReal();
		Real newValue = (Real)newPosition.get(j);
		newValue.set(value + (twister.nextDouble()*2-1)*(value - other));
		newPosition.set(j, newValue);
		
		//Determine if new position is better than old and update
		Fitness oldFitness =  Algorithm.get().getOptimisationProblem().getFitness(oldPosition,false);
		Fitness newFitness =  Algorithm.get().getOptimisationProblem().getFitness(newPosition,false);
		if (newFitness.compareTo(oldFitness) < 0)
		{
			bee.setPosition(oldPosition);
			return false;
		}
		else
			return true;
	}

}
