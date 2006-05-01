/*
 * MutationOperator.java
 * 
 * Created on Jun 21, 2005
 *
 * Copyright (C) 2003, 2004, 2005 - CIRG@UP 
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
 * 
 */
package net.sourceforge.cilib.ec.mutationoperators;

import java.util.Random;

import net.sourceforge.cilib.controlparameterupdatestrategies.ConstantUpdateStrategy;
import net.sourceforge.cilib.controlparameterupdatestrategies.ControlParameterUpdateStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.math.random.generator.MersenneTwister;

/**
 * @author otter
 *
 * Defines a common interface for all mutation operators.
 * Every Selection Operator must implement this interface. 
 */
public abstract class MutationOperator<E extends Entity> {
	
	//This class member is for purposes of the rate at which paramaters are selected within the entity to be mutated.
	protected ControlParameterUpdateStrategy paramaterSelectionRate;
	protected Random random = new MersenneTwister();

	public MutationOperator() {
		paramaterSelectionRate = new ConstantUpdateStrategy(0.3);
		random = new MersenneTwister();
	}
	
	/**
	 * Mutate the supplied entity.
	 * @param entity
	 */
	public abstract void mutate(E entity);

	public void setRandom(Random random) {
		this.random = random;
	}
	public void setParamaterSelectionRate(ControlParameterUpdateStrategy paramaterSelectionRate) {
		this.paramaterSelectionRate = paramaterSelectionRate;
	} 	
}
