/*
 * FIPSAcceleration.java
 * 
 * Created on Jun 15, 2004
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
package net.sourceforge.cilib.pso.parameterupdatestrategies;

import net.sourceforge.cilib.controlparameter.RandomizingControlParameter;
import net.sourceforge.cilib.math.random.RandomNumber;

/**
 * This is an implementation of the fully informed PSO, for which there is only one velocity
 * component in addition to the inertia, and therefor the one larger default for the acceleration
 * @author engel
 * @deprecated this class is invalid and will be replaced by the functionality within the GCVelocityUpdateStrategy
 */
@Deprecated
public class FIPSAcceleration extends RandomizingControlParameter {
	private static final long serialVersionUID = -3430086608413297395L;
	private RandomNumber randomNumber;
	private double initialisationRange; // This is equal to c1 + c2 in original paper

	public FIPSAcceleration() {
		super();
		initialisationRange = 4.1;

		randomNumber = new RandomNumber();
	}

	public double getUniformRandom() {
		return randomNumber.getUniform(0, initialisationRange);
	}

	public RandomNumber getRandomNumber() {
		return randomNumber;
	}

	public void setRandomNumber(RandomNumber randomNumber) {
		this.randomNumber = randomNumber;
	}

	public double getInitialisationRange() {
		return initialisationRange;
	}

	public void setInitialisationRange(double initialisationRange) {
		this.initialisationRange = initialisationRange;
	}

	public double getParameter() {
		return this.getUniformRandom();
	}

	public void setParameter(double value) {
	}

	public void updateParameter() {
	}
}
