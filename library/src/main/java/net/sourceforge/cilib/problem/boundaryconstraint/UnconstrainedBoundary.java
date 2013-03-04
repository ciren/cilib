/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.boundaryconstraint;

import net.sourceforge.cilib.entity.Entity;

/**
 * This is the default boundary constraint, whereby no boundary checking
 * is performed. This constraint defines that no boundary checking is always
 * defined to return {@code true}.
 */
public class UnconstrainedBoundary implements BoundaryConstraint {

    private static final long serialVersionUID = -6672863576480662484L;

    /**
     * {@inheritDoc}
     */
    @Override
    public UnconstrainedBoundary getClone() {
        return this;
    }


    /**
     * This enforcement of the boundary constraint does nothing.
     * @param entity The entity to which no constraint is to be applied.
     */
    @Override
    public void enforce(Entity entity) {
        // Do nothing as there is no boundary constraint to enforce
    }

}
