package net.sourceforge.cilib.algorithm.population;

import java.io.Serializable;

import net.sourceforge.cilib.util.Cloneable;

public interface IterationStrategy<E extends PopulationBasedAlgorithm> extends Cloneable, Serializable {
	
	/**
	 * {@inheritDoc}
	 */
	public abstract IterationStrategy<E> getClone();
	
	/**
	 * Perform the iteration of the PopulationBasedAlgorithm.
	 * <p>
	 * Due to the nature of the PopulationBasedAlgorithms, the actual manner in which the algorithm's
	 * iteration is performed is deferred to the specific iteration strategy being used.
	 * <p>
	 * This implies that the general structure of the iteration for a specific flavour of
	 * algorithm is constant with modifications on that algorithm being made. For example,
	 * within a Genetic Algorithm you would expect:
	 * <ol>
	 *   <li>Parent individuals to be <b>selected</b> in some manner</li>
	 *   <li>A <b>crossover</b> process to be done on the selected parent individuals to create
	 *       the offspring</li>
	 *   <li>A <b>mutation</b> process to alter the generated offspring</li>
	 *   <li><b>Recombine</b> the existing parent individuals and the generated offspring to create
	 *       the next generation</li>
	 * </ol>
	 *       
	 * @param algorithm The algorithm to perform the iteration process on.
	 */
	public abstract void performIteration(E algorithm);

}