/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.util.selection.weighting;

import net.sourceforge.cilib.pso.particle.ParticleBehavior;

/**
 * Obtains the ratio of the ParticleBehavior based on how often it gets selected.
 */
public class SelectedRatio implements ParticleBehaviorRatio {
    @Override
    public double getRatio(ParticleBehavior particleBehavior) {
        return particleBehavior.getSelectedCounter();
    }
}
