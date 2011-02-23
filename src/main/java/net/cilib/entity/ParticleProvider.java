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
import com.google.inject.Provider;
import fj.Ord;
import fj.Ordering;
import fj.data.Option;

/**
 * Factory object to create {@code Particle} instances.
 * @author gpampara
 */
public final class ParticleProvider implements Provider<Particle> {

    private final FitnessProvider fitnessProvider;
    private CandidateSolution position;
    private Velocity velocity;
    private CandidateSolution previousBest;
    private Option<Double> previousFitness;

    @Inject
    public ParticleProvider(FitnessProvider fitnessProvider) {
        this.fitnessProvider = fitnessProvider;
    }

    /**
     * @todo does this API make sense? Should it not be somewhere else?
     * @param solution
     * @return
     */
    public Particle newParticle(CandidateSolution solution) {
        return new Particle(solution, solution,
                Velocity.fill(0.0, solution.size()),
                fitnessProvider.finalize(solution));
    }

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
        Preconditions.checkNotNull(position);
        Preconditions.checkNotNull(velocity);
        Preconditions.checkNotNull(previousBest);
        Preconditions.checkNotNull(previousFitness);

        // Should this be done with DI somehow?
        try {
            Option<Double> newFitness = fitnessProvider.finalize(position);
            if (compare(newFitness, previousFitness) < 0) {
                return new Particle(position, position, velocity, newFitness);
            } else {
                return new Particle(position, previousBest, velocity, newFitness);
            }
        } finally {
            position = null;
            velocity = null;
            previousBest = null;
            previousFitness = null;
        }
    }

    private int compare(Option<Double> newFitness, Option<Double> previousFitness) {
        return Ord.optionOrd(Ord.doubleOrd).compare(newFitness, previousFitness) == Ordering.GT ? 0 : 1;
    }
}
