package net.sourceforge.cilib.entity.operators.mutation;

import net.sourceforge.cilib.controlparameterupdatestrategies.ControlParameterUpdateStrategy;
import net.sourceforge.cilib.controlparameterupdatestrategies.ProportionalControlParameterUpdateStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Vector;

/**

* @author  Andries Engelbrecht
*/

public class UniformMutationStrategy extends MutationStrategy {

	private String operatorType;
	private ControlParameterUpdateStrategy minStrategy, maxStrategy;
	
	public UniformMutationStrategy() {
		minStrategy = new ProportionalControlParameterUpdateStrategy();
		maxStrategy = new ProportionalControlParameterUpdateStrategy();
		
		this.operatorType = "+";
	}
	@Override
	public void mutate(Entity entity) {
		Vector chromosome = (Vector) entity.get();
		
		if (this.getMutationProbability().getParameter() >= this.getRandomNumber().getUniform()) {
			for (int i = 0; i < chromosome.getDimension(); i++) {
				double value;
				Numeric element = (Numeric) chromosome.get(i);
				double min = element.getLowerBound();
				double max = element.getUpperBound();
				
				if (operatorType.equals("+")) {	
					value = chromosome.getReal(i) + this.getRandomNumber().getUniform(min,max);
				}
				else
					value = chromosome.getReal(i) * this.getRandomNumber().getUniform(min,max);
				
				chromosome.setReal(i, value);
			}
		}

	}

}
