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

import static com.google.common.base.Preconditions.checkNotNull;
import com.google.common.base.Objects;
import com.google.inject.Inject;
import java.util.Comparator;
import static net.cilib.entity.EntityComparators.FITNESS_COMPARATOR;

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
    private final Fitness fitness;

    @Inject
    public Individual(CandidateSolution solution, Fitness fitness) {
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
    public Fitness fitness() {
        return fitness;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equiv(Entity that) {
        // the only check we can do it that the fitnesses are similar.
        return this.fitness.equals(that.fitness());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Entity moreFit(Entity that) {
        return moreFit(that, FITNESS_COMPARATOR);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Entity moreFit(Entity that, Comparator<? super Entity> comparator) {
        return (comparator.compare(this, that) >= 0) ? this : that;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isMoreFit(Entity than) {
        return moreFit(than) == this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Entity lessFit(Entity that) {
        return lessFit(that, FITNESS_COMPARATOR);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Entity lessFit(Entity that, Comparator<? super Entity> comparator) {
        return (comparator.compare(this, that) < 0) ? this : that;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLessFit(Entity than) {
        return lessFit(than) == this;
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
