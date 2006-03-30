package net.sourceforge.cilib.entity.operators.mutation;

import net.sourceforge.cilib.controlparameterupdatestrategies.ControlParameterUpdateStrategy;
import net.sourceforge.cilib.controlparameterupdatestrategies.ProportionalControlParameterUpdateStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Vector;

/**

* @author  Andries Engelbrecht
*/

public class GaussianMutationStrategy extends MutationStrategy {

	private double mean;
	
	private String operatorType;
	private ControlParameterUpdateStrategy deviationStrategy;
	
	public GaussianMutationStrategy() {
		this.mean = 0;
		
		deviationStrategy = new ProportionalControlParameterUpdateStrategy();
		
		this.operatorType = "+";
	}
	
	@Override
	public void mutate(Entity entity) {
		Vector chromosome = (Vector) entity.get();
		
		if (this.getMutationProbability().getParameter() >= this.getRandomNumber().getUniform()) {
			for (int i = 0; i < chromosome.getDimension(); i++) {
				double value;
				Numeric element = (Numeric) chromosome.get(i);
				double deviation = this.deviationStrategy.getParameter(element.getLowerBound(), element.getUpperBound());
				
				if (operatorType.equals("+")) {	
					value = chromosome.getReal(i) + this.getRandomNumber().getGaussian(mean,deviation);
				}
				else
					value = chromosome.getReal(i) * this.getRandomNumber().getGaussian(this.mean, deviation);
				
				chromosome.setReal(i, value);
			}
		}

	}

}
