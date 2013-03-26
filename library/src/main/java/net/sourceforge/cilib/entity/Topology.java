/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity;

import java.util.Collection;
import java.util.List;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.visitor.TopologyVisitor;
import net.sourceforge.cilib.util.Cloneable;

/**
 * This is a generalisation for all algorithms that maintain a collection of
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
     * Accept a {@code TopologyVisitor} into the {@code Topology} to perform the actions
     * defined within the {@code TopologyVisitor}.
     * @param visitor The instance to accept into the {@code Topology}.
     */
    void accept(TopologyVisitor visitor);

    /**
     * Returns an iterator over all entities in the neighbourhood of
     * the particle referred to by the given iterator.
     *
     * @param e An iterator that refers to a particle in this topology.
     * @return A particle iterator.
     */
    Collection<E> neighbourhood(final E e);

    /**
     * Accessor for the number of entities in a neighbourhood. NOTE: This method should
     * return the value of the {@linkplain ControlParameter} rounded to the nearest integer.
     *
     * @return The size of the neighbourhood.
     */
    int getNeighbourhoodSize();

    /**
     * Sets the {@linkplain ControlParameter} that should be used to determine the
     * number of entities in the neighbourhood of each entity.
     *
     * @param neighbourhoodSize The {@linkplain ControlParameter} to use.
     */
    void setNeighbourhoodSize(ControlParameter neighbourhoodSize);
}
