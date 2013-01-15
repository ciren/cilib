/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.pso.dynamic;

import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.solution.InferiorFitness;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Charged Particle used by charged PSO (ChargedVelocityProvider). The only
 * difference from DynamicParticle is that a charged particle stores the charge
 * magnitude and the initialisation strategy for charge.
 *
 *
 */
public class ChargedParticle extends DynamicParticle {

    private static final long serialVersionUID = 7872499872488908368L;

    private double charge;

    public ChargedParticle() {
        this.behavior.setVelocityProvider(new ChargedVelocityProvider());
    }

    public ChargedParticle(ChargedParticle copy) {
        super(copy);
        this.charge = copy.charge;
    }

    @Override
    public ChargedParticle getClone() {
        return new ChargedParticle(this);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if ((object == null) || (this.getClass() != object.getClass())) {
            return false;
        }

        ChargedParticle other = (ChargedParticle) object;
        return super.equals(object)
                && (Double.valueOf(this.charge).equals(Double.valueOf(other.charge)));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + super.hashCode();
        hash = 31 * hash + Double.valueOf(charge).hashCode();
        return hash;
    }

    /**
     * @return the charge
     */
    public double getCharge() {
        return charge;
    }

    /**
     * @param charge the charge to set
     */
    public void setCharge(double charge) {
        this.charge = charge;
    }

    @Override
    public void initialise(Problem problem) {
        this.getProperties().put(EntityType.CANDIDATE_SOLUTION, problem.getDomain().getBuiltRepresentation().getClone());
        this.getProperties().put(EntityType.Particle.BEST_POSITION, Vector.copyOf(getPosition()));
        this.getProperties().put(EntityType.Particle.VELOCITY, Vector.copyOf(getPosition()));

        this.positionInitialisationStrategy.initialise(EntityType.CANDIDATE_SOLUTION, this);
        this.personalBestInitialisationStrategy.initialise(EntityType.Particle.BEST_POSITION, this);
        this.velocityInitialisationStrategy.initialise(EntityType.Particle.VELOCITY, this);

        this.getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
        this.getProperties().put(EntityType.Particle.BEST_FITNESS, InferiorFitness.instance());
        this.neighbourhoodBest = this;
        this.getProperties().put(EntityType.Particle.Count.PBEST_STAGNATION_COUNTER, Int.valueOf(0));
    }
}
