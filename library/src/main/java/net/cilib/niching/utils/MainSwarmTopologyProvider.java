/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.niching.utils;

import fj.data.List;
import net.cilib.entity.Entity;
import net.cilib.niching.NichingSwarms;

/**
 *
 */
public class MainSwarmTopologyProvider extends TopologyProvider {
    @Override
    public List<? extends Entity> f(NichingSwarms a) {
        return List.iterableList(a._1().getTopology());
    }
}
