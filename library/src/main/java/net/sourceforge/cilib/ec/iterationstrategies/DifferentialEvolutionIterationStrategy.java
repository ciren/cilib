/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.ec.iterationstrategies;

import net.sourceforge.cilib.entity.operators.creation.CreationStrategy;
import net.sourceforge.cilib.entity.operators.creation.RandCreationStrategy;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.ec.EC;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.de.DifferentialEvolutionBinomialCrossover;
import net.sourceforge.cilib.util.selection.recipes.RandomSelector;
import net.sourceforge.cilib.util.selection.recipes.Selector;

/**
 * Evolutionary Strategy to implement the Differential Evolutionary Algorithm.
 *
 */
public class DifferentialEvolutionIterationStrategy extends AbstractIterationStrategy<EC> {

    private static final long serialVersionUID = 8019668923312811974L;
    private Selector targetVectorSelectionStrategy; // x
    private CreationStrategy trialVectorCreationStrategy; // y
    private CrossoverStrategy crossoverStrategy; // z

    /**
     * Create an instance of the {@linkplain DifferentialEvolutionIterationStrategy}.
     */
    public DifferentialEvolutionIterationStrategy() {
        this.targetVectorSelectionStrategy = new RandomSelector();
        this.trialVectorCreationStrategy = new RandCreationStrategy();
        this.crossoverStrategy = new DifferentialEvolutionBinomialCrossover();
    }

    /**
     * Copy constructor. Create a copy of the given instance.
     * @param copy The instance to copy.
     */
    public DifferentialEvolutionIterationStrategy(DifferentialEvolutionIterationStrategy copy) {
        this.targetVectorSelectionStrategy = copy.targetVectorSelectionStrategy;
        this.trialVectorCreationStrategy = copy.trialVectorCreationStrategy.getClone();
        this.crossoverStrategy = copy.crossoverStrategy.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DifferentialEvolutionIterationStrategy getClone() {
        return new DifferentialEvolutionIterationStrategy(this);
    }

    /**
     * Perform an iteration of the DE algorithm defined as the DE/x/y/z implementation.
     * @param ec The {@linkplain EC} on which to perform this iteration.
     */
    @Override
    public void performIteration(EC ec) {
        @SuppressWarnings("unchecked")
        Topology<Entity> topology = (Topology<Entity>) ec.getTopology();

        for (int i = 0; i < topology.size(); i++) {
            Entity current = topology.get(i);
            current.calculateFitness();

            // Create the trial vector by applying mutation
            Entity targetEntity = (Entity) targetVectorSelectionStrategy.on(topology).exclude(current).select();

            // Create the trial vector / entity
            Entity trialEntity = trialVectorCreationStrategy.create(targetEntity, current, topology);

            // Create the offspring by applying cross-over
            List<Entity> offspring = (List<Entity>) this.crossoverStrategy.crossover(Arrays.asList(current, trialEntity)); // Order is VERY important here!!

            // Replace the parent (current) if the offspring is better
            Entity offspringEntity = offspring.get(0);
            boundaryConstraint.enforce(offspringEntity);
            offspringEntity.calculateFitness();

            if (offspringEntity.getFitness().compareTo(current.getFitness()) > 0) { // the trial vector is better than the parent
                topology.set(i, offspringEntity); // Replace the parent with the offspring individual
            }
        }
    }

    /**
     * Obtain the {@linkplain SelectionStrategy} used to select the target vector.
     * @return The {@linkplain SelectionStrategy} of the target vector.
     */
    public Selector getTargetVectorSelectionStrategy() {
        return targetVectorSelectionStrategy;
    }

    /**
     * Set the {@linkplain SelectionStrategy} used to select the target vector within the DE.
     * @param targetVectorSelectionStrategy The {@linkplain SelectionStrategy} to use for the
     *        selection of the target vector.
     */
    public void setTargetVectorSelectionStrategy(Selector targetVectorSelectionStrategy) {
        this.targetVectorSelectionStrategy = targetVectorSelectionStrategy;
    }

    /**
     * Get the {@linkplain CrossoverStrategy} used to create offspring entities.
     * @return The {@linkplain CrossoverStrategy} used to create offspring.
     */
    public CrossoverStrategy getCrossoverStrategy() {
        return crossoverStrategy;
    }

    /**
     * Set the {@linkplain CrossoverStrategy} used to create offspring entities.
     * @param crossoverStrategy The {@linkplain CrossoverStrategy} to create entities.
     */
    public void setCrossoverStrategy(CrossoverStrategy crossoverStrategy) {
        this.crossoverStrategy = crossoverStrategy;
    }

    /**
     * Get the current strategy for creation of the trial vector.
     * @return The {@linkplain CreationStrategy}.
     */
    public CreationStrategy getTrialVectorCreationStrategy() {
        return trialVectorCreationStrategy;
    }

    /**
     * Set the strategy to create trial vectors.
     * @param trialVectorCreationStrategy The value to set.
     */
    public void setTrialVectorCreationStrategy(CreationStrategy trialVectorCreationStrategy) {
        this.trialVectorCreationStrategy = trialVectorCreationStrategy;
    }
}
