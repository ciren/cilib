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
import java.util.Arrays;
import java.util.List;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFuction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Binomial crossover operator.
 */
public class DifferentialEvolutionBinomialCrossover implements CrossoverStrategy {

    private static final long serialVersionUID = -2939023704055943968L;
    
    private ProbabilityDistributionFuction random;
    private ControlParameter crossoverPointProbability;
    
    public DifferentialEvolutionBinomialCrossover() {
        this.random = new UniformDistribution();
        this.crossoverPointProbability = ConstantControlParameter.of(0.5);
    }
    
    public DifferentialEvolutionBinomialCrossover(DifferentialEvolutionBinomialCrossover copy) {
        this.random = copy.random;
        this.crossoverPointProbability = copy.crossoverPointProbability.getClone();
    }

    @Override
    public DifferentialEvolutionBinomialCrossover getClone() {
        return new DifferentialEvolutionBinomialCrossover(this);
    }

    /**
     * <p>
     * Perform the cross-over based on the binomial method for recombination. The given
     * <code>parentCollection</code> should only contain two {@linkplain Entity} objects,
     * as the crossover operator is only defined for two {@linkplain Entity}s.
     * </p>
     * <p>
     * It is VERY important that the order in which the parents are presented is consistent.
     * The first {@linkplain Entity} within the collection MUST be the <code>trialVector</code>
     * {@linkplain Entity}, followed by the target parent {@linkplain Entity}.
     * </p>
     * <p>This method implements the following logic:</p>
     * <pre>
     * for j = 1, ..., x_n:
     *   if ( (U(0,1) < P_c) || (j == i) )
     *     x'_{i,j}(t) = trialVector_{i,j}(t)
     *   else
     *     x'_{i,j}(t) = x_{i,j}(t)
     * </pre>
     *
     * @param parentCollection the collection of parent {@linkplain Entity} objects.
     * @throws UnsupportedOperationException if the number of parents does not equal the size value of 2.
     * @return A list consisting of the offspring. This operator only returns a single offspring {@linkplain Entity}.
     */
    @Override
    public <E extends Entity> List<E> crossover(List<E> parentCollection) {
        Preconditions.checkArgument(parentCollection.size() == 2, "DifferentialEvolutionBinomialCrossover requires 2 parents.");

        Vector parentVector = (Vector) parentCollection.get(0).getCandidateSolution();
        Vector trialVector = (Vector) parentCollection.get(1).getCandidateSolution();
        Vector.Builder offspringVector = Vector.newBuilder();

        //this is the index of the dimension that will always be included
        int i = Double.valueOf(random.getRandomNumber(0, parentVector.size())).intValue();

        for (int j = 0; j < parentVector.size(); j++) {
            if (random.getRandomNumber() < crossoverPointProbability.getParameter() || j == i) {
                offspringVector.add(trialVector.get(j));
            } else {
                offspringVector.add(parentVector.get(j));
            }
        }

        E offspring = (E) parentCollection.get(0).getClone();
        offspring.setCandidateSolution(offspringVector.build());
        
        return Arrays.asList(offspring);
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
