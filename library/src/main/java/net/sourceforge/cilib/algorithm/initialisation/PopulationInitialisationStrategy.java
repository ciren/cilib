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
package net.sourceforge.cilib.algorithm.initialisation;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.util.Cloneable;

/**
 * Interface describing the manner in which populations are initialised.
 * @param <E> The {@code Entity} type.
 */
public interface PopulationInitialisationStrategy<E extends Entity> extends Cloneable {

    /**
     * {@inheritDoc}
     */
    @Override
    PopulationInitialisationStrategy<E> getClone();

    /**
     * Set the entity type to use.
     * @param entity The entity type to use.
     */
    void setEntityType(Entity entity);

    /**
     * Get the current entity type.
     * @return The entity being used.
     */
    Entity getEntityType();

    /**
     * Initialise the {@see net.sourceforge.cilib.entity.Entity} collection based on the given
     * Topology and Problem.
     * @param problem The Problem to based the initialisation on
     * @return An {@code Iterable<E>} of instances.
     */
    Iterable<E> initialise(OptimisationProblem problem);

    /**
     * Get the number of entities specified to be created by the <code>InitialisationStrategy</code>.
     * @return The number of entities to construct.
     */
    int getEntityNumber();

    /**
     * Set the number of {@code Entity} instances to clone.
     * @param entityNumber The number to clone.
     */
    void setEntityNumber(int entityNumber);

}
