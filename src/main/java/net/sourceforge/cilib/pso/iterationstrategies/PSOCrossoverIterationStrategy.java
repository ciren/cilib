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
package net.sourceforge.cilib.pso.iterationstrategies;

import java.util.Comparator;
import java.util.List;
import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.comparator.AscendingFitnessComparator;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.ParentCentricCrossoverStrategy;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.util.selection.Samples;
import net.sourceforge.cilib.util.selection.recipes.ElitistSelector;
import net.sourceforge.cilib.util.selection.recipes.RandomSelector;

/**
 * An iteration strategy that uses crossover to replace particles if the
 * offspring is better than the worst parent.
 * 
 * @author filipe
 */
public class PSOCrossoverIterationStrategy extends AbstractIterationStrategy<PSO> {
    
    private IterationStrategy delegate;
    private CrossoverStrategy crossoverStrategy;
    private int retries;
    private RandomSelector selector;
    private Comparator comparator;
    
    /**
     * Default constructor
     */
    public PSOCrossoverIterationStrategy() {
        this.delegate = new SynchronousIterationStrategy();
        this.crossoverStrategy = new ParentCentricCrossoverStrategy();
        this.retries = 10;
        this.selector = new RandomSelector();
        this.comparator = new AscendingFitnessComparator<Particle>();
    }
    
    /**
     * Copy constructor
     * 
     * @param copy 
     */
    public PSOCrossoverIterationStrategy(PSOCrossoverIterationStrategy copy) {
        this.delegate = copy.delegate.getClone();
        this.crossoverStrategy = copy.crossoverStrategy.getClone();
        this.retries = copy.retries;
        this.selector = copy.selector;
        this.comparator = copy.comparator;
    }

    /**
     * Clones this instance
     * 
     * @return the clone
     */
    @Override
    public PSOCrossoverIterationStrategy getClone() {
        return new PSOCrossoverIterationStrategy(this);
    }

    /**
     * Performs a standard iteration then selects three random parents and performs
     * crossover with them (default crossover strategy is PCX). If the offspring is
     * better that the worst parent then the worst parent is replaced by the offspring.
     * If not, the process is repeated a number of times (default 10).
     * 
     * @param algorithm 
     */
    @Override
    public void performIteration(PSO algorithm) {
        delegate.performIteration(algorithm);
        
        int counter = 0;
        boolean isBetter = false;
        
        do {
            // get 3 random particles
            List<Entity> parents = selector.on(algorithm.getTopology()).select(Samples.all().unique()).subList(0, 3);
            
            //perform crossover and compute offspring's fitness
            Particle offspring = (Particle) crossoverStrategy.crossover(parents).get(0);
            offspring.calculateFitness();
            
            //get worst parent
            Particle worstParent = (Particle) new ElitistSelector().on(parents).select(Samples.all()).get(2);
            
            //replace worst parent with offspring if offspring is better
            if (comparator.compare(offspring, worstParent) > 0) {
                isBetter = true;
                worstParent = offspring;
            }            
        } while(++counter < retries || !isBetter);
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
     * Sets the standard iteration strategy to use.
     * 
     * @param delegate 
     */
    public void setDelegate(IterationStrategy delegate) {
        this.delegate = delegate;
    }

    /**
     * Sets the number of times to retry the crossover process.
     * 
     * @param retries 
     */
    public void setRetries(int retries) {
        this.retries = retries;
    }
}
