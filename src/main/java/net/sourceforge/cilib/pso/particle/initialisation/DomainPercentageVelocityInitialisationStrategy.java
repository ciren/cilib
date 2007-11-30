/*
 * DomainPercentageVelocityInitialisationStrategy.java
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
package net.sourceforge.cilib.pso.particle.initialisation;

import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.type.types.container.Vector;

// TODO: this class should be refactored to use the RandomInitialVelocityStrategy or to be a compound
//       operation where the velocity is first randomised and then scaled by a percentage
public class DomainPercentageVelocityInitialisationStrategy implements
		VelocityInitialisationStrategy {
	private static final long serialVersionUID = -7178323673738508287L;
	private VelocityInitialisationStrategy velocityInitialisationStrategy;
	private double percentage;
	
	public DomainPercentageVelocityInitialisationStrategy() {
		this.velocityInitialisationStrategy = new RandomInitialVelocityStrategy();
		this.percentage = 0.1;
	}
	
	public DomainPercentageVelocityInitialisationStrategy(DomainPercentageVelocityInitialisationStrategy copy) {
		this.velocityInitialisationStrategy = copy.velocityInitialisationStrategy.getClone();
		this.percentage = copy.percentage;
	}
	
	public DomainPercentageVelocityInitialisationStrategy getClone() {
		return new DomainPercentageVelocityInitialisationStrategy(this);
	}

	public void initialise(Particle particle) {
		velocityInitialisationStrategy.initialise(particle);
		
		Vector velocity = (Vector) particle.getVelocity();
		for (int i = 0; i < velocity.getDimension(); ++i)
			velocity.setReal(i, velocity.getReal(i) * percentage);
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

}
