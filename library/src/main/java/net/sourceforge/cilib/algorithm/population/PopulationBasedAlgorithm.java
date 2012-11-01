/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.algorithm.population;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.Stoppable;
import net.sourceforge.cilib.algorithm.initialisation.PopulationInitialisationStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.visitor.TopologyVisitor;

/**
 * Base class for all algorithms that manage a collection of
 * {@linkplain Entity entities} in some manner.
 */
public interface PopulationBasedAlgorithm extends Algorithm, Stoppable {

    @Override
    PopulationBasedAlgorithm getClone();

    /**
     * Get the current collection (population) of {@linkplain Entity entities}.
     * @return The current population.
     */
    abstract Topology<? extends Entity> getTopology();

    /**
     * General method to accept a visitor to perform a calculation on the current algorithm. The
     * operation is generally deferred down to the underlying topology associated with the
     * algorithm, as the algorithm does not contain information, but rather only behaviour to alter
     * the candidate solutions that are managed by the <tt>Topology</tt>.
     * @param visitor The <tt>Visitor</tt> to be applied to the algorithm
     * @return The result of the visitor operation.
     */
    abstract Object accept(TopologyVisitor visitor);

    /**
     * Set the initialisation strategy to use for the initialisation of the population.
     * @param initialisationStrategy The population initialisation strategy to set.
     */
    abstract void setInitialisationStrategy(PopulationInitialisationStrategy<? extends Entity> initialisationStrategy);

    /**
     * Get the current {@code PopulationInitialisationStrategy}.
     * @return The current {@code PopulationInitialisationStrategy}.
     */
    abstract PopulationInitialisationStrategy getInitialisationStrategy();

}
