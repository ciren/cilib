/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.algorithm.initialisation;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.Problem;
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
     * Initialise the {@link Entity} collection based on the given {@link Problem}.
     * @param problem The Problem to based the initialisation on
     * @return An {@code Iterable<E>} of instances.
     */
    Iterable<E> initialise(Problem problem);

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
