/*
 * LinearPositionUpdateStrategy.java
 *
 * Created on Oct 14, 2005
 *
 * Copyright (C) 2003 - 2006 
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

import net.sourceforge.cilib.pso.particle.Particle;
import net.sourceforge.cilib.type.types.Vector;

/**
 * This sets the position to the velocity.
 * 
 * @author Andries Engelbrecht
 *
 */
public class LinearPositionUpdateStrategy implements PositionUpdateStrategy {
	private static final long serialVersionUID = -3328130784035172372L;

	public LinearPositionUpdateStrategy() {
	}
        
        
	public LinearPositionUpdateStrategy(LinearPositionUpdateStrategy copy) {
		
	}
        
                
	public LinearPositionUpdateStrategy clone() {
		return new LinearPositionUpdateStrategy(this);
	}
        
	public void updatePosition(Particle particle) {
		Vector position = (Vector) particle.getPosition();
		Vector velocity = (Vector) particle.getVelocity();
                
		for (int i = 0; i < position.getDimension(); ++i)
			position.setReal(i, velocity.getReal(i));
	}
}