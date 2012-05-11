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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 */
public class OnePointCrossoverStrategy extends CrossoverStrategy {

    private static final long serialVersionUID = 7313531386910938748L;

    public OnePointCrossoverStrategy() {
    }

    public OnePointCrossoverStrategy(OnePointCrossoverStrategy copy) {
        super(copy);
    }

    /**
     * {@inheritDoc}
     */
    public OnePointCrossoverStrategy getClone() {
        return new OnePointCrossoverStrategy(this);
    }

    public List<? extends Entity> crossover(List<? extends Entity> parentCollection) {
        ArrayList<Entity> offspring = new ArrayList<Entity>();
        offspring.ensureCapacity(parentCollection.size());

        // This needs a selection strategy to select the parent individuals!!!!
        Entity parent1 = parentCollection.get(0);
        Entity parent2 = parentCollection.get(1);

        if (this.getRandomDistribution().getRandomNumber() <= this.getCrossoverProbability().getParameter()) {
            // Select the pivot point where crossover will occour
            int maxLength = Math.min(parent1.getDimension(), parent2.getDimension());
            int crossoverPoint = Double.valueOf(this.getRandomDistribution().getRandomNumber(0, maxLength + 1)).intValue();

            Entity offspring1 = parent1.getClone();
            Entity offspring2 = parent2.getClone();

            Vector offspringVector1 = (Vector) offspring1.getCandidateSolution();
            Vector offspringVector2 = (Vector) offspring2.getCandidateSolution();

            Vector.Builder offspringVector1Builder = Vector.newBuilder();
            Vector.Builder offspringVector2Builder = Vector.newBuilder();

            offspringVector1Builder.copyOf(offspringVector1.copyOfRange(0, crossoverPoint));
            offspringVector2Builder.copyOf(offspringVector2.copyOfRange(0, crossoverPoint));
            offspringVector1Builder.copyOf(offspringVector2.copyOfRange(crossoverPoint, offspringVector2.size()));
            offspringVector2Builder.copyOf(offspringVector1.copyOfRange(crossoverPoint, offspringVector1.size()));

            offspring1.calculateFitness();
            offspring2.calculateFitness();

            offspring.add(offspring1);
            offspring.add(offspring2);
        }

        return offspring;
    }
}
