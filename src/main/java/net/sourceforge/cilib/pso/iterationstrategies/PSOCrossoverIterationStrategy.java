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

import net.sourceforge.cilib.algorithm.population.AbstractIterationStrategy;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.comparator.SocialBestFitnessComparator;
import net.sourceforge.cilib.pso.PSO;
import net.sourceforge.cilib.pso.crossover.BoltzmannCrossoverSelection;
import net.sourceforge.cilib.pso.crossover.PSOCrossoverOperation;

/**
 * An iteration strategy that uses different PSOCrossoverOperations to affect the 
 * swarm of particles.
 */
public class PSOCrossoverIterationStrategy extends AbstractIterationStrategy<PSO> {
    
    private PSOCrossoverOperation crossoverOperation;
    
    /**
     * Default constructor
     */
    public PSOCrossoverIterationStrategy() {
        this.crossoverOperation = new BoltzmannCrossoverSelection();
    }
    
    /**
     * Copy constructor
     * 
     * @param copy 
     */
    public PSOCrossoverIterationStrategy(PSOCrossoverIterationStrategy copy) {
        this.crossoverOperation = copy.crossoverOperation.getClone();
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
     * 
     * 
     * @param algorithm 
     */
    @Override
    public void performIteration(PSO algorithm) {
        Topology<Particle> topology = algorithm.getTopology();
        
        for (Particle p : topology) {
            p.calculateFitness();
        
            Particle nBest = Topologies.getNeighbourhoodBest(topology, p, new SocialBestFitnessComparator());
            p.setNeighbourhoodBest(nBest);
        }

        for (Particle current : topology) {
            current.updateVelocity();
            current.updatePosition();

            boundaryConstraint.enforce(current);
        }
        
        algorithm.setTopology(crossoverOperation.performCrossoverOpertation(algorithm));
    }

    public void setCrossoverOperation(PSOCrossoverOperation crossoverOperation) {
        this.crossoverOperation = crossoverOperation;
    }
}
