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
package net.cilib.pso;

import com.google.inject.Inject;
import net.cilib.algorithm.PopulationBasedAlgorithm;
import net.cilib.collection.Topology;
import net.cilib.collection.TopologyBuffer;
import net.cilib.entity.*;

/**
 * Particle Swarm Optimizer, implementing the synchronous update strategy.
 * @author gpampara
 * @since 0.8
 */
public class PSO extends PopulationBasedAlgorithm<Particle> {

    private final ParticleProvider particleProvider;

    @Inject
    public PSO(final ParticleProvider particleProvider) {
        this.particleProvider = particleProvider;
    }

    /**
     * @param topology the population for the algorithm to operate on
     * @return topology of particles
     *
     * All particles had had their fitness already updated.
     *
     * TODO: Need to verify the neighourhood best selection.
     *
     * TODO: This is nothing more than a simple function that you can map() over
     * the current topology to create the next topology for the process.
     */
    @Override
    public Topology<Particle> next(final Topology<Particle> topology) {
//        return topology.map(particleProvider.basedOn(particle).get(topology));
        final TopologyBuffer<Particle> topologyBuilder = topology.newBuffer();
        for (Particle particle : topology) {
            topologyBuilder.add(particleProvider.basedOn(particle).get(topology));
        }
        return topologyBuilder.build();
    }
}
