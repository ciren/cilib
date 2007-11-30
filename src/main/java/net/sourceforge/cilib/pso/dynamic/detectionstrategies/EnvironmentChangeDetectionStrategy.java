package net.sourceforge.cilib.pso.dynamic.detectionstrategies;

import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.util.Cloneable;

public interface EnvironmentChangeDetectionStrategy extends Cloneable {
	/**
	 * Clone the <tt>EnvironmentChangeDetectionStrategy</tt> object.
	 * @return A cloned <tt>EnvironmentChangeDetectionStrategy</tt>
	 */
	public EnvironmentChangeDetectionStrategy getClone();
	
	/**
	 * Check the environment in which the specified PSO algorithm is running for changes.
	 * @param algorithm The <tt>PSO</tt> that runs in a dynamic environment.
	 * @return true if any changes are detected, false otherwise
	 */
	public boolean detectChange(PSO algorithm);
}
