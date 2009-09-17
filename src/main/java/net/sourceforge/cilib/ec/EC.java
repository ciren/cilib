/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.ec;

import com.google.common.collect.Iterables;
import java.util.List;

import net.sourceforge.cilib.algorithm.initialisation.ClonedPopulationInitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.cooperative.ParticipatingAlgorithm;
import net.sourceforge.cilib.ec.iterationstrategies.GeneticAlgorithmIterationStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.initialization.InitializationStrategy;
import net.sourceforge.cilib.entity.initialization.NullInitializationStrategy;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;

/**
 * Generic EC skeleton algorithm. The algorithm is altered by defining the
 * appropriate {@linkplain net.sourceforge.cilib.algorithm.population.IterationStrategy}.
 *
 * @author Gary Pampara
 */
public class EC extends SinglePopulationBasedAlgorithm implements ParticipatingAlgorithm {
    private static final long serialVersionUID = -4324446523858690744L;

    private OptimisationProblem problem;
    private IterationStrategy<EC> iterationStrategy;
    private Topology<Individual> topology;

    private InitializationStrategy<Entity> strategyParameterInitialization;

    /**
     * Create a new instance of {@code EC}.
     */
    public EC() {
        this.initialisationStrategy = new ClonedPopulationInitialisationStrategy();
        this.initialisationStrategy.setEntityType(new Individual());

        this.iterationStrategy = new GeneticAlgorithmIterationStrategy();
        this.topology = new GBestTopology<Individual>();

        this.strategyParameterInitialization = new NullInitializationStrategy<Entity>();
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
    public void performInitialisation() {
        Iterable<? extends Entity> individuals = this.initialisationStrategy.initialise(this.problem);
        //Iterables.addAll(getTopology(), particles); // Use this instead?
        for (Entity individual : individuals)
            topology.add((Individual) individual);
//        Iterables.addAll(topology, individuals);

        for (Entity entity : topology) {
            this.strategyParameterInitialization.initialize(EntityType.STRATEGY_PARAMETERS, entity);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void algorithmIteration() {
        for (Entity entity : this.getTopology()) {
            entity.calculateFitness();
        }

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
    public void setOptimisationProblem(OptimisationProblem problem) {
        this.problem = problem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OptimisationProblem getOptimisationProblem() {
        return this.problem;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OptimisationSolution getBestSolution() {
        Entity bestEntity = topology.getBestEntity();
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
    public List<OptimisationSolution> getSolutions() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Entity getContribution() {
        //TODO: This might not be what you want, change as desired
        return this.topology.getBestEntity();
    }

    /**
     * {@inheritDoc}
     */
    public Fitness getContributionFitness() {
        //TODO: This might not be what you want, change as desired
        return this.topology.getBestEntity().getFitness();
    }

    /**
     * {@inheritDoc}
     */
    public void updateContributionFitness(Fitness fitness) {
        //TODO: This might not be what you want, change as desired
        //getBestEntity().setFitness(fitness);
        this.topology.getBestEntity().calculateFitness();
    }

    public InitializationStrategy<Entity> getStrategyParameterInitialization() {
        return strategyParameterInitialization;
    }

    public void setStrategyParameterInitialization(InitializationStrategy<Entity> strategyParameterInitialization) {
        this.strategyParameterInitialization = strategyParameterInitialization;
    }

}
