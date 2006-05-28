/*
 * CauchyMutationStrategy.java
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

import java.util.List;
import java.util.ListIterator;

import net.sourceforge.cilib.controlparameterupdatestrategies.ControlParameterUpdateStrategy;
import net.sourceforge.cilib.controlparameterupdatestrategies.ProportionalControlParameterUpdateStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Vector;

/**
 * 
 * @author Andries Engelbrecht
 * @author Gary Pampara
 */
public class CauchyMutationStrategy extends MutationStrategy {
	
	private double location;
	private ControlParameterUpdateStrategy scaleStrategy;
	
	public CauchyMutationStrategy() {
		this.location = 0;
		this.scaleStrategy = new ProportionalControlParameterUpdateStrategy();
	}

	/**
	 * TODO: add comment :) Will wants this one
	 */
	@Override
	public void mutate(List<? extends Entity> entity) {

		for (ListIterator<? extends Entity> individual = entity.listIterator(); individual.hasNext(); ) {
			Entity current = individual.next(); 
			Vector chromosome = (Vector) current.get();
			
			if (this.getMutationProbability().getParameter() >= this.getRandomNumber().getUniform()) {
				for (int i = 0; i < chromosome.getDimension(); i++) {
					double value;
					Numeric element = (Numeric) chromosome.get(i);
					double scale = this.scaleStrategy.getParameter(element.getLowerBound(), element.getUpperBound());
					
					value = this.getOperatorStrategy().evaluate(chromosome.getReal(i), this.getRandomNumber().getCauchy(this.location, scale));
									
					chromosome.setReal(i, value);
				}
			}
		}
		
	}

	
	public double getLocation() {
		return location;
	}

	public void setLocation(double location) {
		this.location = location;
	}

	
	
	public ControlParameterUpdateStrategy getScaleStrategy() {
		return scaleStrategy;
	}

	public void setScaleStrategy(ControlParameterUpdateStrategy scaleStrategy) {
		this.scaleStrategy = scaleStrategy;
	}
	

}
