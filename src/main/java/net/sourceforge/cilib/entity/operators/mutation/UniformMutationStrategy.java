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

import java.util.List;
import java.util.ListIterator;

import net.sourceforge.cilib.controlparameterupdatestrategies.ControlParameter;
import net.sourceforge.cilib.controlparameterupdatestrategies.ProportionalControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @author Andries Engelbrecht
 * @author Gary Pampara
 */
public class UniformMutationStrategy extends MutationStrategy {

	private ControlParameter minStrategy, maxStrategy;
	
	public UniformMutationStrategy() {
		super();
		minStrategy = new ProportionalControlParameter();
		maxStrategy = new ProportionalControlParameter();
	}
	
	public UniformMutationStrategy(UniformMutationStrategy copy) {
		super(copy);
		this.minStrategy = copy.minStrategy.clone();
		this.maxStrategy = copy.maxStrategy.clone();
	}
	
	public UniformMutationStrategy clone() {
		return new UniformMutationStrategy(this);
	}
	
	
	@Override
	public void mutate(List<? extends Entity> entity) {
		for (ListIterator<? extends Entity> individual = entity.listIterator(); individual.hasNext(); ) {
			Entity current = individual.next(); 
			Vector chromosome = (Vector) current.getContents();
			
			if (this.getMutationProbability().getParameter() >= this.getRandomNumber().getUniform()) {
				for (int i = 0; i < chromosome.getDimension(); i++) {
					double value;
									
					value = this.getOperatorStrategy().evaluate(chromosome.getReal(i), this.getRandomNumber().getUniform(minStrategy.getParameter(),maxStrategy.getParameter()));
									
					chromosome.setReal(i, value);
				}
			}
	
		}
	}

	public void performOperation(Topology<? extends Entity> topology, Topology<Entity> offspring) {
		// TODO Auto-generated method stub
		this.mutate(offspring);
	}

}
