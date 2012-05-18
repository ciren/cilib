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
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.problem.Fitness;

/**
 * Calculates the change in pheromone level for a particular particle's behavior
 * using exponential change:
 * <br>change = sign(diffInFitnessChange) * exp(abs(diffInFitnessChange)) - 1
 */
public class ExponentialPheromoneUpdateStrategy implements PheromoneUpdateStrategy{
    
    private ControlParameter scale;

    /**
     * Creates a new instance of ExponentialPheromoneUpdateStrategy
     */
    public ExponentialPheromoneUpdateStrategy() {
        this.scale = ConstantControlParameter.of(0.001);
    }

    /**
     * Creates an ExponentialPheromoneUpdateStrategy with the same attributes as another
     * ExponentialPheromoneUpdateStrategy
     *
     * @param o the other instance ExponentialPheromoneUpdateStrategy
     */
    public ExponentialPheromoneUpdateStrategy(ExponentialPheromoneUpdateStrategy o) {
    }

    /**
     * {@inheritDoc}
     */
    public ExponentialPheromoneUpdateStrategy getClone() {
        return new ExponentialPheromoneUpdateStrategy(this);
    }

    /**
     * Calculates the change in pheromone level for a particular particle's behavior.
     *
     * @param e The particle whose behavior is being used for the pheromone update
     * @return The change in pheromone level for a behavior
     */
    @Override
    public double updatePheromone(Particle e) {
        Fitness prevFitness = ((Fitness)e.getProperties().get(EntityType.PREVIOUS_FITNESS));
        double diff = e.getFitness().getValue() - (prevFitness.getValue().isNaN() ? 0 : prevFitness.getValue());
        double sign = e.getFitness().compareTo(prevFitness);
        return Math.exp(sign * Math.abs(diff) * scale.getParameter());
    }

    public void setScale(ControlParameter scale) {
        this.scale = scale;
    }

    public ControlParameter getScale() {
        return scale;
    }
}
