/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching.utils;

import fj.data.List;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.niching.NichingSwarms;

/**
 *
 */
public class MainSwarmTopologyProvider extends TopologyProvider {
    @Override
    public List<? extends Entity> f(NichingSwarms a) {
        return List.iterableList(a._1().getTopology());
    }
}
