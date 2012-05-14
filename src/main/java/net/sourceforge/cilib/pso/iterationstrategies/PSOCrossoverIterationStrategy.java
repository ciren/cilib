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

import fj.P3;
import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.crossover.BoltzmannCrossoverSelection;
import net.sourceforge.cilib.pso.crossover.CrossoverSelection;

/**
 * An iteration strategy that uses crossover to replace particles if the
 * offspring is better than the worst parent.
 */
public class PSOCrossoverIterationStrategy extends AbstractIterationStrategy<PSO> {
    
    private IterationStrategy delegate;
    private CrossoverSelection crossoverSelection;
    
    /**
     * Default constructor
     */
    public PSOCrossoverIterationStrategy() {
        this.delegate = new SynchronousIterationStrategy();
        this.crossoverSelection = new BoltzmannCrossoverSelection();
    }
    
    /**
     * Copy constructor
     * 
     * @param copy 
     */
    public PSOCrossoverIterationStrategy(PSOCrossoverIterationStrategy copy) {
        this.delegate = copy.delegate.getClone();
        this.crossoverSelection = copy.crossoverSelection.getClone();
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
     * better than the worst parent then the worst parent is replaced by the offspring.
     * If not, the process is repeated a number of times (default 10).
     * 
     * @param algorithm 
     */
    @Override
    public void performIteration(PSO algorithm) {
        delegate.performIteration(algorithm);
        
        P3<Boolean, Particle, Particle> result = 
                crossoverSelection.doAction(algorithm, EntityType.CANDIDATE_SOLUTION, EntityType.FITNESS);

        // if offspring is better replace the selected particle
        if (result._1()) {
            int i = algorithm.getTopology().indexOf(result._2());
            algorithm.getTopology().set(i, result._3());
            result._3().setNeighbourhoodBest(result._2().getNeighbourhoodBest());
        }
    }

    /**
     * Sets the standard iteration strategy to use.
     * 
     * @param delegate 
     */
    public void setDelegate(IterationStrategy delegate) {
        this.delegate = delegate;
    }

    public void setCrossoverSelection(CrossoverSelection crossoverSelection) {
        this.crossoverSelection = crossoverSelection;
    }
}
