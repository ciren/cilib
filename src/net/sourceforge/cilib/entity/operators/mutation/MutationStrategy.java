package net.sourceforge.cilib.entity.operators.mutation;

import net.sourceforge.cilib.controlparameterupdatestrategies.ControlParameterUpdateStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.RandomNumber;

public abstract class MutationStrategy {
	
	private ControlParameterUpdateStrategy mutationProbability;
	private RandomNumber randomNumber;
	
	public abstract void mutate(Entity entity);

	
	/**
	 * 
	 * @return
	 */
	public ControlParameterUpdateStrategy getMutationProbability() {
		return mutationProbability;
	}

	/**
	 * 
	 * @param mutationProbability
	 */
	public void setMutationProbability(
			ControlParameterUpdateStrategy mutationProbability) {
		this.mutationProbability = mutationProbability;
	}


	public RandomNumber getRandomNumber() {
		return randomNumber;
	}


	public void setRandomNumber(RandomNumber randomNumber) {
		this.randomNumber = randomNumber;
	}
	
	
	
}
