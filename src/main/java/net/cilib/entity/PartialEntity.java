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
import java.util.Comparator;

/**
 * A partial entity is an entity that has, at least, a candidate solution. No
 * guarantees are made regarding the content of this object. This object is
 * mainly required in cases where an adapter class is necessitated.
 *
 * @author gpampara
 */
public final class PartialEntity implements Entity {

    private final CandidateSolution solution;

    public PartialEntity(CandidateSolution solution) {
        this.solution = checkNotNull(solution);
    }

    /**
     * Get the current {@code CandidateSolution}.
     * @return the {@code CandidateSolution}.
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
     * The current fitness of the {@code PartialEntity} is always
     * {@linkplain Fitnesses#inferior() inferior}.
     * @return An inferior fitness.
     */
    @Override
    public Fitness fitness() {
        return Fitnesses.inferior();
    }

    /**
     * This method always returns the provided {@code Entity}.
     * {@inheritDoc}
     */
    @Override
    public Entity moreFit(Entity that) {
        return that;
    }

    /**
     * This method always returns the provided {@code Entity}.
     * {@inheritDoc}
     * @param comparator for comparisons.
     */
    @Override
    public Entity moreFit(Entity that, Comparator<? super Entity> comparator) {
        return that;
    }

    /**
     * Always returns {@code false}.
     * {@inheritDoc}
     */
    @Override
    public boolean isMoreFit(Entity than) {
        return false;
    }

    /**
     * This method always returns the provided {@code Entity}.
     * {@inheritDoc}
     */
    @Override
    public Entity lessFit(Entity that) {
        return that;
    }

    /**
     * This method always returns the provided {@code Entity}.
     * {@inheritDoc}
     */
    @Override
    public Entity lessFit(Entity that, Comparator<? super Entity> comparator) {
        return that;
    }

    /**
     * Always returns {@code true}.
     * {@inheritDoc}
     */
    @Override
    public boolean isLessFit(Entity than) {
        return true;
    }

    /**
     * Always returns {@code false}.
     * {@inheritDoc}
     */
    @Override
    public boolean equiv(Entity that) {
        return false;
    }
}
