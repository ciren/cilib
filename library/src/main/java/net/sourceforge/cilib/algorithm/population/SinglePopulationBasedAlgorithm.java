/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.algorithm.population;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.initialisation.PopulationInitialisationStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.visitor.TopologyVisitor;

/**
 * Base class for algorithms that focus on a single populations of entities.
 * These types of algoruthms typically include PSO , EC, ACO etc.
 */
public abstract class SinglePopulationBasedAlgorithm extends AbstractAlgorithm implements PopulationBasedAlgorithm {
    private static final long serialVersionUID = -4095104893057340895L;

    protected PopulationInitialisationStrategy<? extends Entity> initialisationStrategy;

    /**
     * Create an empty {@linkplain PopulationBasedAlgorithm}.
     */
    protected SinglePopulationBasedAlgorithm() {
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The {@linkplain PopulationBasedAlgorithm} to copy.
     */
    protected SinglePopulationBasedAlgorithm(SinglePopulationBasedAlgorithm copy) {
        super(copy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract SinglePopulationBasedAlgorithm getClone();

    /**
     * Perform the iteration within the algorithm.
     */
    @Override
    protected abstract void algorithmIteration();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract Topology<? extends Entity> getTopology();

    /**
     * Set the <tt>Topology</tt> for the population-based algorithm.
     * @param topology The {@linkplain Topology} to be set.
     */
    public abstract void setTopology(Topology<? extends Entity> topology);

    /**
     * Get the currently set {@linkplain PopulationInitialisationStrategy}.
     * @return The current {@linkplain PopulationInitialisationStrategy}.
     */
    @Override
    public PopulationInitialisationStrategy getInitialisationStrategy() {
        return initialisationStrategy;
    }

    /**
     * Set the {@linkplain PopulationInitialisationStrategy} to be used.
     * @param initialisationStrategy The {@linkplain PopulationInitialisationStrategy} to use.
     */
    @Override
    public void setInitialisationStrategy(PopulationInitialisationStrategy<? extends Entity> initialisationStrategy) {
        this.initialisationStrategy = initialisationStrategy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object accept(TopologyVisitor visitor) {
        getTopology().accept(visitor);
        return visitor.getResult();
    }

}
