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
import net.cilib.entity.*;

import net.cilib.collection.TopologyBuffer;

/**
 * Particle Swarm Optimizer implementing the asynchronous update strategy.
 *
 * @author gpampara
 * @since 0.8
 */
public class ASyncPSO extends PopulationBasedAlgorithm<Particle> {

    private final ParticleProvider particleProvider;

    /**
     * @param velocityProvider {@code Velocity} provider
     * @param particleProvider {@code Particle} provider (finalized particles)
     * @param globalGuide      {@code Provider} for global guides.
     * @param localGuide       {@code Provider} for local guides.
     */
    @Inject
    public ASyncPSO(ParticleProvider particleProvider) {
        this.particleProvider = particleProvider;
    }

    /**
     * @param topology the population for the algorithm to operate on
     * @return topology of particles
     */
    @Override
    public Topology<Particle> next(Topology<Particle> topology) {
        final TopologyBuffer<Particle> topologyBuilder = topology.newBuffer();
        for (Particle particle : topology) {
            Topology<Particle> partial = topology.newBuffer()
                    .addAll(topology.drop(topology.indexOf(particle)))
                    .addAll(topologyBuilder)
                    .build();
            topologyBuilder.add(particleProvider.basedOn(particle).get(partial));
        }
        return topologyBuilder.build();
    }
}
