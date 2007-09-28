/*
 * DifferentialEvolutionIterationStrategy.java
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
package net.sourceforge.cilib.ec.iterationstrategies;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.ec.EC;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.math.random.RandomNumber;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.MixedVector;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Evolutionary Strategy to implement the Differential Evolutionary Algorithm.
 * 
 * @author Gary Pampara
 */
public class DifferentialEvolutionIterationStrategy extends IterationStrategy<EC> {
	private static final long serialVersionUID = 8019668923312811974L;	
	private RandomNumber random1;
	private RandomNumber random2;
	private ControlParameter crossoverProbability;
	private ControlParameter scaleParameter;
	
	public DifferentialEvolutionIterationStrategy() {
		this.random1 = new RandomNumber();
		this.random2 = new RandomNumber();
		
		this.crossoverProbability = new ConstantControlParameter(0.6);
		this.scaleParameter = new ConstantControlParameter(0.5);
	}
	
	public DifferentialEvolutionIterationStrategy(DifferentialEvolutionIterationStrategy copy) {
		this.random1 = copy.random1.clone();
		this.random2 = copy.random2.clone();
		
		this.crossoverProbability = copy.crossoverProbability;
		this.scaleParameter = copy.scaleParameter;
	}
	
	public DifferentialEvolutionIterationStrategy clone() {
		return new DifferentialEvolutionIterationStrategy(this);
	}

	/**
	 * Implementation of the Scheme 1 strategy for differential evolution 
	 */
	public void performIteration(EC ec) {
		Topology<? extends Entity> topology = ec.getTopology();
		
		for (Iterator<? extends Entity> iterator = topology.iterator(); iterator.hasNext(); ) {
			Entity current = iterator.next();
			//current.setFitness(ec.getOptimisationProblem().getFitness(current.get(), true));
			current.calculateFitness();
		
			List<Entity> parents = getRandomParentEntities(topology);
			
			Vector currentPosition = (Vector) current.getContents(); 
			Vector position1 = (Vector) parents.get(0).getContents();
			Vector position2 = (Vector) parents.get(1).getContents();
			Vector position3 = (Vector) parents.get(2).getContents();
			
			Vector trialVector = new MixedVector();
			
			int i = Double.valueOf(random1.getUniform(0, currentPosition.getDimension())).intValue();
			for (int j = 0; j < currentPosition.getDimension(); j++) {
				if ((random2.getUniform() < crossoverProbability.getParameter()) || (j == i)) {
					double value = position3.getReal(j);
					value += scaleParameter.getParameter() * (position1.getReal(j) - position2.getReal(j));
					
					trialVector.add(new Real(value));
				}
				else {
					trialVector.add(currentPosition.get(j));
				}
			}
			
			Fitness trialVectorFitness = ec.getOptimisationProblem().getFitness(trialVector, false);
			
			if (trialVectorFitness.compareTo(current.getFitness()) > 0) { // the trial vector is better than the parent
				current.setContents(trialVector);
			}
			
			//this.boundaryConstraint.enforce(current);
		}
	}	
	
	/**
	 * Get a list of individuals that are suitable to be used within
	 * the recombination arithmetic operator.
	 * @param topology The {@see net.sourceforge.cilib.entity.Topology Topology} containing the entites.
	 * @return A list of unique entities.
	 */
	private List<Entity> getRandomParentEntities(Topology<? extends Entity> topology) {
		List<Entity> parents = new ArrayList<Entity>(3);
		
		RandomNumber randomNumber = new RandomNumber();
		
		int count = 0;
		
		while (count < 3) {
			int random = randomNumber.getRandomGenerator().nextInt(topology.size());
			Entity parent = topology.get(random);
			if (!parents.contains(parent)) {
				parents.add(parent);
				count++;
			}
		}
		
		return parents;
	}

	public ControlParameter getCrossoverProbability() {
		return crossoverProbability;
	}

	public void setCrossoverProbability(ControlParameter crossoverProbability) {
		this.crossoverProbability = crossoverProbability;
	}

	public ControlParameter getScaleParameter() {
		return scaleParameter;
	}

	public void setScaleParameter(ControlParameter scaleParameter) {
		this.scaleParameter = scaleParameter;
	}

}
