/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.pso.pbestupdate;

import net.cilib.entity.EntityType;
import net.cilib.pso.particle.Particle;
import net.cilib.problem.solution.InferiorFitness;
import net.cilib.type.types.Types;

/**
 * Implementation of {@link PersonalBestUpdateStrategy} where a
 * {@link Particle}'s guide can get updated if the new guide is not dominated by
 * the current guide, i.e. both of the guides are non-dominated. If both guides
 * are non-dominated the new guide is selected.
 */
public class BoundedDominantPersonalBestUpdateStrategy extends DominantPersonalBestUpdateStrategy {

   /**
    * {@inheritDoc}
    */
    @Override
    public PersonalBestUpdateStrategy getClone() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePersonalBest(Particle particle) {
        if (!Types.isInsideBounds(particle.getPosition())) {
            particle.getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
            return;
        }

        super.updatePersonalBest(particle);
    }
}
