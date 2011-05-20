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
package net.cilib.matchers;

import fj.F;
import fj.data.Option;
import net.cilib.collection.Topology;
import net.cilib.entity.Entities;
import net.cilib.entity.Entity;
import net.cilib.entity.FitnessComparator;

/**
 * A collection of matchers based on Entity instances.
 */
public final class EntityMatchers {

    private EntityMatchers() {
        throw new UnsupportedOperationException();
    }

    /**
     * Return the most fit {@code HasFitness} instance contained within the
     * provided {@code Topology}.
     *
     * @param topology   the container of elements to compare.
     * @param comparator the comparison method.
     * @return an {@code Option} containing the most fit instance, or
     *         {@code none}.
     */
    public static <A extends Entity> Option<A> mostFit(final Topology<A> topology, final FitnessComparator comparator) {
        A result = topology.foldLeft(new F<A, F<A, A>>() {
            @Override
            public F<A, A> f(final A x) {
                return new F<A, A>() {
                    @Override
                    public A f(final A y) {
                        return comparator.moreFit(x, y);
                    }
                };
            }
        }, Entities.<A>dummy());

        return (result == Entities.dummy()) ? Option.<A>none() : Option.some(result);
    }
}
