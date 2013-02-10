/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.boundaryconstraint;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.util.Cloneable;

/**
 * Enforce predefined boundary constraints on {@link Entity} instances that are
 * operating in the current search space. Various strategies are available to
 * enforce these boundary constraints on the provided {@link Entity} objects.
 */
public interface BoundaryConstraint extends Cloneable {

    /**
     * {@inheritDoc}
     */
    @Override
    BoundaryConstraint getClone();

    /**
     * Enforce the defined boundary constraint on the provided {@linkplain Entity}.
     * @param entity The {@linkplain Entity} with which the boundary is to be enforced.
     */
    void enforce(Entity entity);

}
