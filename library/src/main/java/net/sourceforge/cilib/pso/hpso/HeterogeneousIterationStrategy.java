/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.hpso;

import java.util.List;
import net.sourceforge.cilib.entity.behaviour.Behaviour;
import net.sourceforge.cilib.measurement.multiple.AdaptiveHPSOBehaviorProfileMeasurement;

/**
 * Interface that heterogeneous iteration strategies should implement so they can be measured
 * using {@link AdaptiveHPSOBehaviorProfileMeasurement}
 */
public interface HeterogeneousIterationStrategy {
    /**
     * Add a {@link ParticleBehavior} to the behavior pool.
     * @param behavior The {@link ParticleBehavior} to add to the behavior pool.
     */
    public void addBehavior(Behaviour behavior);
    
    /**
     * Set the {@link ParticleBehavior} pool.
     * @param pool A {@link List} of {@link ParticleBehavior} objects.
     */
    public void setBehaviorPool(List<Behaviour> pool);
    
    /**
     * Get the current behavior pool.
     * @return The current {@link List} of {@link ParticleBehavior} objects.
     */
    public List<Behaviour> getBehaviorPool();
}
