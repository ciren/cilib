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
package net.sourceforge.cilib.pso.positionupdatestrategies;

import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;

/**
 * Update the personal best of the particle, based on the standard PSO definition
 * of the process.
 *
 * @author gpampara
 */
public class StandardPersonalBestUpdateStrategy implements PersonalBestUpdateStrategy {
    private static final long serialVersionUID = 266386833476786081L;

    /**
     * {@inheritDoc}
     */
    @Override
    public PersonalBestUpdateStrategy getClone() {
        return this;
    }

    /**
     * If the current fitness is better than the current best fitness, update
     * the best fitness of the particle to equal the current fitness and make
     * the personal best position a clone of the current particle position.
     * @param particle The particle to update.
     */
    @Override
    public void updatePersonalBest(Particle particle) {
        if (particle.getFitness().compareTo(particle.getBestFitness()) > 0) {
            particle.getProperties().put(EntityType.Particle.BEST_FITNESS, particle.getFitness());
            particle.getProperties().put(EntityType.Particle.BEST_POSITION, particle.getPosition().getClone());
        }
    }
}
