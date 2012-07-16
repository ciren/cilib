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

import net.sourceforge.cilib.entity.Particle;

/**
 * Interface to calculate the change in pheromone level for the adaptive HPSO using
 * pheromones
 */
public interface PheromoneUpdateStrategy {

    /**
     * Calculates the amount that a behavior's pheromone level should change
     *
     * @param e the particle which affects the behavior's pheromone level
     * @return the change in pheromone for a particle's behavior
     */
    double updatePheromone(Particle e);
}
