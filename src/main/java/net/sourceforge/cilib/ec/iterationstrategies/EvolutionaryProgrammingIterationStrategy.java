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
package net.sourceforge.cilib.ec.iterationstrategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.container.Pair;
import net.sourceforge.cilib.ec.EC;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.operators.mutation.GaussianMutationStrategy;
import net.sourceforge.cilib.entity.operators.mutation.MutationStrategy;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;
import net.sourceforge.cilib.util.selection.Selection;

/**
 *
 */
public class EvolutionaryProgrammingIterationStrategy extends AbstractIterationStrategy<EC> {
    private static final long serialVersionUID = 4966470754016818350L;

    private MutationStrategy mutationStrategy;

    public EvolutionaryProgrammingIterationStrategy() {
        this.mutationStrategy = new GaussianMutationStrategy();
    }

    private EvolutionaryProgrammingIterationStrategy(EvolutionaryProgrammingIterationStrategy copy) {
        this.mutationStrategy = copy.mutationStrategy.getClone();
    }

    @Override
    public EvolutionaryProgrammingIterationStrategy getClone() {
        return new EvolutionaryProgrammingIterationStrategy(this);
    }

    @Override
    public void performIteration(EC algorithm) {
        Topology<Individual> topology = (Topology<Individual>) algorithm.getTopology();
        List<Individual> offspring = new ArrayList<Individual>();

        for (Individual individual : topology) {
            offspring.add(individual.getClone()); // Create an offspring by cloning the parent.
        }

        // Apply the mutation
        this.mutationStrategy.mutate(offspring);

        for (Individual individual : offspring)
            individual.calculateFitness();

        topology.addAll(offspring);

        List<Individual> newPopulation = new ArrayList<Individual>(algorithm.getInitialisationStrategy().getEntityNumber());

        List<Pair<Individual, Integer>> scores = new ArrayList<Pair<Individual, Integer>>();
        for (int i = 0; i < topology.size(); i++) {
            Individual current = topology.get(i);
            int score = getScore(current, topology);
            scores.add(new Pair<Individual, Integer>(current, score));
        }

        Collections.sort(scores, new Comparator<Pair<Individual, Integer>>() {
            @Override
            public int compare(Pair<Individual, Integer> o1, Pair<Individual, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        for (int i = 0; i < algorithm.getInitialisationStrategy().getEntityNumber(); i++) {
            newPopulation.add(scores.get(i).getKey());
        }

        topology.clear();
        topology.addAll(newPopulation);
    }

    private int getScore(Individual current, Topology<Individual> topology) {
        int score = 0;
        List<Individual> selection = Selection.from(topology).unique().random(new MersenneTwister(), 10).select();

        for (Individual i : selection)
            if (current.getFitness().compareTo(i.getFitness()) < 0)
                score++;

        return score;
    }

    public MutationStrategy getMutationStrategy() {
        return mutationStrategy;
    }

    public void setMutationStrategy(MutationStrategy mutationStrategy) {
        this.mutationStrategy = mutationStrategy;
    }

}
