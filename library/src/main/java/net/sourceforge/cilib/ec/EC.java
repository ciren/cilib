/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.ec;

import java.util.List;

import net.sourceforge.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.ec.iterationstrategies.GeneticAlgorithmIterationStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Property;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.initialisation.InitialisationStrategy;
import net.sourceforge.cilib.entity.initialisation.NullInitialisationStrategy;
import net.sourceforge.cilib.problem.solution.OptimisationSolution;

import com.google.common.collect.Lists;

import fj.F;

/**
 * Generic EC skeleton algorithm. The algorithm is altered by defining the
 * appropriate {@linkplain net.sourceforge.cilib.algorithm.population.IterationStrategy}.
 */
public class EC<I extends Individual> extends SinglePopulationBasedAlgorithm<Individual> {

    private static final long serialVersionUID = -4324446523858690744L;

    private IterationStrategy iterationStrategy;
    private InitialisationStrategy strategyParameterInitialisation;

    /**
     * Create a new instance of {@code EC}.
     */
    public EC() {
        this.initialisationStrategy = new ClonedPopulationInitialisationStrategy();
        this.initialisationStrategy.setEntityType(new Individual());
        this.iterationStrategy = new GeneticAlgorithmIterationStrategy();
        this.strategyParameterInitialisation = new NullInitialisationStrategy();
    }

    /**
     * Copy constructor. Create a copy of the provided instance.
     * @param copy The instance to copy.
     */
    public EC(EC copy) {
        super(copy);
        this.iterationStrategy = copy.iterationStrategy.getClone();
        this.strategyParameterInitialisation = copy.strategyParameterInitialisation.getClone();
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
        topology = fj.data.List.iterableList(initialisationStrategy.<Individual>initialise(optimisationProblem))
                .map(new F<Individual, Individual>() {
                    @Override
                    public Individual f(Individual i) {
                        i.calculateFitness();
                        strategyParameterInitialisation.initialise(Property.STRATEGY_PARAMETERS, i);
                        return i;
                    }
                });
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
    public IterationStrategy getIterationStrategy() {
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

    public InitialisationStrategy<Individual> getStrategyParameterInitialisation() {
        return strategyParameterInitialisation;
    }

    public void setStrategyParameterInitialisation(InitialisationStrategy<Individual> strategyParameterInitialisation) {
        this.strategyParameterInitialisation = strategyParameterInitialisation;
    }
}
