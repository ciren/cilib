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
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * A merge detection strategy which indicates swarms can merge if they overlap
 * and if they are moving in the same direction.
 * 
 * @author wayne
 * @author filipe
 */
public class DirectionalBasedMergeDetection extends MergeDetection {

    private MergeDetection mergeDetector;

    /**
     * Default constructor.
     */
    public DirectionalBasedMergeDetection() {
        this.mergeDetector = new RadiusOverlapMergeDetection();
    }

    /**
     * Only sub-swarms moving towards the same niche will be merged into one if
     * the areas they cover in search space overlap.
     * 
     * @param swarm1 The first subSwarm
     * @param swarm2 The second subSwarm
     * @return True if the swarms overlap and are moving in the same direction,
     * false otherwise.
     */
    @Override
    public Boolean f(PopulationBasedAlgorithm swarm1, PopulationBasedAlgorithm swarm2) {
        Particle swarm1Best = (Particle) swarm1.getTopology().getBestEntity();
        Particle swarm2Best = (Particle) swarm2.getTopology().getBestEntity();
        
        Vector velocity1 = ((Vector) swarm1Best.getVelocity()).normalize();
        Vector velocity2 = ((Vector) swarm2Best.getVelocity()).normalize();
        
        double direction = velocity1.dot(velocity2);
        
        return direction < 0 && mergeDetector.f(swarm1, swarm2);
    }

    /**
     * Sets the merge detection strategy to use in combination with this one.
     * Mainly used to change parameters of the radius overlap of sub-swarms.
     * 
     * @param intersectionDetector 
     */
    public void setMergeDetector(MergeDetection mergeDetector) {
        this.mergeDetector = mergeDetector;
    }

    /**
     * Gets the merge detection strategy used with this one.
     * 
     * @return 
     */
    public MergeDetection getMergeDetector() {
        return mergeDetector;
    }
}
