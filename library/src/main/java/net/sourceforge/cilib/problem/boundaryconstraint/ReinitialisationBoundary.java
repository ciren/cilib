/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.problem.boundaryconstraint;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.type.types.Types;

/**
 * Once the entity has over shot the search space boundaries, re-initialise
 * the Entity once again to be within the search space of the problem at a
 * random position.
 *
 * @see Types#isInsideBounds(net.sourceforge.cilib.type.types.Type)
 */
public class ReinitialisationBoundary implements BoundaryConstraint {
    private static final long serialVersionUID = -512973040124015665L;

    /**
     * {@inheritDoc}
     */
    @Override
    public ReinitialisationBoundary getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enforce(Entity entity) {
        if (!Types.isInsideBounds(entity.getCandidateSolution())) {
            entity.reinitialise();
        }
    }

}
