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
package net.sourceforge.cilib.container.visitor;

/**
 * Abstract class defining the general structure of a <tt>Visitor</tt>.
 *
 * @param <E> The type object.
 */
public interface Visitor<E> {

    /**
     * Visit the provided object.
     * @param o The object to visit.
     */
    void visit(E o);

    /**
     * Determine if the visitor has completed its visit operation.
     * @return <code>true</code> if the visit operation is complete,
     *         <code>false</code> otherwise.
     */
    boolean isDone();
}
