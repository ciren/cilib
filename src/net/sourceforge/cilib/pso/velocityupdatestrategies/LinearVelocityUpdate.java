/*
 * DiscreteFunction.java
 * 
 * Created on Jul 26, 2004
 *
 * Copyright (C) 2004 - CIRG@UP 
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
package net.sourceforge.cilib.pso.velocityupdatestrategies;

import java.util.Random;

import net.sourceforge.cilib.math.random.KnuthSubtractive;
import net.sourceforge.cilib.pso.parameterupdatestrategies.AccelerationUpdateStrategy;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.Vector;


/**
 * TODO: test this
 * 
 * @author engel
 */
public class LinearVelocityUpdate extends StandardVelocityUpdate {

	public LinearVelocityUpdate() {
		super();
		socialAcceleration = new AccelerationUpdateStrategy();
		cognitiveAcceleration = new AccelerationUpdateStrategy();
		socialRandomGenerator = new KnuthSubtractive();
		cognitiveRandomGenerator = new KnuthSubtractive();
	}
	
	/* (non-Javadoc)
	 * @see net.sourceforge.cilib.PSO.VelocityUpdate#updateVelocity(net.sourceforge.cilib.PSO.Particle)
	 */
	public void updateVelocity(Particle particle) {
		Vector velocity = (Vector) particle.getVelocity();
		Vector position = (Vector) particle.getPosition();
		Vector bestPosition = (Vector) particle.getBestPosition();
		Vector nBestPosition = (Vector) particle.getNeighbourhoodBest().getBestPosition();
		
		float social = socialRandomGenerator.nextFloat();
		float cognitive = cognitiveRandomGenerator.nextFloat();
		
		for (int i = 0; i < particle.getDimension(); ++i) {			
			double tmp = inertiaWeight.getParameter()*velocity.getReal(i)
				+ cognitive * (bestPosition.getReal(i) - position.getReal(i)) * cognitiveAcceleration.getParameter()
				+ social * (nBestPosition.getReal(i) - position.getReal(i)) * socialAcceleration.getParameter();
			velocity.setReal(i, tmp);
			
			clamp(velocity, i);
		}
		
	}

	
	
	/**
	 * @return Returns the congnitiveRandomGenerator.
	 */
	public Random getCongnitiveRandomGenerator() {
		return cognitiveRandomGenerator;
	}

	/**
	 * @param congnitiveRandomGenerator The congnitiveRandomGenerator to set.
	 */
	public void setCongnitiveRandomGenerator(Random congnitiveRandomGenerator) {
		this.cognitiveRandomGenerator = congnitiveRandomGenerator;
	}

	/**
	 * @return Returns the socialRandomGenerator.
	 */
	public Random getSocialRandomGenerator() {
		return socialRandomGenerator;
	}

	/**
	 * @param socialRandomGenerator The socialRandomGenerator to set.
	 */
	public void setSocialRandomGenerator(Random socialRandomGenerator) {
		this.socialRandomGenerator = socialRandomGenerator;
	}


	private Random socialRandomGenerator;
	private Random cognitiveRandomGenerator;
	
}
