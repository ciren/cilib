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
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.comparator.SocialBestFitnessComparator;

/**
 * Takes all the entities of the first sub-swarm, puts them in the second sub-swarm
 * and returns a copy of that sub-swarm.
 */
public class StandardMergeStrategy extends MergeStrategy {
    private static final long serialVersionUID = 6790307057694598017L;
    
    @Override
    public PopulationBasedAlgorithm f(PopulationBasedAlgorithm subSwarm1, PopulationBasedAlgorithm subSwarm2) {
        PopulationBasedAlgorithm newSwarm = new SingleSwarmMergeStrategy().f(subSwarm1, subSwarm2);
        Particle neighbourhoodBest = (Particle) Topologies.getBestEntity(newSwarm.getTopology(), new SocialBestFitnessComparator());

        for (Entity e : subSwarm2.getTopology()) {
            Particle p = (Particle) e.getClone();
            ((Topology<Particle>) newSwarm.getTopology()).add(p);
        }
        
        if (neighbourhoodBest == null) {
            neighbourhoodBest = (Particle) Topologies.getBestEntity(newSwarm.getTopology(), new SocialBestFitnessComparator());
        }

        for (Entity e : newSwarm.getTopology()) {
            Particle p = (Particle) e;
            p.setNeighbourhoodBest(neighbourhoodBest);
            p.setParticleBehavior(neighbourhoodBest.getParticleBehavior());
        }

        return newSwarm;
    }
}
