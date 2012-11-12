/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching.utils;

import fj.F;
import fj.data.List;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.niching.NichingSwarms;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.util.functions.Algorithms;

public class JoinedTopologyProvider extends TopologyProvider {
    @Override
    public List<? extends Entity> f(NichingSwarms a) {
        return List.join(
            List.cons(a._1(), a._2())
                .map(Algorithms.getTopology())
                .map(new F<Topology<? extends Entity>, List<Entity>>() {
                    @Override
                    public List<Entity> f(Topology<? extends Entity> a) {
                        return List.iterableList((Topology<Entity>) a).filter(new F<Entity, Boolean>() {
                            @Override
                            public Boolean f(Entity a) {
                                return ((Int) a.getProperties().get(EntityType.Coevolution.POPULATION_ID)).intValue() == 0;
                            }                            
                        });
                    }
                })
            );
    }
}
