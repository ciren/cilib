package net.sourceforge.cilib.entity;

import java.io.Serializable;

import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.util.Cloneable;

/**
 * Interface to describe the notion of a <code>CandidateSoution</code>. In general
 * a <code>CandidateSolution</code> contains a {@linkplain Type} to describe the
 * solution the <code>CandidateSolution</code> represents together with its
 * associated {@linkplain Fitness} value.
 */
public interface CandidateSolution extends Serializable, Cloneable {
	
	/**
	 * Get the contents of the <code>CandidateSoltion</code>. i.e.: The
	 * potential solution.
	 * @return A {@linkplain Type} representing the solution.
	 */
	public Type getContents();
	
	/**
	 * Set the solution that the <code>CandidateSolution</code> represents.
	 * @param contents The potential solution to set.
	 */
	public void setContents(Type contents);
	
	/**
	 * Obtain the {@linkplain Fitness} of the current <code>CandidateSolution</code>.
	 * @return The current {@linkplain Fitness} value.
	 */
	public Fitness getFitness();

}
