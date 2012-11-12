/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.selection.weighting;

import net.sourceforge.cilib.pso.particle.ParticleBehavior;

/**
 * Obtain the ratio of a ParticleBehavior.
 */
public interface ParticleBehaviorRatio {

    /**
     * Obtain the ratio of the provided ParticleBehavior.
     * 
     * @param particleBehavior The ParticleBehavior to query.
     * @return The obtained ratio value.
     */
    double getRatio(ParticleBehavior particleBehavior);
}
