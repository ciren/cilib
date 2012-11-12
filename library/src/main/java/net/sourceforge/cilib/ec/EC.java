/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ec;

import com.google.common.collect.Lists;
import java.util.List;
import net.sourceforge.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.ParticipatingAlgorithm;
import net.sourceforge.cilib.coevolution.cooperative.contributionselection.ContributionSelectionStrategy;
import net.sourceforge.cilib.coevolution.cooperative.contributionselection.ZeroContributionSelectionStrategy;
import net.sourceforge.cilib.ec.iterationstrategies.GeneticAlgorithmIterationStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.initialization.InitializationStrategy;
import net.sourceforge.cilib.entity.initialization.NullInitializationStrategy;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;

/**
 * Generic EC skeleton algorithm. The algorithm is altered by defining the
 * appropriate {@linkplain net.sourceforge.cilib.algorithm.population.IterationStrategy}.
 *
 */
public class EC extends SinglePopulationBasedAlgorithm implements ParticipatingAlgorithm {

    private static final long serialVersionUID = -4324446523858690744L;
    private Problem problem;
    private IterationStrategy<EC> iterationStrategy;
    private Topology<Individual> topology;
    private InitializationStrategy<Entity> strategyParameterInitialization;
    private ContributionSelectionStrategy contributionSelection;

    /**
     * Create a new instance of {@code EC}.
     */
    public EC() {
        this.initialisationStrategy = new ClonedPopulationInitialisationStrategy();
        this.initialisationStrategy.setEntityType(new Individual());
        this.iterationStrategy = new GeneticAlgorithmIterationStrategy();
        this.topology = new GBestTopology<Individual>();
        this.strategyParameterInitialization = new NullInitializationStrategy<Entity>();
        this.contributionSelection = new ZeroContributionSelectionStrategy();
    }

    /**
     * Copy constructor. Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public EC(EC copy) {
        super(copy);
        this.initialisationStrategy = copy.initialisationStrategy.getClone();
        this.iterationStrategy = copy.iterationStrategy.getClone();
        this.topology = copy.topology.getClone();
        this.strategyParameterInitialization = copy.strategyParameterInitialization.getClone();
        this.contributionSelection = copy.contributionSelection.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EC getClone() {
        return new EC(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void algorithmInitialisation() {
        Iterable<? extends Entity> individuals = this.initialisationStrategy.initialise(this.problem);
        //Iterables.addAll(getTopology(), particles); // Use this instead?
        for (Entity individual : individuals) {
            topology.add((Individual) individual);
        }

        for (Entity entity : topology) {
            this.strategyParameterInitialization.initialize(EntityType.STRATEGY_PARAMETERS, entity);
        }

        for (Entity e : getTopology()) {
            e.calculateFitness();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void algorithmIteration() {
        iterationStrategy.performIteration(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Topology<? extends Entity> getTopology() {
        return this.topology;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTopology(Topology topology) {
        this.topology = topology;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOptimisationProblem(Problem problem) {
        this.problem = problem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Problem getOptimisationProblem() {
        return this.problem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OptimisationSolution getBestSolution() {
        Entity bestEntity = Topologies.getBestEntity(topology);
        OptimisationSolution solution = new OptimisationSolution(bestEntity.getCandidateSolution().getClone(), bestEntity.getFitness());

        return solution;
    }

    /**
     * Get the {@linkplain net.sourceforge.cilib.algorithm.population.IterationStrategy} for the current
     * {@code EC}.
     * @return The current {@linkplain net.sourceforge.cilib.algorithm.population.IterationStrategy}.
     */
    public IterationStrategy<EC> getIterationStrategy() {
        return iterationStrategy;
    }

    /**
     * Set the current {@linkplain net.sourceforge.cilib.algorithm.population.IterationStrategy}.
     * @param iterationStrategy The value to set.
     */
    public void setIterationStrategy(IterationStrategy iterationStrategy) {
        this.iterationStrategy = iterationStrategy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OptimisationSolution> getSolutions() {
        return Lists.newArrayList(getBestSolution());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContributionSelectionStrategy getContributionSelectionStrategy() {
        return contributionSelection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setContributionSelectionStrategy(ContributionSelectionStrategy strategy) {
        contributionSelection = strategy;
    }

    public InitializationStrategy<Entity> getStrategyParameterInitialization() {
        return strategyParameterInitialization;
    }

    public void setStrategyParameterInitialization(InitializationStrategy<Entity> strategyParameterInitialization) {
        this.strategyParameterInitialization = strategyParameterInitialization;
    }
}
