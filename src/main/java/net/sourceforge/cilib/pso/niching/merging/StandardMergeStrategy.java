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

import java.util.Collection;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.pso.particle.ParticleBehavior;

/**
 * Takes all the entities of the second sub-swarm, puts them in the first sub-swarm
 * and returns a copy of the combined sub-swarms.
 */
public class StandardMergeStrategy extends MergeStrategy {
    private static final long serialVersionUID = 6790307057694598017L;
    
    @Override
    public PopulationBasedAlgorithm f(PopulationBasedAlgorithm subSwarm1, PopulationBasedAlgorithm subSwarm2) {
        PopulationBasedAlgorithm newSwarm = subSwarm1.getClone();
        newSwarm.getTopology().addAll((Collection) subSwarm2.getClone().getTopology());

        Particle p;
        if (!newSwarm.getTopology().isEmpty() && (p = (Particle) newSwarm.getTopology().get(0)) instanceof Particle) {
            ParticleBehavior pb = p.getParticleBehavior();
            for (Entity e : newSwarm.getTopology()) {
                ((Particle) e).setParticleBehavior(pb);
            }
        }

        return newSwarm;
    }
}
