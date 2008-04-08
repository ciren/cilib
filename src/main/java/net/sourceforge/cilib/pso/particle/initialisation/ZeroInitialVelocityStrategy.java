/*
 * ZeroInitialVelocityStrategy.java
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
package net.sourceforge.cilib.pso.particle.initialisation;

import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * 
 * @author Gary Pampara
 */
public class ZeroInitialVelocityStrategy implements
		VelocityInitialisationStrategy {
	private static final long serialVersionUID = 7339106246533286485L;

	public ZeroInitialVelocityStrategy() {
		
	}
	
	public ZeroInitialVelocityStrategy(ZeroInitialVelocityStrategy copy) {
		
	}
	
	public ZeroInitialVelocityStrategy getClone() {
		return new ZeroInitialVelocityStrategy(this);
	}

	public void initialise(Particle particle) {
		Vector velocity = (Vector) particle.getVelocity();
		
		velocity.reset();		
	}

}
