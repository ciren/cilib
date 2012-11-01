/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.measurement.single.diversity.centerinitialisationstrategies;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topologies;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Returns the center of a given topology where the center is the position
 * of the best entity in the topology.
 */
public class GBestCenterInitialisationStrategy implements CenterInitialisationStrategy {

    /**
     * {@inheritDoc} 
     */
    @Override
    public Vector getCenter(Topology<? extends Entity> topology) {
        return (Vector) Topologies.getBestEntity(topology).getCandidateSolution();
    }
}
