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
        this.solution = solution;
    }

    @Override
    public CandidateSolution solution() {
        return solution;
    }

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

    @Override
    public Entity moreFit(Entity that) {
        return that;
    }

    @Override
    public Entity moreFit(Entity that, Comparator<? super Entity> comparator) {
        return that;
    }

    @Override
    public boolean isMoreFit(Entity than) {
        return false;
    }

    @Override
    public Entity lessFit(Entity that) {
        return that;
    }

    @Override
    public Entity lessFit(Entity that, Comparator<? super Entity> comparator) {
        return that;
    }

    @Override
    public boolean isLessFit(Entity than) {
        return true;
    }

    @Override
    public boolean equiv(Entity that) {
        return false;
    }

    /**
     * Compare a {@code PartialEntity} to the provided {@code Entity}.
     * The provided entity is <b>always</b> better.
     * @param o provided entity
     * @return {@code -1} always
     */
    @Override
    public int compareTo(Entity o) {
        return -1;
    }
}
