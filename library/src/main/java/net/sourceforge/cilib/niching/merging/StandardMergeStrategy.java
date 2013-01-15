/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching.merging;

import java.util.Collection;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.pso.particle.ParticleBehavior;

/**
 * Takes all the entities of the second sub-swarm, puts them in the first sub-swarm
 * and returns a copy of the combined sub-swarms.
 */
public class StandardMergeStrategy extends MergeStrategy {
    private static final long serialVersionUID = 6790307057694598017L;

    @Override
    public PopulationBasedAlgorithm f(PopulationBasedAlgorithm subSwarm1, PopulationBasedAlgorithm subSwarm2) {
        PopulationBasedAlgorithm newSwarm = subSwarm1.getClone();
        newSwarm.getTopology().addAll((Collection) subSwarm2.getClone().getTopology());

        Particle p;
        if (!newSwarm.getTopology().isEmpty() && (p = (Particle) newSwarm.getTopology().get(0)) instanceof Particle) {
            ParticleBehavior pb = p.getParticleBehavior();
            for (Entity e : newSwarm.getTopology()) {
                ((Particle) e).setParticleBehavior(pb);
            }
        }

        return newSwarm;
    }
}
