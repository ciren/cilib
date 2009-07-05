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
import java.util.List;
import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.ec.EC;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.operators.mutation.GaussianMutationStrategy;
import net.sourceforge.cilib.entity.operators.mutation.MutationStrategy;
import net.sourceforge.cilib.entity.operators.selection.SelectionStrategy;
import net.sourceforge.cilib.entity.operators.selection.TournamentSelectionStrategy;

/**
 *
 */
public class EvolutionaryProgrammingIterationStrategy extends AbstractIterationStrategy<EC> {
    private static final long serialVersionUID = 4966470754016818350L;

    private MutationStrategy mutationStrategy;
    private SelectionStrategy selectionStrategy;

    public EvolutionaryProgrammingIterationStrategy() {
        this.mutationStrategy = new GaussianMutationStrategy();
        this.selectionStrategy = new TournamentSelectionStrategy();
    }

    @Override
    public AbstractIterationStrategy<EC> getClone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void performIteration(EC algorithm) {
        Topology<Individual> topology = (Topology<Individual>) algorithm.getTopology();
        List<Individual> offspring = new ArrayList<Individual>();

        for (Individual individual : topology) {
            // Create an offspring by cloning the parent.
            offspring.add(individual.getClone());
        }

        // Apply the mutation
        this.mutationStrategy.mutate(offspring);

        for (Individual individual : offspring)
            individual.calculateFitness();

        topology.addAll(offspring);

        List<Individual> newPopulation = new ArrayList<Individual>(algorithm.getInitialisationStrategy().getEntityNumber());
        for (int i = 0; i < algorithm.getInitialisationStrategy().getEntityNumber(); i++) {
            Individual individual = this.selectionStrategy.select(topology);
            newPopulation.add(individual);
        }

        topology.clear();
        topology.addAll(newPopulation);
    }

}
