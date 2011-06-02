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
import net.cilib.inject.annotation.Global;
import net.cilib.inject.annotation.Local;

import javax.inject.Provider;

/**
 * Particle Swarm Optimizer implementing the asynchronous update strategy.
 *
 * @author gpampara
 * @since 0.8
 */
public class ASyncPSO extends PopulationBasedAlgorithm<Particle> {
    private final VelocityProvider velocityProvider;
    private final ParticleProvider particleProvider;
    private final Provider<Guide> localGuide;
    private final Provider<Guide> globalGuide;

    /**
     * @param velocityProvider {@code Velocity} provider
     * @param particleProvider {@code Particle} provider (finalized particles)
     * @param globalGuide      {@code Provider} for global guides.
     * @param localGuide       {@code Provider} for local guides.
     */
    @Inject
    public ASyncPSO(VelocityProvider velocityProvider, ParticleProvider particleProvider,
                    @Global Provider<Guide> globalGuide, @Local Provider<Guide> localGuide) {
        this.velocityProvider = velocityProvider;
        this.particleProvider = particleProvider;
        this.localGuide = localGuide;
        this.globalGuide = globalGuide;
    }

    /**
     * @param topology the population for the algorithm to operate on
     * @return topology of particles
     */
    @Override
    public Topology<Particle> next(Topology<Particle> topology) {
        TopologyBuffer<Particle> topologyBuilder = topology.newBuffer();
        for (Particle particle : topology) { // This should be Particle p = topology.drop(1)
            /*
             In the async pso is similar to the sync pso, except that the
             neighbourhood best may also be from the currently forming topology.
             As a result, the new and previous topologies need to be merged.
            */
            // @TODO: This needs to be tested, the drop + current partial topology might be too slow?
            Topology<Particle> partial = topology.drop(topology.indexOf(particle)).newBuffer().addAll(topologyBuilder).build();
            Entity global = globalGuide.get().f(particle, partial).some();
            Entity local = localGuide.get().f(particle, topology).some();

            Velocity velocity = velocityProvider.create(particle, local, global); // New velocity
            MutableSeq newPosition = particle.solution().toMutableSeq().plus(velocity); // Update position
            Particle updatedParticle = particleProvider.basedOn(particle)
                    .position(CandidateSolution.copyOf(newPosition))
                    .velocity(velocity)
                    .get();
            topologyBuilder.add(updatedParticle);
        }
        return topologyBuilder.build();
    }
}
