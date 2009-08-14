/**
 * Copyright (C) 2003 - 2009
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
package net.sourceforge.cilib.algorithm.initialisation;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.util.Cloneable;

/**
 * Interface describing the manner in which populations are initialised.
 * @param <E> The {@code Entity} type.
 * @author Gary Pampara
 */
public interface PopulationInitialisationStrategy<E extends Entity> extends Cloneable {

    /**
     * {@inheritDoc}
     */
    @Override
    public PopulationInitialisationStrategy getClone();

    /**
     * Set the entity type to use.
     * @param entity The entity type to use.
     */
    public void setEntityType(Entity entity);

    /**
     * Get the current entity type.
     * @return The entity being used.
     */
    public Entity getEntityType();

    /**
     * Initialise the {@see net.sourceforge.cilib.entity.Entity} collection based on the given
     * Topology and Problem.
     * @param problem The Problem to based the initialisation on
     * @return An {@code Iterable<E>} of instances.
     */
    public Iterable<E> initialise(OptimisationProblem problem);

    /**
     * Get the number of entities specified to be created by the <code>InitialisationStrategy</code>.
     * @return The number of entities to construct.
     */
    public int getEntityNumber();

    /**
     * Set the number of {@code Entity} instances to clone.
     * @param entityNumber The number to clone.
     */
    public void setEntityNumber(int entityNumber);

}
