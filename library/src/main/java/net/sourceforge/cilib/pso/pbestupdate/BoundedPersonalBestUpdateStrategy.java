/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.pbestupdate;

import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.Types;

/**
 * Update the personal best of the particle, if it is a valid update. Valid updates are
 * defined to be only within the problem search space. Any particle drifting into an
 * infeasible part of the search space will be allowed to do so, however, any solutions
 * found will not allowed to become personal best positions.
 *
 */
public class BoundedPersonalBestUpdateStrategy implements PersonalBestUpdateStrategy {

    private static final long serialVersionUID = -3574938411781908840L;
    private PersonalBestUpdateStrategy delegate;

    public BoundedPersonalBestUpdateStrategy() {
        this.delegate = new StandardPersonalBestUpdateStrategy();
    }

    public BoundedPersonalBestUpdateStrategy(BoundedPersonalBestUpdateStrategy copy) {
        this.delegate = copy.delegate.getClone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PersonalBestUpdateStrategy getClone() {
        return new BoundedPersonalBestUpdateStrategy(this);
    }

    /**
     * Update personal best if and only if the particle is within the bounds of the
     * search space / problem.
     * @param particle The particle to update.
     */
    @Override
    public void updatePersonalBest(Particle particle) {
        if (!Types.isInsideBounds(particle.getPosition())) {
            particle.getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
            return;
        }

        delegate.updatePersonalBest(particle);
    }

    public void setDelegate(PersonalBestUpdateStrategy delegate) {
        this.delegate = delegate;
    }

    public PersonalBestUpdateStrategy getDelegate() {
        return delegate;
    }
}
