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
package net.sourceforge.cilib.pso.hpso.pheromoneupdate;

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.problem.Fitness;

/**
 * Calculates the change in pheromone level for a particular particle's behavior
 * using three constants for whether the particle did better, the same or worse
 * than the previous iteration.
 *
 * @author filipe
 */
public class ConstantPheromoneUpdateStrategy implements PheromoneUpdateStrategy{
    private ConstantControlParameter better;
    private ConstantControlParameter same;
    private ConstantControlParameter worse;

    /**
     * Initializes the controlparameters to 1.0, 0.5 and 0.0 for whether the
     * particle did better, the same or worse respectively.
     */
    public ConstantPheromoneUpdateStrategy() {
        this.better = new ConstantControlParameter(1.0);
        this.same = new ConstantControlParameter(0.5);
        this.worse = new ConstantControlParameter(0.0);
    }

    /**
     * Creates a ConstantPheromoneUpdateStrategy with the same attributes as another
     * ConstantPheromoneUpdateStrategy
     * 
     * @param o the other instance ConstantPheromoneUpdateStrategy
     */
    public ConstantPheromoneUpdateStrategy(ConstantPheromoneUpdateStrategy o) {
        this.better = o.better;
        this.same = o.same;
        this.worse = o.worse;
    }

    /**
     * {@inheritDoc}
     */
    public ConstantPheromoneUpdateStrategy getClone() {
        return new ConstantPheromoneUpdateStrategy(this);
    }

    /**
     * Calculates the change in pheromone level for a particular particle's behavior.
     *
     * @param e The particle whose behavior is being used for the pheromone update
     * @return The change in pheromone level for a behavior
     */
    @Override
    public double updatePheromone(Particle e) {
        int compResult = ((Fitness)e.getProperties().get(EntityType.Particle.PREV_FITNESS)).compareTo(e.getFitness());
        double result = compResult < 0 ? this.better.getParameter() :
            (compResult == 0 ? this.same.getParameter() : this.worse.getParameter());
        return result;
    }
}
