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
package net.sourceforge.cilib.util.calculator;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.solution.Fitness;

/**
 * Calculates the fitness by passing the entity's blackboard to the algorithm's optimization problem
 */
public class PropertyBasedFitnessCalculator implements
        FitnessCalculator<Entity> {
    private static final long serialVersionUID = -5225410711497956675L;

    /**
     * {@inheritDoc}
     */
    @Override
    public PropertyBasedFitnessCalculator getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Fitness getFitness(Entity entity) {
        Algorithm algorithm = AbstractAlgorithm.get();
        return algorithm.getOptimisationProblem().getFitness(entity.getProperties());
    }
}
