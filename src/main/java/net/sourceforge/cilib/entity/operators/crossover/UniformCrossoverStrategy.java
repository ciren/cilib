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
package net.sourceforge.cilib.entity.operators.crossover;

import com.google.common.collect.Lists;
import java.util.List;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.selection.Samples;

/**
 *
 */
public class UniformCrossoverStrategy extends CrossoverStrategy {

    private static final long serialVersionUID = 8912494112973025634L;

    public UniformCrossoverStrategy() {
    }

    public UniformCrossoverStrategy(UniformCrossoverStrategy copy) {
        super(copy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniformCrossoverStrategy getClone() {
        return new UniformCrossoverStrategy(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Entity> crossover(final List<Entity> parentCollection) {
        // Create a topology for the purposes of selection.
        // This is a code smell indicating that the crossover
        // API is not well defined.
        Topology<Entity> parentTopology = new GBestTopology<Entity>();
        parentTopology.addAll(parentCollection);
        List<Entity> offspring = Lists.newArrayListWithCapacity(parentCollection.size());

        // Select two parents, based on the selection strategy - this
        // is not 100% solid, needs to be looked at.
        List<Entity> selectedParents = Lists.newArrayList();
        selectedParents.addAll(getSelectionStrategy().on(parentTopology).select(Samples.first(2)));

        //How do we handle variable sizes? Resizing the entities?
        Entity parent1 = selectedParents.get(0);
        Entity parent2 = selectedParents.get(1);

        if (this.getCrossoverProbability().getParameter() >= this.getRandomDistribution().getRandomNumber()) {
            int minDimension = Math.min(parent1.getDimension(), parent2.getDimension());

            Entity offspring1 = parent1.getClone();
            Entity offspring2 = parent2.getClone();

            // Calculate the mask for the cross-over
            boolean[] mask = new boolean[minDimension];
            for (int i = 0; i < minDimension; i++) {
                if (this.getRandomDistribution().getRandomNumber() <= 0.5) {
                    mask[i] = true;
                }
            }

            // Now apply the mask
            Vector parentChromosome1 = (Vector) parent1.getCandidateSolution();
            Vector parentChromosome2 = (Vector) parent2.getCandidateSolution();
            Vector.Builder offspringChromosome1Builder = Vector.newBuilder();
            Vector.Builder offspringChromosome2Builder = Vector.newBuilder();
            for (int i = 0; i < minDimension; i++) {
                if (!mask[i]) {
                    offspringChromosome1Builder.add(parentChromosome1.get(i).getClone());
                    offspringChromosome2Builder.add(parentChromosome2.get(i).getClone());
                } else {
                    offspringChromosome1Builder.add(parentChromosome2.get(i).getClone());
                    offspringChromosome2Builder.add(parentChromosome1.get(i).getClone());
                }
            }

            offspring1.setCandidateSolution(offspringChromosome1Builder.build());
            offspring2.setCandidateSolution(offspringChromosome2Builder.build());

            offspring1.calculateFitness();
            offspring2.calculateFitness();

            offspring.add(offspring1);
            offspring.add(offspring2);
        }

        return offspring;
    }
}
