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

import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.List;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.random.ProbabilityDistributionFuction;
import net.sourceforge.cilib.math.random.UniformDistribution;
import net.sourceforge.cilib.type.types.container.Vector;

public class OnePointCrossoverStrategy implements CrossoverStrategy {

    private static final long serialVersionUID = 7313531386910938748L;
    
    private ProbabilityDistributionFuction random;

    public OnePointCrossoverStrategy() {
        this.random = new UniformDistribution();
    }

    public OnePointCrossoverStrategy(OnePointCrossoverStrategy copy) {
        this.random = copy.random;
    }

    @Override
    public OnePointCrossoverStrategy getClone() {
        return new OnePointCrossoverStrategy(this);
    }

    @Override
    public <E extends Entity> List<E> crossover(List<E> parentCollection) {
        Preconditions.checkArgument(parentCollection.size() == 2, "OnePointCrossoverStrategy requires 2 parents.");
        
        E offspring1 = (E) parentCollection.get(0).getClone();
        E offspring2 = (E) parentCollection.get(1).getClone();

        // Select the pivot point where crossover will occour
        int maxLength = Math.min(offspring1.getDimension(), offspring2.getDimension());
        int crossoverPoint = Double.valueOf(random.getRandomNumber(0, maxLength + 1)).intValue();

        Vector offspringVector1 = (Vector) offspring1.getCandidateSolution();
        Vector offspringVector2 = (Vector) offspring2.getCandidateSolution();

        Vector.Builder offspringVector1Builder = Vector.newBuilder();
        Vector.Builder offspringVector2Builder = Vector.newBuilder();

        offspringVector1Builder.copyOf(offspringVector1.copyOfRange(0, crossoverPoint));
        offspringVector2Builder.copyOf(offspringVector2.copyOfRange(0, crossoverPoint));
        offspringVector1Builder.copyOf(offspringVector2.copyOfRange(crossoverPoint, offspringVector2.size()));
        offspringVector2Builder.copyOf(offspringVector1.copyOfRange(crossoverPoint, offspringVector1.size()));
        
        offspring1.setCandidateSolution(offspringVector1Builder.build());
        offspring2.setCandidateSolution(offspringVector2Builder.build());

        return Arrays.asList(offspring1, offspring2);
    }

    public void setRandom(ProbabilityDistributionFuction random) {
        this.random = random;
    }

    public ProbabilityDistributionFuction getRandom() {
        return random;
    }

    @Override
    public int getNumberOfParents() {
        return 2;
    }
}
