package net.sourceforge.cilib.pso.dynamic.responsestrategies;

import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.util.Cloneable;

public interface EnvironmentChangeResponseStrategy extends Cloneable {
	/**
	 * Clone the <tt>EnvironmentChangeResponseStrategy</tt> object.
	 * @return A cloned <tt>EnvironmentChangeResponseStrategy</tt>
	 */
	public EnvironmentChangeResponseStrategy getClone();
	
	/**
	 * Adapt to environment change.
	 * @param algorithm The <tt>PSO</tt> that runs in a dynamic environment.
	 */
	public void respondToChange(PSO algorithm);
}
