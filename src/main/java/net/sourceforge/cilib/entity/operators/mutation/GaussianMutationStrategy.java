/*
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
package net.sourceforge.cilib.entity.operators.mutation;

import java.util.List;
import java.util.ListIterator;

import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.controlparameter.ProportionalControlParameter;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @author Andries Engelbrecht
 * @author Gary Pampara
 */
public class GaussianMutationStrategy extends MutationStrategy {

	private double mean;
	private ControlParameter deviationStrategy;
	
	public GaussianMutationStrategy() {
		super();
		this.mean = 0;
		this.deviationStrategy = new ProportionalControlParameter();
	}
	
	public GaussianMutationStrategy(GaussianMutationStrategy copy) {
		super(copy);
		this.mean = copy.mean;
		this.deviationStrategy = copy.deviationStrategy.getClone();
	}
	
	public GaussianMutationStrategy getClone() {
		return new GaussianMutationStrategy(this);
	}
	
	public void mutate(List<? extends Entity> entity) {
		
		for (ListIterator<? extends Entity> individual = entity.listIterator(); individual.hasNext();) {
			Entity current = individual.next(); 
			Vector chromosome = (Vector) current.getCandidateSolution();
			
			for (int i = 0; i < chromosome.getDimension(); i++) {
				double random = this.getRandomNumber().getUniform(); 
				if (random <= this.getMutationProbability().getParameter()) {
					double deviation = this.getRandomNumber().getGaussian();
					double value = this.getOperatorStrategy().evaluate(chromosome.getReal(i), this.getRandomNumber().getGaussian(this.mean, deviation));
									
					chromosome.setReal(i, value);
				}
			}
		}
	}

	public ControlParameter getDeviationStrategy() {
		return deviationStrategy;
	}

	public void setDeviationStrategy(ControlParameter deviationStrategy) {
		this.deviationStrategy = deviationStrategy;
	}

	public double getMean() {
		return mean;
	}

	public void setMean(double mean) {
		this.mean = mean;
	}

	public void performOperation(Topology<? extends Entity> topology, Topology<Entity> offspring) {
		this.mutate(offspring);		
	}
	
}
