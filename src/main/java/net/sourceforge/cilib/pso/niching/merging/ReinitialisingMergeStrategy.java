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
package net.sourceforge.cilib.pso.niching.merging;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;

/**
 * Takes all the entities of the second sub-swarm, reinitializes those entities
 * in the first sub-swarm and returns the merged sub-swarm.
 * 
 * @author wayne
 * @author filipe
 */
public class ReinitialisingMergeStrategy extends MergeStrategy {    
    @Override
    public PopulationBasedAlgorithm f(PopulationBasedAlgorithm subSwarm1, PopulationBasedAlgorithm subSwarm2) {
        PopulationBasedAlgorithm newSwarm = new StandardMergeStrategy().f(subSwarm1, subSwarm2);
        
        for (int i = subSwarm2.getTopology().size(); i < newSwarm.getTopology().size(); i++) {
            newSwarm.getTopology().get(i).reinitialise();
            newSwarm.getTopology().get(i).calculateFitness();
        }

        return newSwarm;
    }
}
