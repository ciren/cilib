package net.sourceforge.cilib.pso.dynamic.responsestrategies;

import net.sourceforge.cilib.pso.PSO;

public interface EnvironmentChangeResponseStrategy {
	/**
	 * Clone the <tt>EnvironmentChangeResponseStrategy</tt> object.
	 * @return A cloned <tt>EnvironmentChangeResponseStrategy</tt>
	 */
	public EnvironmentChangeResponseStrategy clone();
	
	/**
	 * Adapt to environment change.
	 * @param algorithm The <tt>PSO</tt> that runs in a dynamic environment.
	 */
	public void respondToChange(PSO algorithm);
}
