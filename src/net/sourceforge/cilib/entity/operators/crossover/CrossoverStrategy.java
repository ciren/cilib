package net.sourceforge.cilib.entity.operators.crossover;

import net.sourceforge.cilib.controlparameterupdatestrategies.ControlParameterUpdateStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.RandomNumber;

/**

* @author  Andries Engelbrecht
*/

public abstract class CrossoverStrategy {
	private ControlParameterUpdateStrategy crossoverProbability;
	private RandomNumber randomNumber;
	
	public abstract void crossover(Entity parent1, Entity parent2);

	
	/**
	 * 
	 * @return
	 */
	public ControlParameterUpdateStrategy getCrossoverProbability() {
		return crossoverProbability;
	}

	/**
	 * 
	 * @param crossoverProbability
	 */
	public void setCrossoverProbability(
			ControlParameterUpdateStrategy crossoverProbability) {
		this.crossoverProbability = crossoverProbability;
	}


	public RandomNumber getRandomNumber() {
		return randomNumber;
	}


	public void setRandomNumber(RandomNumber randomNumber) {
		this.randomNumber = randomNumber;
	}
	
}
