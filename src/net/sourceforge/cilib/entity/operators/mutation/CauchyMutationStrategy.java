package net.sourceforge.cilib.entity.operators.mutation;

import net.sourceforge.cilib.controlparameterupdatestrategies.ControlParameterUpdateStrategy;
import net.sourceforge.cilib.controlparameterupdatestrategies.ProportionalControlParameterUpdateStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Vector;

public class CauchyMutationStrategy extends MutationStrategy {
	
	private double location;
	
	private String operatorType;
	private ControlParameterUpdateStrategy scaleStrategy;
	
	public CauchyMutationStrategy() {
		this.location = 0;
		
		scaleStrategy = new ProportionalControlParameterUpdateStrategy();
		
		this.operatorType = "+";
	}

	/**
	 * TODO: add comment :) Will wants this one
	 * TODO: Visitor idea for the selection of the correct scheme
	 */
	@Override
	public void mutate(Entity entity) {

		Vector chromosome = (Vector) entity.get();
		
		if (this.getMutationProbability().getParameter() >= this.getRandomNumber().getUniform()) {
			for (int i = 0; i < chromosome.getDimension(); i++) {
				double value;
				Numeric element = (Numeric) chromosome.get(i);
				double scale = this.scaleStrategy.getParameter(element.getLowerBound(), element.getUpperBound());
				
				if (operatorType.equals("+")) {	
					value = chromosome.getReal(i) + this.getRandomNumber().getCauchy(this.location, scale);
				}
				else
					value = chromosome.getReal(i) * this.getRandomNumber().getCauchy(this.location, scale);
				
				chromosome.setReal(i, value);
			}
		}
	}

	
	public double getLocation() {
		return location;
	}

	public void setLocation(double location) {
		this.location = location;
	}

	
	
	public ControlParameterUpdateStrategy getScaleStrategy() {
		return scaleStrategy;
	}

	public void setScaleStrategy(ControlParameterUpdateStrategy scaleStrategy) {
		this.scaleStrategy = scaleStrategy;
	}
	

}
