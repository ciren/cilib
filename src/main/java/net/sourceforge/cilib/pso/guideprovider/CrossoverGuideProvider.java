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

import java.util.Comparator;
import java.util.List;
import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.comparator.AscendingFitnessComparator;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.ParentCentricCrossoverStrategy;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.util.selection.Samples;
import net.sourceforge.cilib.util.selection.recipes.RandomSelector;

/**
 *
 * @author filipe
 */
public class CrossoverGuideProvider implements GuideProvider {
    
    private CrossoverStrategy crossoverStrategy;
    private int retries;
    private RandomSelector selector;
    private Comparator comparator;

    public CrossoverGuideProvider() {
        this.crossoverStrategy = new ParentCentricCrossoverStrategy();
        this.retries = 10;
        this.selector = new RandomSelector();
        this.comparator = new AscendingFitnessComparator<Particle>();
    }
    
    public CrossoverGuideProvider(CrossoverGuideProvider copy) {
        this.crossoverStrategy = copy.crossoverStrategy.getClone();
        this.retries = copy.retries;
        this.selector = copy.selector;
        this.comparator = copy.comparator;
    }
    
    @Override
    public GuideProvider getClone() {
        return new CrossoverGuideProvider(this);
    }

    @Override
    public StructuredType get(Particle particle) {
        int counter = 0;
        boolean isBetter = false;
        Topology<Particle> topology = ((PSO)AbstractAlgorithm.get()).getTopology();
        Particle offspring = null;
        
        do {
            // get 3 random particles
            List<Entity> parents = selector.on(topology).select(Samples.all().unique()).subList(0, 3);
            
            //perform crossover and compute offspring's fitness
            offspring = (Particle) crossoverStrategy.crossover(parents).get(0);
            offspring.calculateFitness();
            
            for (Particle p : topology) {
                //get gBest
                Particle gBest = p.getNeighbourhoodBest();

                //replace gBest if offspring is better
                if (comparator.compare(offspring, gBest.getBestPosition()) > 0) {
                    isBetter = true;
                    gBest.getProperties().put(EntityType.Particle.BEST_POSITION, offspring.getCandidateSolution());
                    gBest.getProperties().put(EntityType.Particle.BEST_FITNESS, offspring.getFitness());
                }
            }
        } while(++counter < retries || !isBetter);
        
        if (isBetter)
            return offspring.getCandidateSolution();
        
        return particle.getNeighbourhoodBest().getBestPosition();
    }    
}
