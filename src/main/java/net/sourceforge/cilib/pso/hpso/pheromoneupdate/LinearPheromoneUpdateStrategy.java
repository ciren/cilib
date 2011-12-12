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
 * in direct proportion to the change in the particle's fitness from one
 * iteration to the next.
 *
 * @author filipe
 */
public class LinearPheromoneUpdateStrategy implements PheromoneUpdateStrategy{
    private ConstantControlParameter gradient;

    /**
     * Creates a new instance of LinearPheromoneUpdateStrategy
     */
    public LinearPheromoneUpdateStrategy() {
        this.gradient = new ConstantControlParameter(0.1);
    }

    /**
     * Creates a LinearPheromoneUpdateStrategy with the same attributes as another
     * LinearPheromoneUpdateStrategy
     *
     * @param o the other instance LinearPheromoneUpdateStrategy
     */
    public LinearPheromoneUpdateStrategy(LinearPheromoneUpdateStrategy o) {
        this.gradient = o.gradient;
    }

    /**
     * {@inheritDoc}
     */
    public LinearPheromoneUpdateStrategy getClone() {
        return new LinearPheromoneUpdateStrategy(this);
    }

    /**
     * Calculates the change in pheromone level for a particular particle's behavior.
     *
     * @param e The particle whose behavior is being used for the pheromone update
     * @return The change in pheromone level for a behavior
     */
    @Override
    public double updatePheromone(Particle e) {
        Fitness prevFitness = ((Fitness)e.getProperties().get(EntityType.Particle.PREV_FITNESS));
        double diff = e.getFitness().getValue() - (prevFitness.getValue().isNaN() ? 0 : prevFitness.getValue());
        return Math.abs(diff) * this.gradient.getParameter() * (e.getFitness().compareTo(prevFitness));
    }


}
