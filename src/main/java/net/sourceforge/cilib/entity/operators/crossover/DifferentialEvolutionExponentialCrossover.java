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
import java.util.Arrays;
import java.util.List;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.topologies.TopologyHolder;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 *
 *
 */
public class DifferentialEvolutionExponentialCrossover extends CrossoverStrategy {
    private static final long serialVersionUID = -4811879014933329926L;

    /**
     * {@inheritDoc}
     */
    public DifferentialEvolutionExponentialCrossover getClone() {
        return new DifferentialEvolutionExponentialCrossover();
    }

    /**
     * Perform the cross-over based on the exponential method for recombination. The given
     * <code>parentCollection</code> should only contain two {@linkplain Entity} objects,
     * as the crossover operator is only defined for two {@linkplain Entity}s.
     *
     * <p>
     * It is VERY important that the order in which the parents are presented is consistent.
     * The first {@linkplain Entity} within the collection MUST be the <code>trialVector</code>
     * {@linkplain Entity}, followed by the target parent {@linkplain Entity}.
     *
     * @throws UnsupportedOperationException if the number of parents does not equal the size value of 2.
     * @return A list consisting of the offspring. This operator only returns a single offspring {@linkplain Entity}.
     */
    public List<Entity> crossover(List<Entity> parentCollection) {
        if (parentCollection.size() != 2)
            throw new UnsupportedOperationException("Cannot use exponential recomination on a parent entity grouping not consisting of 2 entities");

        Vector parentVector = (Vector) parentCollection.get(0).getCandidateSolution();
        Vector trialVector = (Vector) parentCollection.get(1).getCandidateSolution();
        Vector offspringVector = parentVector.getClone();

        for (Integer point : getMutationPoints(trialVector.getDimension())) {
            offspringVector.setReal(point, trialVector.getReal(point));
        }

        Entity offspring = parentCollection.get(0).getClone();
        offspring.setCandidateSolution(offspringVector);
        return Arrays.asList(offspring);
    }

    /**
     * Determine the points of mutation.
     * @param dimension The maximum size of the {@linkplain Entity}.
     * @return A {@linkplain List} of points defining the mutation points.
     */
    private List<Integer> getMutationPoints(int dimension) {
        List<Integer> points = new ArrayList<Integer>();
        int j = Double.valueOf(getRandomNumber().getUniform(0, dimension-1)).intValue();

        while ((getRandomNumber().getUniform() >= getCrossoverProbability().getParameter()) || (points.size() == dimension)) {
            points.add(j+1);
            j = (j+1) % dimension;
        }

        return points;
    }

    /**
     * {@inheritDoc}
     */
//    public void performOperation(Topology<? extends Entity> topology, Topology<Entity> offspring) {
    public void performOperation(TopologyHolder holder) {
        throw new UnsupportedOperationException("performOperation() is currently not implemented.");
    }

}
