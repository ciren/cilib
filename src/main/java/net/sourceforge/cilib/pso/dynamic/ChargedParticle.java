/*
 * ChargedParticle.java
 *
 * Copyright (C) 2003 - 2008
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

import net.sourceforge.cilib.problem.InferiorFitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.pso.PSO;

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
        setId(PSO.getNextParticleId());
        
        getPositionInitialisationStrategy().initialise(this, problem);
        
        // Create the velocity vector by cloning the position and setting all the values
        // within the velocity to 0
        this.properties.put("velocity", getPosition().getClone());
        
        velocityInitialisationStrategy.initialise(this);
        
        // Initialise particle charge
        chargedParticleInitialisationStrategy.initialise(this);
        
        this.properties.put("fitness", InferiorFitness.instance());
        this.properties.put("bestFitness", InferiorFitness.instance());
        neighbourhoodBest = this;
    }
}
