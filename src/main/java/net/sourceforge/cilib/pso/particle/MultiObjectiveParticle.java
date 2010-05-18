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
package net.sourceforge.cilib.pso.particle;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.type.types.Type;

/**
 * TODO: Complete this javadoc.
 */
public class MultiObjectiveParticle extends StandardParticle {
    private static final long serialVersionUID = 2449622504036301616L;

    public void setBestPosition(Type bestPosition) {
        Fitness bestPositionFitness = AbstractAlgorithm.get().getOptimisationProblem().getFitness(bestPosition);

        this.getProperties().put(EntityType.Particle.BEST_POSITION, bestPosition.getClone());
        this.getProperties().put(EntityType.Particle.BEST_FITNESS, bestPositionFitness);
    }

}
