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
package net.sourceforge.cilib.pso.crossover;

import java.util.List;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.pso.crossover.pbestupdate.OffspringPBestProvider;
import net.sourceforge.cilib.pso.crossover.velocityprovider.OffspringVelocityProvider;

public class ParticleCrossover {
    private OffspringVelocityProvider velocityProvider;
    private OffspringPBestProvider pbestProvider;
    private CrossoverStrategy crossoverStrategy;
    
    public List<Particle> performParticleCrossover(List<Particle> parents) {
        List<Particle> offspring = (List<Particle>) crossoverStrategy.crossover(parents);
        
        for (Particle p : offspring) {
            p.getProperties().put(EntityType.Particle.BEST_POSITION, pbestProvider.get(parents, p));
            p.getProperties().put(EntityType.Particle.VELOCITY, velocityProvider.get(parents, p));
            
            p.calculateFitness();
        }
        
        return offspring;
    }

    public void setVelocityProvider(OffspringVelocityProvider velocityProvider) {
        this.velocityProvider = velocityProvider;
    }

    public OffspringVelocityProvider getVelocityProvider() {
        return velocityProvider;
    }

    public void setPbestProvider(OffspringPBestProvider pbestProvider) {
        this.pbestProvider = pbestProvider;
    }

    public OffspringPBestProvider getPbestProvider() {
        return pbestProvider;
    }

    public void setCrossoverStrategy(CrossoverStrategy crossoverStrategy) {
        this.crossoverStrategy = crossoverStrategy;
    }

    public CrossoverStrategy getCrossoverStrategy() {
        return crossoverStrategy;
    }
}
