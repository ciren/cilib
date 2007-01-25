package net.sourceforge.cilib.controlparameterupdatestrategies;

public class ProportionalControlParameterUpdateStrategy implements
		ControlParameterUpdateStrategy {
	
	private double proportion;
	
	public ProportionalControlParameterUpdateStrategy() {
		this.proportion = 0.1;
	}
	
	public ProportionalControlParameterUpdateStrategy clone() {
		return null;
	}

	public double getParameter() {
		return this.proportion;
	}

	public double getParameter(double min, double max) {
		double diff = max - min;
		return this.proportion * diff;
	}

	public void setParameter(double value) {
		if (value < 0)
			throw new IllegalArgumentException("The proportion must be positive");
		
		this.proportion = value;
	}

	public void updateParameter() {

	}

}
