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
package net.sourceforge.cilib.entity.operators.crossover.de;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFuction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.type.types.container.Vector;

public class DifferentialEvolutionExponentialCrossover implements CrossoverStrategy {

    private static final long serialVersionUID = -4811879014933329926L;

    private ProbabilityDistributionFuction random;
    private ControlParameter crossoverPointProbability;
    
    public DifferentialEvolutionExponentialCrossover() {
        this.random = new UniformDistribution();
        this.crossoverPointProbability = ConstantControlParameter.of(0.5);
    }
    
    public DifferentialEvolutionExponentialCrossover(DifferentialEvolutionExponentialCrossover copy) {
        this.random = copy.random;
        this.crossoverPointProbability = copy.crossoverPointProbability.getClone();
    }

    @Override
    public DifferentialEvolutionExponentialCrossover getClone() {
        return new DifferentialEvolutionExponentialCrossover(this);
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
    @Override
    public <E extends Entity> List<E> crossover(List<E> parentCollection) {
        Preconditions.checkArgument(parentCollection.size() == 2, "DifferentialEvolutionExponentialCrossover requires 2 parents.");

        Vector parentVector = (Vector) parentCollection.get(0).getCandidateSolution();
        Vector trialVector = (Vector) parentCollection.get(1).getCandidateSolution();
        Vector.Builder offspringVector = Vector.newBuilder();
        List<Integer> points = getMutationPoints(trialVector.size());
        
        for (int i = 0; i < trialVector.size(); i++) {
            if (points.contains(i)) {
                offspringVector.add(trialVector.get(i));
            } else {
                offspringVector.add(parentVector.get(i));
            }
        }

        E offspring = (E) parentCollection.get(0).getClone();
        offspring.setCandidateSolution(offspringVector.build());
        
        return Arrays.asList(offspring);
    }

    /**
     * Determine the points of mutation.
     * @param dimension The maximum size of the {@linkplain Entity}.
     * @return A {@linkplain List} of points defining the mutation points.
     */
    private List<Integer> getMutationPoints(int dimension) {
        List<Integer> points = Lists.newArrayList();
        int j = Double.valueOf(random.getRandomNumber(0, dimension)).intValue();

        do {
            points.add(j);
            j = (j + 1) % dimension;
        } while (random.getRandomNumber() < crossoverPointProbability.getParameter() && (points.size() < dimension));

        return points;
    }
    
    public void setRandom(ProbabilityDistributionFuction random) {
        this.random = random;
    }

    public ProbabilityDistributionFuction getRandom() {
        return random;
    }

    public void setCrossoverPointProbability(ControlParameter crossoverPointProbability) {
        this.crossoverPointProbability = crossoverPointProbability;
    }

    public ControlParameter getCrossoverPointProbability() {
        return crossoverPointProbability;
    }

    @Override
    public int getNumberOfParents() {
        return 2;
    }
}
