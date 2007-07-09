/*
 * OnePointCrossoverStrategy.java
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
package net.sourceforge.cilib.entity.operators.crossover;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.type.types.container.Vector;

/**
*
* @author Andries Engelbrecht
* @author Gary Pampara
*/
public class OnePointCrossoverStrategy extends CrossoverStrategy {
	
	public OnePointCrossoverStrategy() {
		super();
	}
	
	public OnePointCrossoverStrategy(OnePointCrossoverStrategy copy) {
		super(copy);
	}
	
	public OnePointCrossoverStrategy clone() {
		return new OnePointCrossoverStrategy(this);
	}
	
	@Override
	public List<Entity> crossover(Topology<? extends Entity> parentCollection) {
		ArrayList<Entity> offspring = new ArrayList<Entity>();
		offspring.ensureCapacity(parentCollection.size());
		
		Collections.shuffle(parentCollection); // This should be a selectionstrategy on the entire population
		
		for (int i = 0; i < parentCollection.size(); i++) {
			// This needs a selection strategy to select the parent individuals!!!!
			Entity parent1 = parentCollection.get(this.getRandomNumber().getRandomGenerator().nextInt(parentCollection.size()));
			Entity parent2 = parentCollection.get(this.getRandomNumber().getRandomGenerator().nextInt(parentCollection.size()));
			
			if (this.getRandomNumber().getUniform() <= this.getCrossoverProbability().getParameter()) {
				// Select the pivot point where crossover will occour
				int maxLength = Math.min(parent1.getDimension(), parent2.getDimension());
				int crossoverPoint = Double.valueOf(this.getRandomNumber().getUniform(0, maxLength+1)).intValue(); 
				
				Entity offspring1 = parent1.clone();
				Entity offspring2 = parent2.clone();
				
				Vector offspringVector1 = (Vector) offspring1.getContents();
				Vector offspringVector2 = (Vector) offspring2.getContents();
				
				for (int j = crossoverPoint; j < offspringVector2.getDimension(); j++) {
					offspringVector1.remove(j);
					offspringVector1.insert(j, offspringVector2.get(j));
				}
				
				for (int j = crossoverPoint; j < offspringVector1.getDimension(); j++) {
					offspringVector2.remove(j);
					offspringVector2.insert(j, offspringVector1.get(j));
				}
				
				//OptimisationProblem problem = ((PopulationBasedAlgorithm) Algorithm.get()).getOptimisationProblem();
//				offspring1.setFitness(problem.getFitness(offspring1.get(), false));
//				offspring2.setFitness(problem.getFitness(offspring2.get(), false));
				offspring1.calculateFitness(false);
				offspring2.calculateFitness(false);
						
				offspring.add(offspring1);
				offspring.add(offspring2);
			}
		}
		
		return offspring;
	}

	public void performOperation(Topology<? extends Entity> topology, Topology<Entity> offspring) {
		offspring.addAll(this.crossover(topology));
	}
}
