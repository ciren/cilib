/*
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.pso.dynamic;

import net.sourceforge.cilib.entity.EntityType;
import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.problem.OptimisationProblem;

/**
 * Charged Particle used by charged PSO (ChargedVelocityUpdate). The only difference
 * from DynamicParticle is that a charged particle stores the charge magnitude and
 * the inialisation strategy for charge.
 *
 * @author Anna Rakitianskaia
 *
 */
public class ChargedParticle extends DynamicParticle/*StandardParticle implements ReevaluatingParticle*/{
    private static final long serialVersionUID = 7872499872488908368L;
    private double charge;
    private ChargedParticleInitialisationStrategy chargedParticleInitialisationStrategy;

    public ChargedParticle() {
        super();
        velocityUpdateStrategy = new ChargedVelocityUpdateStrategy();
        chargedParticleInitialisationStrategy = new StandardChargedParticleInitialisationStrategy();
    }

    public ChargedParticle(ChargedParticle copy) {
        super(copy);

        this.charge = copy.charge;
        this.chargedParticleInitialisationStrategy = copy.chargedParticleInitialisationStrategy.getClone();
    }

    public ChargedParticle getClone() {
        return new ChargedParticle(this);
    }


    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if ((object == null) || (this.getClass() != object.getClass()))
            return false;

        ChargedParticle other = (ChargedParticle) object;
        return super.equals(object) &&
            (Double.valueOf(this.charge).equals(Double.valueOf(other.charge)));
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
    /**
     * @return the chargedParticleInitialisationStrategy
     */
    public ChargedParticleInitialisationStrategy getChargedParticleInitialisationStrategy() {
        return chargedParticleInitialisationStrategy;
    }
    /**
     * @param chargedParticleInitialisationStrategy the chargedParticleInitialisationStrategy to set
     */
    public void setChargedParticleInitialisationStrategy(
            ChargedParticleInitialisationStrategy chargedParticleInitialisationStrategy) {
        this.chargedParticleInitialisationStrategy = chargedParticleInitialisationStrategy;
    }

    @Override
    public void initialise(OptimisationProblem problem) {
        this.getProperties().put(EntityType.CANDIDATE_SOLUTION, problem.getDomain().getBuiltRepresenation().getClone());

        getPositionInitialisationStrategy().initialize(EntityType.CANDIDATE_SOLUTION, this);
        this.getProperties().put(EntityType.Particle.BEST_POSITION, getPosition().getClone());

        // Create the velocity vector by cloning the position and setting all the values
        // within the velocity to 0
        this.getProperties().put(EntityType.Particle.VELOCITY, getPosition().getClone());
        this.velocityInitializationStrategy.initialize(EntityType.Particle.VELOCITY, this);

        // Initialise particle charge
        chargedParticleInitialisationStrategy.initialise(this);

        this.getProperties().put(EntityType.FITNESS, InferiorFitness.instance());
        this.getProperties().put(EntityType.Particle.BEST_FITNESS, InferiorFitness.instance());
        neighbourhoodBest = this;
    }
}
