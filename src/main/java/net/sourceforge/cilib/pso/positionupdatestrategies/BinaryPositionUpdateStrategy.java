/*
 * BinaryParticle.java
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
package net.sourceforge.cilib.pso.positionupdatestrategies;

import net.sourceforge.cilib.math.MathUtil;
import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.Vector;


/**
 * Binary position update strategy to enable the BinaryPSO.
 * 
 * @author Gary Pampara
 */
public class BinaryPositionUpdateStrategy implements PositionUpdateStrategy {

	public BinaryPositionUpdateStrategy() {
		
	}
	
	
	public BinaryPositionUpdateStrategy(BinaryPositionUpdateStrategy copy) {

	}
	
	
	public BinaryPositionUpdateStrategy clone() {
		return new BinaryPositionUpdateStrategy(this);
	}
	
	/**
	 * BinaryPSO particle position update, as defined by Kennedy and Eberhart.
	 */
	public void updatePosition(Particle particle) {
		Vector position = (Vector) particle.getPosition();
		Vector velocity = (Vector) particle.getVelocity();
		
		for (int i = 0; i < position.getDimension(); i++) {
			double result = MathUtil.sigmoid(velocity.getReal(i));
			double rand = Math.random();
			
			if (rand < result) {
				position.setBit(i, true);
			}
			else {
				position.setBit(i, false);
			}
		}
	}
	
}
