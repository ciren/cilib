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
package net.sourceforge.cilib.pso.velocityprovider;

import java.util.Arrays;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.real.ParentCentricCrossoverStrategy;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Updates a particle's solution to the crossover of its current position, 
 * personal best and global best. Use with a LinearPositionProvider. Default 
 * crossover strategy is PCX.
 */
public class CrossoverVelocityProvider implements VelocityProvider {
    
    private CrossoverStrategy crossoverStrategy;
    
    /**
     * Default constructor.
     */
    public CrossoverVelocityProvider() {
        this.crossoverStrategy = new ParentCentricCrossoverStrategy();
    }
    
    /**
     * Copy constructor.
     * @param copy 
     */
    public CrossoverVelocityProvider(CrossoverVelocityProvider copy) {
        this.crossoverStrategy = copy.crossoverStrategy.getClone();
    }

    /**
     * Clones this instance
     * 
     * @return the clone
     */
    @Override
    public CrossoverVelocityProvider getClone() {
        return new CrossoverVelocityProvider(this);
    }

    /**
     * Returns the new position
     * 
     * @param particle The particle to update
     * @return  the particle's new position
     */
    @Override
    public Vector get(Particle particle) {
        Entity parent1 = particle.getClone();
        Entity parent2 = particle.getClone();
        Entity parent3 = particle.getClone();
        
        parent2.setCandidateSolution(particle.getBestPosition());
        parent3.setCandidateSolution(particle.getNeighbourhoodBest().getBestPosition());
        
        return (Vector) crossoverStrategy.crossover(Arrays.asList(parent1, parent2, parent3))
                .get(0).getCandidateSolution();
    }

    /**
     * Sets the crossover strategy to use.
     * 
     * @param crossoverStrategy 
     */
    public void setCrossoverStrategy(CrossoverStrategy crossoverStrategy) {
        this.crossoverStrategy = crossoverStrategy;
    }
}
