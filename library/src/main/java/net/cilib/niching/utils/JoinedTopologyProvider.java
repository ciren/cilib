/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.cilib.niching.utils;

import net.cilib.entity.Entity;
import net.cilib.entity.EntityType;
import net.cilib.niching.NichingSwarms;
import net.cilib.type.types.Int;
import net.cilib.util.functions.Algorithms;
import fj.F;
import fj.data.List;

public class JoinedTopologyProvider extends TopologyProvider {
    @Override
    public List<? extends Entity> f(NichingSwarms a) {
        return List.join(
            List.cons(a._1(), a._2())
                .map(Algorithms.getTopology())
                .map(new F<List<? extends Entity>, List<Entity>>() {
                    @Override
                    public List<Entity> f(List<? extends Entity> a) {
                        return List.iterableList(((List<Entity>)a).filter(new F<Entity, Boolean>() {
                            @Override
                            public Boolean f(Entity a) {
                                return ((Int) a.getProperties().get(EntityType.Coevolution.POPULATION_ID)).intValue() == 0;
                            }
                        }));
                    }
                })
            );
    }
}
