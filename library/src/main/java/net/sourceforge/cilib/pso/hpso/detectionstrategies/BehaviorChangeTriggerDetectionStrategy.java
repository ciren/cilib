/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.hpso.detectionstrategies;

import net.sourceforge.cilib.pso.particle.Particle;

/**
 * An interface to detect when an {@link Particle} should change its behavior.
 */
public interface BehaviorChangeTriggerDetectionStrategy {
    /**
     * Detect whether some condition holds that should prompt a {@link Particle}
     * to change its behavior.
     *
     * @param particle  The particle to inspect.
     * @return True if the entity should change its behavior. False otherwise.
     */
    boolean detect(Particle particle);

    /**
     * Clone the current {@link BehaviorChangeTriggerDetectionStrategy}.
     *
     * @return A clone of this {@link BehaviorChangeTriggerDetectionStrategy}.
     */
    BehaviorChangeTriggerDetectionStrategy getClone();
}
