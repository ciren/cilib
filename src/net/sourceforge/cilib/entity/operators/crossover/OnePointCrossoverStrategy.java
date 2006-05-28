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
import java.util.List;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.type.types.Vector;

/**
*
* @author  Andries Engelbrecht
*/

public class OnePointCrossoverStrategy extends CrossoverStrategy {

	@Override
	public List<Entity> crossover(List<? extends Entity> parentCollection) {
		
		//How do we handle variable sizes? Resizing the entities?
		Entity offspring1 = parentCollection.get(0).clone();
		Entity offspring2 = parentCollection.get(1).clone();
		
		if (this.getCrossoverProbability().getParameter() >= this.getRandomNumber().getUniform()) {
			
			Vector parentChromosome1 = (Vector) parentCollection.get(0).get();
			Vector parentChromosome2 = (Vector) parentCollection.get(1).get();
			Vector offspringChromosome1 = (Vector) offspring1.get();
			Vector offspringChromosome2 = (Vector) offspring2.get();
			
			int sizeParent1 = parentChromosome1.getDimension();
			int sizeParent2 = parentChromosome2.getDimension();
				
			int selectRange = Math.min(sizeParent1, sizeParent2);		
			//int maxDimension = Math.max(sizeParent1, sizeParent2);
					
			int crossoverPoint = (int) this.getRandomNumber().getUniform(0, selectRange);
		
			Vector remainder1 = offspringChromosome1.subVector(crossoverPoint, offspringChromosome1.getDimension());
			Vector remainder2 = offspringChromosome2.subVector(crossoverPoint, offspringChromosome2.getDimension());

			// This needs to be tested !!!
			for (int i = 0; i < remainder2.size(); i++) {
				if (offspringChromosome1.size() <= crossoverPoint+i)
					offspringChromosome1.add(remainder2.get(i));
				else
					offspringChromosome1.set(crossoverPoint+i, remainder2.get(i));
			}
			
			for (int i = crossoverPoint; i < remainder1.size(); i++) {
				if (offspringChromosome2.size() <= crossoverPoint+i)
					offspringChromosome2.add(remainder1.get(i));
				else
					offspringChromosome1.set(crossoverPoint+i, remainder1.get(i));
			}
		}
		
		List<Entity> offspring = new ArrayList<Entity>();
		offspring.add(offspring1);
		offspring.add(offspring2);
		
		return offspring;
	}
}
