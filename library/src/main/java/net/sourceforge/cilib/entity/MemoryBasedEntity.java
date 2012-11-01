/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity;

import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.type.types.container.StructuredType;

/**
 * All {@link Entity} types that have a memory of pervious experiences
 * should implement this interface.
 */
public interface MemoryBasedEntity {

    /**
     * Get the best position of the {@linkplain MemoryBasedEntity}.
     * @return The {@linkplain net.sourceforge.cilib.type.types.Type}
     *         representing the best position.
     */
    StructuredType getBestPosition();

    /**
     * Get the best {@linkplain Fitness} for the {@linkplain MemoryBasedEntity}.
     * @return The entity's best {@linkplain Fitness}.
     */
    Fitness getBestFitness();
}
