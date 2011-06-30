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
package net.cilib.entity;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import fj.data.List;
import fj.data.Option;
import net.cilib.collection.Topology;
import net.cilib.pso.PositionProvider;
import net.cilib.pso.VelocityProvider;

/**
 * Factory object to create {@code Particle} instances.
 * @author gpampara
 */
public final class ParticleProvider {

    private final PositionProvider positionProvider;
    private final VelocityProvider velocityProvider;
    private final FitnessProvider fitnessProvider;
    private final FitnessComparator comparator;

    @Inject
    public ParticleProvider(PositionProvider position, VelocityProvider velocity,
            FitnessProvider fitness, FitnessComparator comparator) {
        this.positionProvider = position;
        this.velocityProvider = velocity;
        this.fitnessProvider = fitness;
        this.comparator = comparator;
    }

    /**
     * Base the builder on the provided {@code Particle}. The provided particle
     * seeds the builder with the {@code bestPosition} and {@code fitness} of
     * the given particle.
     * @param previous the {@code Particle} to base the new instance on.
     * @return the current builder instance.
     */
    public BuildableParticleProvider basedOn(Particle previous) {
        Preconditions.checkNotNull(previous);
        return new BuildableParticleProvider(positionProvider, velocityProvider, previous);
    }

    public class BuildableParticleProvider {

        private final VelocityProvider velocityProvider;
        private final PositionProvider positionProvider;
        private Particle previous;

        private BuildableParticleProvider(PositionProvider positionProvider,
                VelocityProvider velocityProvider, Particle previous) {
            this.positionProvider = positionProvider;
            this.velocityProvider = velocityProvider;
            this.previous = previous;
        }

        /**
         * Create a new instance of a {@code Particle}.
         * @return a new {@code Particle} instance.
         * @throws NullPointerException if a required value is not set.
         */
        public Particle get(Topology topology) {
            try {
                final List<Double> velocity = velocityProvider.f(previous, topology);
                final List<Double> position = positionProvider.f(previous.solution(), velocity);

                Preconditions.checkNotNull(previous);
                Preconditions.checkState(previous.memory().isNotEmpty());
                Preconditions.checkState(position.isNotEmpty());
                Preconditions.checkState(velocity.isNotEmpty());

                Option<Double> newFitness = fitnessProvider.evaluate(position);

                return comparator.isMoreFit(newFitness, previous.fitness())
                        ? new Particle(position, position, velocity, newFitness)
                        : new Particle(position, previous.memory(), velocity, newFitness);
            } finally {
                previous = null;
            }
        }
    }
}
