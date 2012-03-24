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
package net.sourceforge.cilib.pso.guideprovider;

import java.util.List;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.ParentCentricCrossoverStrategy;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.util.selection.Samples;
import net.sourceforge.cilib.util.selection.recipes.RandomSelector;

/**
 * This guide provider generates an offspring particle from random parents.
 * If the offspring is better than the gBest of the swarm then the offspring
 * "replaces" (the gBest's best position and fitness are updated) the gBest.
 * This is done until a better offspring is generated or the retry limit is 
 * reached.
 * 
 * @author filipe
 */
public class CrossoverGuideProvider implements GuideProvider {
    
    private GuideProvider delegate;
    private CrossoverStrategy crossoverStrategy;
    private int retries;
    private RandomSelector selector;

    private enum TempEnums {
        TEMP
    };
    
    /**
     * Default constructor.
     */
    public CrossoverGuideProvider() {
        this.delegate = new NBestGuideProvider();
        this.crossoverStrategy = new ParentCentricCrossoverStrategy();
        this.retries = 10;
        this.selector = new RandomSelector();
    }
    
    /**
     * Copy constructor.
     * 
     * @param copy 
     */
    public CrossoverGuideProvider(CrossoverGuideProvider copy) {
        this.delegate = copy.delegate.getClone();
        this.crossoverStrategy = copy.crossoverStrategy.getClone();
        this.retries = copy.retries;
        this.selector = copy.selector;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public CrossoverGuideProvider getClone() {
        return new CrossoverGuideProvider(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StructuredType get(Particle particle) {
        int counter = 0;
        boolean isBetter = false;
        Topology<Particle> topology = ((PSO)AbstractAlgorithm.get()).getTopology();
       
        do {
            // get 3 random particles
            List<Entity> parents = selector.on(topology).select(Samples.all().unique()).subList(0, 3);
            
            //put pbest as candidate solution for the crossover
            for (Entity e : parents) {
                Particle p = (Particle) e;
                e.getProperties().put(TempEnums.TEMP, p.getCandidateSolution());
                e.getProperties().put(EntityType.CANDIDATE_SOLUTION, p.getBestPosition());              
            }
            
            //perform crossover and compute offspring's fitness
            Particle offspring = (Particle) crossoverStrategy.crossover(parents).get(0);
            offspring.calculateFitness();
            
            Particle gBest = particle.getNeighbourhoodBest();

            //replace gBest if offspring is better (we dont replace the gbest particle,
            //we only update it's best position)
            if (offspring.getFitness().compareTo(gBest.getBestFitness()) > 0) {
                isBetter = true;
                gBest.getProperties().put(EntityType.Particle.BEST_POSITION, offspring.getCandidateSolution());
                gBest.getProperties().put(EntityType.Particle.BEST_FITNESS, offspring.getFitness());
            }
            
            for (Entity e : parents) {
                e.getProperties().put(EntityType.CANDIDATE_SOLUTION, e.getProperties().get(TempEnums.TEMP));
            }
        } while(++counter < retries && !isBetter);
        
        return delegate.get(particle);
    }

    /**
     * Sets the crossover strategy to use.
     * 
     * @param crossoverStrategy 
     */
    public void setCrossoverStrategy(CrossoverStrategy crossoverStrategy) {
        this.crossoverStrategy = crossoverStrategy;
    }

    /**
     * Sets the number times to retry before returning the gBest.
     * 
     * @param retries 
     */
    public void setRetries(int retries) {
        this.retries = retries;
    }
}
