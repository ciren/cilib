/*
 * 
 * RandomBoundedInitialVelocityStrategy.java
 * 
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

import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.entity.Particle;
import net.sourceforge.cilib.math.random.RandomNumber;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * 
 * @author Andries Engelbrecht
 */
public class RandomBoundedInitialVelocityStrategy implements
		VelocityInitialisationStrategy {
	private static final long serialVersionUID = -7926839076670354209L;
	ControlParameter LowerBound;
	ControlParameter UpperBound;
	private RandomNumber random1;

	public RandomBoundedInitialVelocityStrategy() {
		this.LowerBound = new ConstantControlParameter(0.1);
		this.UpperBound = new ConstantControlParameter(0.1);
		this.random1 = new RandomNumber();
	}
	
	public RandomBoundedInitialVelocityStrategy(RandomBoundedInitialVelocityStrategy copy) {
		this.LowerBound = copy.LowerBound;
		this.UpperBound = copy.UpperBound;
		this.random1 = copy.random1;
	}
	
	public RandomBoundedInitialVelocityStrategy getClone() {
		return new RandomBoundedInitialVelocityStrategy(this);
	}

	public void initialise(Particle particle) {
		Vector velocity = (Vector) particle.getVelocity();
		for (int i = 0; i < velocity.getDimension(); i++)
		   velocity.setReal(i, random1.getUniform(LowerBound.getParameter(), UpperBound.getParameter()));
	}

	public ControlParameter getLowerBound() {
		return LowerBound;
	}

	public void setLowerBound(ControlParameter lowerBound) {
		this.LowerBound = lowerBound;
	}
	
	public ControlParameter getUpperBound() {
		return UpperBound;
	}

	public void setUpperBound(ControlParameter upperBound) {
		this.UpperBound = upperBound;
	}
}
