/*
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

import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.RandomNumber;
import net.sourceforge.cilib.pso.positionupdatestrategies.PositionUpdateStrategy;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Position update strategy for QSO (Quantum PSO). Implemented according
 * to paper by Blackwell and Branke, "Multiswarms, Exclusion, and Anti-
 * Convergence in Dynamic Environments."
 * 
 * @author Anna Rakitianskaia
 */
public class QuantumPositionUpdateStrategy implements PositionUpdateStrategy {

	private static final long serialVersionUID = -7844226788317206737L;
	private static final double EPSILON = 0.000000001;
	private double radius;
	private RandomNumber randomizer;
	
	public QuantumPositionUpdateStrategy() {
		radius = 5;
		randomizer = new RandomNumber();
	}
	
	public QuantumPositionUpdateStrategy(QuantumPositionUpdateStrategy copy) {
		this.radius = copy.radius;
		this.randomizer = copy.randomizer.getClone();		
	}
	
	public QuantumPositionUpdateStrategy getClone() {
		return new QuantumPositionUpdateStrategy(this);
	}
	
	/**
	 * Update particle position; do it in a standard way if the particle is neutral, and 
	 * in a quantum way if the particle is charged. The "quantum" way entails sampling the 
	 * position from a uniform distribution : a spherical cloud around gbest with a radius r.
	 * @param particle the particle to update position of
	 */
	public void updatePosition(Particle particle) {
		ChargedParticle checkChargeParticle = (ChargedParticle) particle;
		if(checkChargeParticle.getCharge() < EPSILON) { // the particle is neutral
			Vector position = (Vector) particle.getPosition();
			Vector velocity = (Vector) particle.getVelocity();
			
			for (int i = 0; i < position.getDimension(); ++i) {
	    		double value = position.getReal(i);
	    		value += velocity.getReal(i);
	    		position.setReal(i, value);
	    	}
		} 
		else { // the particle is charged
			Vector position = (Vector) particle.getPosition(); 
			Vector nucleus = (Vector) particle.getNeighbourhoodBest().getBestPosition(); // gbest
			
			for (int i = 0; i < position.getDimension(); ++i) {
				double centre = nucleus.getReal(i);
				position.setReal(i, randomizer.getUniform(centre - radius, centre + radius));
			}
		}
	}

	/**
	 * @return the radius
	 */
	public double getRadius() {
		return radius;
	}


	/**
	 * @param radius the radius to set
	 */
	public void setRadius(double radius) {
		if(radius < 0) throw new IllegalArgumentException("Radius of the electron cloud can not be negative");
		this.radius = radius;
	}
}
