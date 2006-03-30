/*
 * AccelerationComponentStrategy.java
 *
 * Created on Mar 2, 2004
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

package net.sourceforge.cilib.pso.parameterupdatestrategies;

import java.util.Random;

import net.sourceforge.cilib.controlparameterupdatestrategies.ConstantUpdateStrategy;
import net.sourceforge.cilib.controlparameterupdatestrategies.ControlParameterUpdateStrategy;
import net.sourceforge.cilib.math.random.MersenneTwister;

/**
 * This class defined the required functionality for the <tt>AccelerationComponent</tt>
 * within a <tt>VelocityUpdate</tt>.
 * 
 * @author Gary Pampara
 * @author Andries Engelbrecht
 */
public class AccelerationUpdateStrategy implements ControlParameterUpdateStrategy {

	protected ControlParameterUpdateStrategy parameterUpdateStrategy;
	protected Random randomiser;
	
	
	/**
	 * Create a new <tt>AccelerationUpdateStrategy</tt>. This object will be
	 * instantiated by default with a contained <tt>ConstantUpdateStrategy</tt>
	 * and a <tt>MersenneTwister</tt> as the random number generator.
	 *
	 */
	public AccelerationUpdateStrategy() {
		this.parameterUpdateStrategy = new ConstantUpdateStrategy();
		this.randomiser = new MersenneTwister();
	}
	
	
	/**
	 * Copy constructor.
	 * @param copy
	 */
	public AccelerationUpdateStrategy(AccelerationUpdateStrategy copy) {
		this.parameterUpdateStrategy = copy.parameterUpdateStrategy.clone();
		this.randomiser = new MersenneTwister();
	}
	
	
	public AccelerationUpdateStrategy clone() {
		return new AccelerationUpdateStrategy(this);
	}
	
	
	/**
	 * Return the current <tt>Random</tt>.
	 * @return The <tt>Random</tt> being used.
	 */
	public Random getRandomiser() {
		return randomiser;
	}
	
	
	/**
	 * Set the <tt>Random</tt> to be used 
	 * @param randomiser The <tt>Random</tt> to be used
	 */
	public void setRandomiser(Random randomiser) {
		this.randomiser = randomiser;
	}


	/**
	 * Get the currently used <tt>ControlParameterUpdateStrategy</tt>.
	 * @return The used <tt>ControlParameterUpdateStrategy</tt>
	 */
	public ControlParameterUpdateStrategy getParameterUpdateStrategy() {
		return parameterUpdateStrategy;
	}


	/**
	 * Set the <tt>ControlParameterUpdateStrategy</tt> to be used.
	 * @param parameterUpdateStrategy The <tt>ControlParameterUpdateStrategy</tt> to be used.
	 */
	public void setParameterUpdateStrategy(
			ControlParameterUpdateStrategy parameterUpdateStrategy) {
		this.parameterUpdateStrategy = parameterUpdateStrategy;
	}


	/**
	 * Get the value of this parameter after it has been multiplied with a uniform
	 * random number.
	 * 
	 * @return The value representing a uniform ranfom unmber multiplied by the value of the underlying
	 * <tt>ControlParameterUpdateStrategy</tt>.
	 */
	public double getParameter() {
		return this.randomiser.nextDouble() * this.parameterUpdateStrategy.getParameter();
	}
	
	
	public double getParameter(double min, double max) {
		throw new UnsupportedOperationException("");
	}


	/**
	 * Set the value of the parameter in the underlying <tt>ControlParameterUpdateStrategy</tt>.
	 * @param value The value to have the parameter set to,
	 */
	public void setParameter(double value) {
		this.parameterUpdateStrategy.setParameter(value);
	}


	/**
	 * Update the contained <tt>ControlParameterUpdateStrategy</tt>.
	 */
	public void updateParameter() {
		this.parameterUpdateStrategy.updateParameter();		
	}

}
