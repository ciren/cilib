/*
 * Copyright (C) 2003 - 2008
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.entity;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.cilib.container.visitor.Visitor;
import net.sourceforge.cilib.entity.comparator.AscendingFitnessComparator;
import net.sourceforge.cilib.entity.visitor.TopologyVisitor;
import net.sourceforge.cilib.problem.Fitness;

/**
 * This an abstract class which extends from the abstract Topology class.
 * All {@linkplain net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm}
 * Topologies must inherit from this class.
 *
 * @author Gary Pampara
 * @author otter
 * @param <E> The {@code Entity} type.
 */
public abstract class Topology<E extends Entity> implements EntityCollection<E> {
    private static final long serialVersionUID = -9117512234439769226L;

    private E bestEntity;

    /**
     * {@inheritDoc}
     */
    public abstract Topology<E> getClone();

    /**
     * Returns an <code>Iterator</code> over all particles in the neighbourhood of
     * the particle referred to by the given <code>Iterator</code>.
     *
     * @param An iterator that refers to a particle in this topology.
     * @return A particle iterator.
     */
    public abstract Iterator<E> neighbourhood(Iterator<? extends Entity> iterator);


    /**
     * Accept a vistitor and perform the visitor actions on this
     * <tt>Topology</tt>.
     *
     * @param visitor The {@see net.sourceforge.cilib.container.visitor.Visitor} to accept
     */
    public void accept(Visitor<E> visitor) {
        for (E element : this) {
            if (visitor.isDone())
                return;

            visitor.visit(element);
        }
    }

    /**
     * Accept a {@code TopologyVisitor} into the {@code Topology} to perform the actions
     * defined within the {@code TopologyVisitor}.
     * @param visitor The instance to accept into the {@code Topology}.
     */
    public void accept(TopologyVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Get all the entities within the topology.
     * @return Collection. Data collection of all the entities
     */
    public abstract List<E> asList();


    /**
     * Mainly for use in Co-Evolution, CCGA, Island GA and so on... where there is multiple populations.
     * Returns the topology identifier.
     *
     * @return A <tt>String</tt> representing the identifier.
     */
    public abstract String getId();


    /**
     * Set the identifier for this <tt>Topology</tt>.
     *
     * @param id The identifier to set
     */
    public abstract void setId(String id);

    /**
     * Obtain the most fit {@link Entity} within the {@code Topology}. This is
     * the same as {@code getBestEntity(Comparator)} with a {@link AscendingFitnessComparator}
     * as the provided comparator.
     * @see Topology#getBestEntity(java.util.Comparator)
     * @return The current best {@linkplain Entity}.
     */
    public E getBestEntity() {
        return getBestEntity(new AscendingFitnessComparator());
        /*if (bestEntity == null) {
            Iterator<E> i = this.iterator();
            bestEntity = i.next();
            Fitness bestFitness = bestEntity.getSocialBestFitness();
            while (i.hasNext()) {
                E entity = i.next();
                if (entity.getSocialBestFitness().compareTo(bestFitness) > 0) {
                    bestEntity = entity;
                    bestFitness = bestEntity.getSocialBestFitness();
                }
            }
        }

        return bestEntity;*/
    }

    /**
     * Obtain the {@link Entity} within the current {@code Topology}, based
     * on the provided {@link Comparator} instance.
     * @param comparator The {@link Comparator} to base the selection on.
     * @return The best entity within the current topology.
     */
    public E getBestEntity(Comparator<? super E> comparator) {
        if (bestEntity == null) {
            Iterator<E> i = this.iterator();
            bestEntity = i.next();

            while (i.hasNext()) {
                E entity = i.next();
                if (comparator.compare(bestEntity, entity) < 0) { // bestEntity is worse than entity
                    bestEntity = entity;
                }
            }
        }

        return bestEntity;
    }

    /**
     * Gather the best entity of each neighbourhood (in this {@link Topology}) in a
     * {@link Set} (duplicates are not allowed) and return them. A single {@link Entity} may
     * dominate in more than one neighbourhood, but we just want unique entities.
     *
     * @return a {@link Set} cosisting of the best entity of each neighbourhood in the
     *         topology
     * @author Theuns Cloete
     */
    public Set<E> getNeighbourhoodBestEntities() {
        // a Set does not allow duplicates
        Set<E> neighbourhoodBests = new HashSet<E>(this.size());
        Iterator<E> topologyIterator = this.iterator();

        // iterate over all entities in the topology
        while (topologyIterator.hasNext()) {
            topologyIterator.next();
            Iterator<E> neighbourhoodIterator = this.neighbourhood(topologyIterator);
            E bestEntity = null;

            // iterate over the neighbours of the current entity
            while (neighbourhoodIterator.hasNext()) {
                E anotherEntity = neighbourhoodIterator.next();
                // keep track of the best entity
                if (bestEntity == null || bestEntity.compareTo(anotherEntity) > 0) {
                    bestEntity = anotherEntity;
                }
            }
            // only gather unique entities
            if (bestEntity != null) {
                neighbourhoodBests.add(bestEntity);
            }
        }

        return neighbourhoodBests;
    }

    /**
     * Clear the current best entity from the topology, thereby forcing a
     * re-calculation of the best {@linkplain Entity} within the topology.
     */
    public void clearBestEntity() {
        this.bestEntity = null;
    }

    /**
     * Perform any required updates to the {@linkplain Topology} instance.
     * The method in has an empty implementation and needs to be overridden
     * within the required subclass.
     */
    public void update() {
    }
}
