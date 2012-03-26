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

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import net.sourceforge.cilib.container.visitor.Visitor;
import net.sourceforge.cilib.entity.comparator.AscendingFitnessComparator;
import net.sourceforge.cilib.entity.visitor.TopologyVisitor;

/**
 * This an abstract class which extends from the abstract Topology class.
 * All {@linkplain net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm}
 * Topologies must inherit from this class.
 *
 * @param <E> The {@code Entity} type.
 */
public abstract class AbstractTopology<E extends Entity> implements Topology<E> {
    private static final long serialVersionUID = -9117512234439769226L;

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract Topology<E> getClone();

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(Visitor<E> visitor) {
        for (E element : this) {
            if (visitor.isDone())
                return;

            visitor.visit(element);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(TopologyVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Obtain the most fit {@link Entity} within the {@code Topology}. This is
     * the same as {@code getBestEntity(Comparator)} with a {@link AscendingFitnessComparator}
     * as the provided comparator.
     * @see AbstractTopology#getBestEntity(java.util.Comparator)
     * @return The current best {@linkplain Entity}.
     */
    @Override
    public E getBestEntity() {
        return getBestEntity(new AscendingFitnessComparator<E>());
    }

    /**
     * Obtain the {@link Entity} within the current {@code Topology}, based
     * on the provided {@link Comparator} instance.
     * @param comparator The {@link Comparator} to base the selection on.
     * @return The best entity within the current topology.
     */
    @Override
    public E getBestEntity(Comparator<? super E> comparator) {
        E bestEntity = null;
        Iterator<E> i = this.iterator();

        while (i.hasNext()) {
            E entity = i.next();
            if (bestEntity == null) {
                bestEntity = entity;
                continue;
            }

            if (comparator.compare(bestEntity, entity) < 0) { // bestEntity is worse than entity
                bestEntity = entity;
            }
        }

        return bestEntity;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        Iterator<?> e = c.iterator();
        while (e.hasNext()) {
            if (!c.contains(e.next()))
                return false;
        }
        return true;
    }
}
