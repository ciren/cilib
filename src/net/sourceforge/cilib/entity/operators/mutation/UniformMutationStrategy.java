/*
 * UniformMutationStrategy.java
 * 
 * Created on Apr 1, 2006
 *
 * Copyright (C) 2003, 2004 - CIRG@UP 
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
package net.sourceforge.cilib.entity.operators.mutation;

import net.sourceforge.cilib.controlparameterupdatestrategies.ControlParameterUpdateStrategy;
import net.sourceforge.cilib.controlparameterupdatestrategies.ProportionalControlParameterUpdateStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.type.types.Vector;

/**
 * @author  Andries Engelbrecht
 */
public class UniformMutationStrategy extends MutationStrategy {

	private String operatorType;
	private ControlParameterUpdateStrategy minStrategy, maxStrategy;
	
	public UniformMutationStrategy() {
		minStrategy = new ProportionalControlParameterUpdateStrategy();
		maxStrategy = new ProportionalControlParameterUpdateStrategy();
		
		this.operatorType = "+";
	}
	@Override
	public void mutate(Entity entity) {
		Vector chromosome = (Vector) entity.get();
		
		if (this.getMutationProbability().getParameter() >= this.getRandomNumber().getUniform()) {
			for (int i = 0; i < chromosome.getDimension(); i++) {
				double value;
				//Numeric element = (Numeric) chromosome.get(i);
				//double min = element.getLowerBound();
				//double max = element.getUpperBound();
				
				if (operatorType.equals("+")) {	
					value = chromosome.getReal(i) + this.getRandomNumber().getUniform(minStrategy.getParameter(),maxStrategy.getParameter());
				}
				else
					value = chromosome.getReal(i) * this.getRandomNumber().getUniform(minStrategy.getParameter(),maxStrategy.getParameter());
				
				chromosome.setReal(i, value);
			}
		}

	}

}
