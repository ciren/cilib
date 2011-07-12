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

import com.google.common.base.Objects;
import fj.data.Option;
import static com.google.common.base.Preconditions.checkNotNull;
import com.google.inject.Inject;
import fj.data.List;

/**
 * Representation of a {@code Particle}. A {@code Particle} is an {@code Entity}
 * that maintains a current position}, velocity, fitness and also has a
 * memory to record the best observed position.
 * @author gpampara
 */
public final class Particle implements Entity, HasVelocity, HasMemory {
    private final List<Double> position;
    private final List<Double> bestPosition;
    private final List<Double> velocity;
    private final Option<Double> fitness;

    /**
     * Create a new {@code Particle}.
     * @param position the current position.
     * @param bestPosition the best position observed.
     * @param velocity the current velocity.
     * @param fitness the fitness of the current position.
     */
    @Inject
    public Particle(List<Double> position,
            List<Double> bestPosition,
            List<Double> velocity,
            Option<Double> fitness) {
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
    public List<Double> solution() {
        return position;
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
    public List<Double> velocity() {
        return velocity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Double> memory() {
        return bestPosition;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("position", position)
                .add("bestPosition", bestPosition)
                .add("velocity", velocity)
                .add("fitness", fitness)
                .toString();
    }
}
