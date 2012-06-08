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
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.real.ParentCentricCrossoverStrategy;
import net.sourceforge.cilib.pso.crossover.fitnessupdate.NullOffspringBestFitnessProvider;
import net.sourceforge.cilib.pso.crossover.fitnessupdate.OffspringBestFitnessProvider;
import net.sourceforge.cilib.pso.crossover.pbestupdate.CurrentPositionOffspringPBestProvider;
import net.sourceforge.cilib.pso.crossover.pbestupdate.OffspringPBestProvider;
import net.sourceforge.cilib.pso.crossover.velocityprovider.IdentityOffspringVelocityProvider;
import net.sourceforge.cilib.pso.crossover.velocityprovider.OffspringVelocityProvider;

public class ParticleCrossoverStrategy implements CrossoverStrategy {
    private OffspringVelocityProvider velocityProvider;
    private OffspringPBestProvider pbestProvider;
    private OffspringBestFitnessProvider fitnessProvider;
    private CrossoverStrategy crossoverStrategy;
    
    public ParticleCrossoverStrategy() {
        this(new ParentCentricCrossoverStrategy(), new CurrentPositionOffspringPBestProvider(), 
                new IdentityOffspringVelocityProvider(), new NullOffspringBestFitnessProvider());
    }
    
    public ParticleCrossoverStrategy(CrossoverStrategy strategy, OffspringPBestProvider pbestUpdate, 
            OffspringVelocityProvider velUpdate, OffspringBestFitnessProvider fitnessUpdate) {
        this.crossoverStrategy = strategy;
        this.pbestProvider = pbestUpdate;
        this.velocityProvider = velUpdate;
        this.fitnessProvider = fitnessUpdate;
    }
    
    public ParticleCrossoverStrategy(ParticleCrossoverStrategy copy) {
        this.crossoverStrategy = copy.crossoverStrategy.getClone();
        this.pbestProvider = copy.pbestProvider;
        this.velocityProvider = copy.velocityProvider;
        this.fitnessProvider = copy.fitnessProvider;
    }
    
    @Override
    public ParticleCrossoverStrategy getClone() {
        return new ParticleCrossoverStrategy(this);
    }
    
    @Override
    public <E extends Entity> List<E> crossover(List<E> parentCollection) {
        List<Particle> parents = (List<Particle>) parentCollection;
        List<Particle> offspring = crossoverStrategy.crossover(parents);
        
        for (Particle p : offspring) {
            p.getProperties().put(EntityType.Particle.BEST_POSITION, pbestProvider.f(parents, p));
            p.getProperties().put(EntityType.Particle.VELOCITY, velocityProvider.f(parents, p));
            
            p.calculateFitness();
            
            p.getProperties().put(EntityType.Particle.BEST_FITNESS, fitnessProvider.f(parents, p));
        }
        
        return (List<E>) offspring;
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

    public void setFitnessProvider(OffspringBestFitnessProvider fitnessProvider) {
        this.fitnessProvider = fitnessProvider;
    }

    public OffspringBestFitnessProvider getFitnessProvider() {
        return fitnessProvider;
    }

    @Override
    public int getNumberOfParents() {
        return crossoverStrategy.getNumberOfParents();
    }
}
