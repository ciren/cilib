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

import fj.data.Option;
import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.base.Objects;
import com.google.inject.Inject;

/**
 * Basic individual class.
 * <p/>
 * The individual is an {@link Entity} instances that maintains a single
 * {@linkplain CandidateSolution solution} and {@linkplain Fitness fitness}.
 *
 * @author gpampara
 * @since 0.8
 */
public final class Individual implements Entity {
    private final CandidateSolution solution;
    private final Option<Double> fitness;

    @Inject
    public Individual(CandidateSolution solution, Option<Double> fitness) {
        this.solution = checkNotNull(solution);
        this.fitness = checkNotNull(fitness);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CandidateSolution solution() {
        return solution;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return solution.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Option<Double> fitness() {
        return fitness;
    }

    /**
     * Get the {@code String} representation of the current {@code Individual}.
     *
     * @return the {@code String} representation.
     */
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("solution", solution)
                .add("fitness", fitness)
                .toString();
    }
}
