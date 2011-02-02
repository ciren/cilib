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
 * Comparisons for {@code Entity} instances.
 * @author gpampara
 */
public interface EntityComparable {
    /**
     * Obtain the {@code Entity} that is the more fit between the current
     * and provided instances.
     * @param that {@code Entity} to test.
     * @return the fitter entity.
     */
    Entity moreFit(Entity that);

    /**
     * Obtain the {@code Entity} that is the more fit between the current
     * and provided instances, based on the given {@code Comparator}.
     * @param that {@code Entity} to test.
     * @param comparator method of comparison.
     * @return the fitter entity.
     */
    Entity moreFit(Entity that, Comparator<? super Entity> comparator);

    /**
     * Test if the current {@code Entity} is, in fact fitter. It is advised that
     * this method only be used when the comparison is required, otherwise
     * {@link EntityComparable#moreFit(net.cilib.entity.Entity)} or
     * {@link EntityComparable#moreFit(net.cilib.entity.Entity, java.util.Comparator)}
     * should be used.
     * @param than {@code Entity} to test
     * @return {@code true} if the current {@code Entity} is fitter,
     *         {@code false} otherwise.
     */
    boolean isMoreFit(Entity than);

    /**
     * Obtain the {@code Entity} that is the least fit between the current
     * and provided instances.
     * @param that {@code Entity} to test.
     * @return the least fit entity.
     */
    Entity lessFit(Entity that);

    /**
     * Obtain the {@code Entity} that is the least fit between the current
     * and provided instances, based on the given {@code Comparator}.
     * @param that {@code Entity} to test.
     * @param comparator method of comparison.
     * @return the least fit entity.
     */
    Entity lessFit(Entity that, Comparator<? super Entity> comparator);

    /**
     * Test if the current {@code Entity} is, in fact less fit. It is advised that
     * this method only be used when the comparison is required, otherwise
     * {@link EntityComparable#lessFit(net.cilib.entity.Entity)} or
     * {@link EntityComparable#lessFit(net.cilib.entity.Entity, java.util.Comparator)}
     * should be used.
     * @param than {@code Entity} to test
     * @return {@code true} if the current {@code Entity} is less fit,
     *         {@code false} otherwise.
     */
    boolean isLessFit(Entity than);

    /**
     * Defining an equivalency between {@link Entity entities}. This is
     * different from {@link Object#equals(java.lang.Object)} by indicating
     * a semantically different concept.
     * <p/>
     * For example, two {@link Entity} instances may be equivalent, but they
     * may not necessarily be equal in a strict sense.
     * Such an example would be: two different entity instances, located at
     * different locations within the search space yet have the same fitness.
     * There is no direct reason that would indicate that one is better than
     * the other (without additional inspection), the only conclusion that
     * can be made is that both entities are equivalent to each other.
     *
     * @param that the {@code Entity} to test equivalency against
     * @return {@code true} if the instances are equivalent, {@code false}
     *         otherwise.
     */
    boolean equiv(Entity that);
}
