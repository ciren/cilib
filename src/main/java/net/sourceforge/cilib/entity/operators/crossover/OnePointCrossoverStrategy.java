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
package net.sourceforge.cilib.entity.operators.crossover;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.TopologyHolder;
import net.sourceforge.cilib.type.types.container.Vector;

/**
*
* @author Andries Engelbrecht
* @author Gary Pampara
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

    public List<Entity> crossover(List<Entity> parentCollection) {
        ArrayList<Entity> offspring = new ArrayList<Entity>();
        offspring.ensureCapacity(parentCollection.size());

        // This needs a selection strategy to select the parent individuals!!!!
        Entity parent1 = parentCollection.get(0);
        Entity parent2 = parentCollection.get(1);

        if (this.getRandomNumber().getUniform() <= this.getCrossoverProbability().getParameter()) {
            // Select the pivot point where crossover will occour
            int maxLength = Math.min(parent1.getDimension(), parent2.getDimension());
            int crossoverPoint = Double.valueOf(this.getRandomNumber().getUniform(0, maxLength+1)).intValue();

            Entity offspring1 = parent1.getClone();
            Entity offspring2 = parent2.getClone();

            Vector offspringVector1 = (Vector) offspring1.getCandidateSolution();
            Vector offspringVector2 = (Vector) offspring2.getCandidateSolution();

            for (int j = crossoverPoint; j < offspringVector2.getDimension(); j++) {
                offspringVector1.remove(j);
                offspringVector1.insert(j, offspringVector2.get(j).getClone());
            }

            for (int j = crossoverPoint; j < offspringVector1.getDimension(); j++) {
                offspringVector2.remove(j);
                offspringVector2.insert(j, offspringVector1.get(j).getClone());
            }

            offspring1.calculateFitness();
            offspring2.calculateFitness();

            offspring.add(offspring1);
            offspring.add(offspring2);
        }

        return offspring;
    }

//    public void performOperation(Topology<? extends Entity> topology, Topology<Entity> offspring) {
    public void performOperation(TopologyHolder holder) {
        List<Entity> parentCollection = new ArrayList<Entity>();

        Topology<? extends Entity> topology = holder.getTopology();
//        Topology<Entity> offspring = (Topology<Entity>) holder.getOffpsring();

        parentCollection.add(getSelectionStrategy().select(topology));
        parentCollection.add(getSelectionStrategy().select(topology));

//        offspring.addAll(this.crossover(parentCollection));
        holder.addAll(this.crossover(parentCollection));
    }
}
