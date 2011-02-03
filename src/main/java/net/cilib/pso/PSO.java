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
import net.cilib.collection.immutable.ImmutableGBestTopology;
import net.cilib.collection.immutable.ImmutableGBestTopology.ImmutableGBestTopologyBuilder;
import net.cilib.entity.CandidateSolution;
import net.cilib.entity.ParticleProvider;
import net.cilib.entity.MutableSeq;
import net.cilib.entity.Particle;
import net.cilib.entity.Velocity;

/**
 * @since 0.8
 * @author gpampara
 */
public class PSO implements PopulationBasedAlgorithm<Particle> {
    private final VelocityProvider velocityProvider;
    private final ParticleProvider particleProvider;

    /**
     *
     * @param velocityProvider {@code Velocity} provider
     * @param particleProvider {@code Particle} provider (finalized particles)
     */
    @Inject
    public PSO(VelocityProvider velocityProvider, ParticleProvider particleProvider) {
        this.velocityProvider = velocityProvider;
        this.particleProvider = particleProvider;
    }

    /**
     *
     * @param topology the population for the algorithm to operate on
     * @return topology of particles
     */
    @Override
    public Topology<Particle> iterate(Topology<Particle> topology) {
        ImmutableGBestTopologyBuilder<Particle> topologyBuilder = ImmutableGBestTopology.newBuilder();
        for (Particle particle : topology) {
            Velocity velocity = velocityProvider.create(particle); // New velocity
            MutableSeq newPosition = particle.solution().toMutableSeq().plus(velocity); // Update position
            Particle updatedParticle = particleProvider.basedOn(particle)
                .position(CandidateSolution.of(newPosition.toArray()))
                .velocity(velocity)
                .get();
            topologyBuilder.add(updatedParticle);
        }
        return topologyBuilder.build();
    }
}
