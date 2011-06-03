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
import fj.data.Option;
import static com.google.common.base.Preconditions.checkNotNull;
import com.google.inject.Inject;
import net.cilib.collection.immutable.CandidateSolution;
import net.cilib.collection.immutable.Velocity;

/**
 * Representation of a {@code Particle}. A {@code Particle} is an {@code Entity}
 * that maintains a {@linkplain CandidateSolution current position},
 * {@linkplain Velocity velocity}, {@linkplain Fitness fitness} and also has a
 * memory to record the best observed {@linkplain CandidateSolution position}.
 * @author gpampara
 */
public final class Particle implements Entity, HasVelocity, HasMemory {
    private final CandidateSolution position;
    private final CandidateSolution bestPosition;
    private final Velocity velocity;
    private final Option<Double> fitness;

    /**
     * Create a new {@code Particle}.
     * @param position the current position.
     * @param bestPosition the best position observed.
     * @param velocity the current velocity.
     * @param fitness the fitness of the current position.
     */
    @Inject
    public Particle(CandidateSolution position,
            CandidateSolution bestPosition,
            Velocity velocity,
            Option<Double> fitness) {
        Preconditions.checkArgument(position.size() == velocity.size());
        Preconditions.checkArgument(position.size() == bestPosition.size());
        this.position = checkNotNull(position);
        this.bestPosition = checkNotNull(bestPosition);
        this.velocity = checkNotNull(velocity);
        this.fitness = checkNotNull(fitness);
    }

    /**
     * Gets the current position for the {@code Particle}.
     * {@inheritDoc}
     */
    @Override
    public CandidateSolution solution() {
        return position;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return position.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Option<Double> fitness() {
        return fitness;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Velocity velocity() {
        return velocity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CandidateSolution memory() {
        return bestPosition;
    }

    /**
     * Create a {@code Particle} instance, with the provided
     * {@code CandidateSolution}. An initial {@code Particle}
     * contains a best-position that is the same as the provided position,
     * a zeroed velocity and an inferior fitness.
     * @param initialSolution the initial position.
     * @return a new {@code Particle} instance.
     *
     * TODO: This API doesn't seem really clean - feels like a backdoor???
     */
    public static Particle create(CandidateSolution initialSolution) {
        return new Particle(initialSolution, initialSolution,
                Velocity.fill(0.0, initialSolution.size()),
                Option.<Double>none());
    }
}
