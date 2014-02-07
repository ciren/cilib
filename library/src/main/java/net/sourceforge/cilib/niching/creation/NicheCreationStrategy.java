/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching.creation;

import fj.F2;
import net.sourceforge.cilib.algorithm.population.SinglePopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.behaviour.Behaviour;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.niching.NichingSwarms;

/**
 * Create new niching populations for the provided Niche. The newly found niche
 * points are provided and are then used to create new niching populations for
 * the provided Niche algorithm.
 */
public abstract class NicheCreationStrategy extends F2<NichingSwarms, Entity, NichingSwarms> {

    protected SinglePopulationBasedAlgorithm swarmType;
    protected Behaviour swarmBehavior;

    public void setSwarmType(SinglePopulationBasedAlgorithm swarm) {
        this.swarmType = swarm;
    }

    public SinglePopulationBasedAlgorithm getSwarmType() {
        return swarmType;
    }

    public void setSwarmBehavior(Behaviour swarmBehavior) {
        this.swarmBehavior = swarmBehavior;
    }

    public Behaviour getSwarmBehavior() {
        return swarmBehavior;
    }

}
