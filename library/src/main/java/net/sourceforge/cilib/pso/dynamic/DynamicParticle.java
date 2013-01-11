/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic;

import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.pso.particle.StandardParticle;

/**
 * Special particle type to use with dynamic algorithms. The extra functionality
 * that it adds is the ability to re-evaluate both current and best position of
 * the particle. A dynamic algorithm usually re-evaluates all particles when a
 * change in the environment has been detected.
 */
public class DynamicParticle extends StandardParticle {

    private static final long serialVersionUID = 1752969607979236619L;

    public DynamicParticle() {
        super();
    }

    public DynamicParticle(DynamicParticle copy) {
        super(copy);
    }

    public DynamicParticle getClone() {
           return new DynamicParticle(this);
    }

    /**
     * Re-evaluate both best and current position of the particle.
     */
    public void reevaluate() {
        DynamicParticle dp = this.getClone();
        dp.getProperties().put(EntityType.CANDIDATE_SOLUTION, dp.getBestPosition());
        this.getProperties().put(EntityType.Particle.BEST_FITNESS, getFitnessCalculator().getFitness(dp));

        this.calculateFitness();
    }
}
