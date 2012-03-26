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
package net.sourceforge.cilib.entity;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import net.sourceforge.cilib.container.visitor.Visitor;
import net.sourceforge.cilib.entity.visitor.TopologyVisitor;
import net.sourceforge.cilib.util.Cloneable;

/**
 * This is a generalization for all algorithms that maintain a collection of
 * {@linkplain Entity} objects. Examples of this would include PSO, EC and ACO.
 *
 * @param <E> All types derived from {@linkplain Entity}.
 */
public interface Topology<E extends Entity> extends List<E>, Cloneable {

    /**
     * {@inheritDoc}
     */
    @Override
    Topology<E> getClone();

    /**
     * Get the <code>id</code> associated with this {@linkplain Topology}, if
     * an id is defined.
     * @return The <code>id</code> for this {@linkplain Topology}.
     */
    String getId();

    /**
     * Set the <code>id</code> for this {@linkplain Topology}.
     * @param id The value to set.
     */
    void setId(String id);

    /**
     * Obtain the current best entity within the {@code Topology}.
     * @return The best {@code Entity}.
     */
    E getBestEntity();

    /**
     * Obtain the current best entity within the {@code Topology}, based
     * on the provided {@code Comparator}.
     * @param comparator The {@code Comparator} to use.
     * @return The best {@code Entity} based on the defined comparison.
     */
    E getBestEntity(Comparator<? super E> comparator);

    /**
     * Accept a vistitor and perform the visitor actions on this
     * <tt>Topology</tt>.
     *
     * @param visitor The {@see net.sourceforge.cilib.container.visitor.Visitor} to accept
     */
    void accept(Visitor<E> visitor);

    /**
     * Accept a {@code TopologyVisitor} into the {@code Topology} to perform the actions
     * defined within the {@code TopologyVisitor}.
     * @param visitor The instance to accept into the {@code Topology}.
     */
    void accept(TopologyVisitor visitor);

    /**
     * Returns an <code>Iterator</code> over all particles in the neighbourhood of
     * the particle referred to by the given <code>Iterator</code>.
     *
     * @param iterator An iterator that refers to a particle in this topology.
     * @return A particle iterator.
     */
    Iterator<E> neighbourhood(Iterator<? extends Entity> iterator);

}
