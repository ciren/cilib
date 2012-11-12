/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single.diversity.centerinitialisationstrategies;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Calculates the center of a given topology.
 */
public interface CenterInitialisationStrategy {

    /**
     * Get the population center.
     * @return the populationCenter
     */
    Vector getCenter(Topology<? extends Entity> topology);

}
