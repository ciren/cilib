/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.pbestupdate;

import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.type.types.Types;

public class BoundedRelaxedNonDominatedPersonalBestUpdateStrategy extends RelaxedNonDominatedPersonalBestUpdateStrategy {

    /**
     * {@inheritDoc}
     */
    @Override
    public PersonalBestUpdateStrategy getClone() {
        return this;
    }

    /**
     * Updates the guide. If the new guide dominates the old guide, the new
     * guide is selected. However, if both guides are non-dominated, one of the
     * guides is randomly selected.
     * @param particle The particle who's guide is to be updated.
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
