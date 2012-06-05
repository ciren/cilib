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

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.ec.EC;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.operators.Crossover;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.UniformCrossoverStrategy;
import net.sourceforge.cilib.entity.operators.mutation.GaussianMutationStrategy;
import net.sourceforge.cilib.entity.operators.mutation.MutationStrategy;

/**
 * TODO: Complete this javadoc.
 */
public class GeneticAlgorithmIterationStrategy extends AbstractIterationStrategy<EC> {

    private static final long serialVersionUID = -2429984051022079804L;
    private Crossover crossover;
    private MutationStrategy mutationStrategy;

    /**
     * Create an instance of the {@linkplain IterationStrategy}. Default cross-over
     * and mutation operators are {@linkplain UniformCrossoverStrategy} and
     * {@linkplain GaussianMutationStrategy} respectively.
     */
    public GeneticAlgorithmIterationStrategy() {
        this.crossover = new Crossover();
        this.crossover.setCrossoverStrategy(new UniformCrossoverStrategy());
        this.mutationStrategy = new GaussianMutationStrategy();
    }

    /**
     * Copy constructor. Create an instance that is a copy of the provided instance.
     * @param copy the instance to copy.
     */
    public GeneticAlgorithmIterationStrategy(GeneticAlgorithmIterationStrategy copy) {
        this.crossover = copy.crossover.getClone();
        this.mutationStrategy = copy.mutationStrategy.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GeneticAlgorithmIterationStrategy getClone() {
        return new GeneticAlgorithmIterationStrategy(this);
    }

    /**
     * {@inheritDoc}
     *
     * @param ec The EC algorithm to perform the iteration on.
     */
    @Override
    public void performIteration(EC ec) {
        Topology<Entity> population = (Topology<Entity>) ec.getTopology();

        // Perform crossover: Allow each individual to create an offspring
        List<Entity> crossedOver = Lists.newArrayList();
        for (int i = 0, n = population.size(); i < n; i++) {
            crossedOver.addAll(this.crossover.crossover((List<Entity>) ec.getTopology()));
        }

        // Perform mutation on offspring
        this.mutationStrategy.mutate(crossedOver);

        // Evaluate the fitness values of the generated offspring
        for (Entity entity : crossedOver) {
            boundaryConstraint.enforce(entity);
            entity.calculateFitness();
        }

        // Perform new population selection
        Topology<Entity> topology = (Topology<Entity>) ec.getTopology();
        for (Entity entity : crossedOver) {
            topology.add(entity);
        }

        Collections.sort(ec.getTopology());
        ListIterator<? extends Entity> i = ec.getTopology().listIterator();

        int count = 0;
        int size = ec.getTopology().size() - ec.getInitialisationStrategy().getEntityNumber();

        while (i.hasNext() && count < size) {
            i.next();
            i.remove();
            count++;
        }
    }

    /**
     * Get the currently specified {@linkplain CrossoverStrategy}.
     * @return The current {@linkplain CrossoverStrategy}.
     */
    public Crossover getCrossover() {
        return crossover;
    }

    /**
     * Set the current {@linkplain CrossoverStrategy} and reinitialise the operator pipeline.
     * @param crossoverStrategy The {@linkplain CrossoverStrategy} to use.
     */
    public void setCrossover(Crossover crossoverStrategy) {
        this.crossover = crossoverStrategy;
    }

    /**
     * Get the currently specified {@linkplain MutationStrategy}.
     * @return The current {@linkplain MutationStrategy}.
     */
    public MutationStrategy getMutationStrategy() {
        return mutationStrategy;
    }

    /**
     * Set the current {@linkplain MutationStrategy} and reinitialise the operator pipeline.
     * @param mutationStrategy The {@linkplain MutationStrategy} to use.
     */
    public void setMutationStrategy(MutationStrategy mutationStrategy) {
        this.mutationStrategy = mutationStrategy;
    }
}
