/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.niching.creation;

import fj.F2;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.niching.NichingSwarms;
import net.sourceforge.cilib.pso.particle.ParticleBehavior;

/**
 * Create new niching populations for the provided Niche. The newly found niche
 * points are provided and are then used to create new niching populations for
 * the provided Niche algorithm.
 */
public abstract class NicheCreationStrategy extends F2<NichingSwarms, Entity, NichingSwarms> {

    protected PopulationBasedAlgorithm swarmType;
    protected ParticleBehavior swarmBehavior;

    public void setSwarmType(PopulationBasedAlgorithm swarm) {
        this.swarmType = swarm;
    }

    public PopulationBasedAlgorithm getSwarmType() {
        return swarmType;
    }

    public void setSwarmBehavior(ParticleBehavior swarmBehavior) {
        this.swarmBehavior = swarmBehavior;
    }

    public ParticleBehavior getSwarmBehavior() {
        return swarmBehavior;
    }

}
