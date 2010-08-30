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

/**
 * Defining an equivalency between instances. This interface is
 * different from {@link Object#equals(java.lang.Object)} by indicating
 * a semantically different concept.
 * <p>
 * For example, two {@link Entity} instances may be equivalent, but they
 * may not necessarily be equal.
 * @author gpampara
 */
public interface Equiv<A> extends Comparable<A> {

    /**
     * Determine if the current instance and the given instance are
     * equivalent.
     * @param that the {@code Entity} to test equivalency against
     * @return {@code true} if the instances are equivalent, {@code false}
     * otherwise.
     */
    boolean equiv(A that);
}
