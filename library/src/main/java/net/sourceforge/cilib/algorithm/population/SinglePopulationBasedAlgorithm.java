/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.algorithm.population;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.initialisation.PopulationInitialisationStrategy;
import net.sourceforge.cilib.coevolution.cooperative.ParticipatingAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.contributionselection.ContributionSelectionStrategy;
import net.sourceforge.cilib.coevolution.cooperative.contributionselection.ZeroContributionSelectionStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.entity.visitor.TopologyVisitor;

/**
 * Base class for algorithms that focus on a single populations of entities.
 * These types of algorithms typically include PSO , EC, ACO etc.
 */
public abstract class SinglePopulationBasedAlgorithm<E extends Entity> extends AbstractAlgorithm implements PopulationBasedAlgorithm, ParticipatingAlgorithm {

    private static final long serialVersionUID = -4095104893057340895L;

    protected PopulationInitialisationStrategy<E> initialisationStrategy;
    protected Topology<E> topology;
    protected ContributionSelectionStrategy contributionSelection;

    /**
     * Create an empty {@linkplain PopulationBasedAlgorithm}.
     */
    protected SinglePopulationBasedAlgorithm() {
        this.topology = new GBestTopology();
        this.contributionSelection = new ZeroContributionSelectionStrategy();
    }

    /**
     * Create a copy of the provided instance.
     * @param copy The {@linkplain PopulationBasedAlgorithm} to copy.
     */
    protected SinglePopulationBasedAlgorithm(SinglePopulationBasedAlgorithm copy) {
        super(copy);
        this.initialisationStrategy = copy.initialisationStrategy.getClone();
        this.topology = copy.topology.getClone();
        this.contributionSelection = copy.contributionSelection.getClone();
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
    public Topology<E> getTopology() {
        return topology;
    }

    /**
     * Set the <tt>Topology</tt> for the population-based algorithm.
     * @param topology The {@linkplain Topology} to be set.
     */
    public void setTopology(Topology topology) {
        this.topology = topology;
    }

    /**
     * Get the currently set {@linkplain PopulationInitialisationStrategy}.
     * @return The current {@linkplain PopulationInitialisationStrategy}.
     */
    @Override
    public PopulationInitialisationStrategy<E> getInitialisationStrategy() {
        return initialisationStrategy;
    }

    /**
     * Set the {@linkplain PopulationInitialisationStrategy} to be used.
     * @param initialisationStrategy The {@linkplain PopulationInitialisationStrategy} to use.
     */
    @Override
    public void setInitialisationStrategy(PopulationInitialisationStrategy initialisationStrategy) {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void setContributionSelectionStrategy(ContributionSelectionStrategy strategy) {
        this.contributionSelection = strategy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContributionSelectionStrategy getContributionSelectionStrategy() {
        return contributionSelection;
    }

}
