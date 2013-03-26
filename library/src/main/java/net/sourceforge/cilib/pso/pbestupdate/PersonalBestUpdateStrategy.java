/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.pbestupdate;

import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.util.Cloneable;

/**
 * Updates the personal best of the {@link Particle}.
 * <p>
 * Updates are done in a variety of manners, refer to implementations for details.
 */
public interface PersonalBestUpdateStrategy extends Cloneable {

    /**
     * {@inheritDoc}
     */
    PersonalBestUpdateStrategy getClone();

    /**
     * Update the personal best of the provided {@link Particle}.
     * @param particle The particle to update.
     */
    void updatePersonalBest(Particle particle);
}
