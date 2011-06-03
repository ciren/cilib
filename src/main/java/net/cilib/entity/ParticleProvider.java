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
import com.google.inject.Provider;
import fj.Ord;
import fj.Ordering;
import fj.data.Option;
import net.cilib.collection.immutable.CandidateSolution;
import net.cilib.collection.immutable.Velocity;

/**
 * Factory object to create {@code Particle} instances.
 * @author gpampara
 */
public final class ParticleProvider implements Provider<Particle> {

    private CandidateSolution position = CandidateSolution.empty();
    private Velocity velocity = Velocity.empty();
    private CandidateSolution previousBest = CandidateSolution.empty();
    private Option<Double> previousFitness = Option.none();
    private FitnessProvider fitnessProvider;

    /**
     * Base the builder on the provided {@code Particle}. The provided particle
     * seeds the builder with the {@code bestPosition} and {@code fitness} of
     * the given particle.
     * @param previous the {@code Particle} to base the new instance on.
     * @return the current builder instance.
     */
    public ParticleProvider basedOn(Particle previous) {
        Preconditions.checkNotNull(previous);
        this.previousBest = previous.memory();
        this.previousFitness = previous.fitness();
        return this;
    }

    /**
     * Define the {@linkplain CandidateSolution position} for the new
     * {@code Particle} instance.
     * @param position the {@code CandidateSolution} to use
     * @return the current factory instance.
     */
    public ParticleProvider position(CandidateSolution position) {
        Preconditions.checkNotNull(position);
        this.position = CandidateSolution.copyOf(position);
        return this;
    }

    /**
     * Provide a velocity. The provided velocity will be used to create the
     * new {@code Particle} instance.
     * @param velocity velocity for the created {@code Particle} instance.
     * @return the current factory instance.
     */
    public ParticleProvider velocity(Velocity velocity) {
        Preconditions.checkNotNull(velocity);
        this.velocity = Velocity.copyOf(velocity.toArray());
        return this;
    }

    /**
     * Create a new instance of a {@code Particle}.
     * @return a new {@code Particle} instance.
     * @throws NullPointerException if a required value is not set.
     */
    @Override
    public Particle get() {
        Preconditions.checkState(position != CandidateSolution.empty());
        Preconditions.checkState(velocity != Velocity.empty());
        Preconditions.checkState(previousBest != CandidateSolution.empty());
        Preconditions.checkState(!previousFitness.isNone());

        // Should this be done with DI somehow?
        try {
            Option<Double> newFitness = fitnessProvider.evaluate(position);
            if (compare(newFitness, previousFitness) < 0) {
                return new Particle(position, position, velocity, newFitness);
            } else {
                return new Particle(position, previousBest, velocity, newFitness);
            }
        } finally {
            position = CandidateSolution.empty();
            velocity = Velocity.empty();
            previousBest = CandidateSolution.empty();
            previousFitness = Option.none();
        }
    }

    private int compare(Option<Double> newFitness, Option<Double> previousFitness) {
        return Ord.optionOrd(Ord.doubleOrd).compare(newFitness, previousFitness) == Ordering.GT ? 0 : 1;
    }

    public ParticleProvider fitness(FitnessProvider provider) {
        this.fitnessProvider = provider;
        return this;
    }
}
