/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching.merging;

import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.behaviour.Behaviour;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.pso.particle.Particle;

/**
 * Takes all the entities of the second sub-swarm, puts them in the first sub-swarm
 * and returns a copy of the combined sub-swarms.
 */
public class StandardMergeStrategy extends MergeStrategy {
    private static final long serialVersionUID = 6790307057694598017L;

    @Override
    public SinglePopulationBasedAlgorithm f(SinglePopulationBasedAlgorithm subSwarm1, SinglePopulationBasedAlgorithm subSwarm2) {
        SinglePopulationBasedAlgorithm newSwarm = subSwarm1.getClone();
        newSwarm.setTopology(newSwarm.getTopology().append(subSwarm2.getClone().getTopology()));

        Particle p;
        if (!newSwarm.getTopology().isEmpty() && (p = (Particle) newSwarm.getTopology().head()) instanceof Particle) {
            Behaviour pb = p.getBehaviour();
            fj.data.List<Entity> local = newSwarm.getTopology();
            for (Entity e : local) {
                e.setBehaviour(pb);
            }
        }

        return newSwarm;
    }
}
