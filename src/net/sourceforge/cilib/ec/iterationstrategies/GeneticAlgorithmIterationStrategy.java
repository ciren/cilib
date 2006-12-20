/*
 * GeneticAlgorithmIterationStrategy.java
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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.cilib.ec.EC;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.comparator.AscendingFitnessComparator;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.UniformCrossoverStrategy;
import net.sourceforge.cilib.entity.operators.mutation.GaussianMutationStrategy;
import net.sourceforge.cilib.entity.operators.mutation.MutationStrategy;

/**
 * 
 * @author Gary Pampara
 */
public class GeneticAlgorithmIterationStrategy implements IterationStrategy {
	
	private CrossoverStrategy crossoverStrategy;
	private MutationStrategy mutationStrategy;
	
	public GeneticAlgorithmIterationStrategy() {
		this.crossoverStrategy = new UniformCrossoverStrategy();
		this.mutationStrategy = new GaussianMutationStrategy();
	}
	
	public GeneticAlgorithmIterationStrategy(GeneticAlgorithmIterationStrategy copy) {
		this.crossoverStrategy = copy.crossoverStrategy.clone();
		this.mutationStrategy = copy.mutationStrategy.clone();
	}
	
	public GeneticAlgorithmIterationStrategy clone() {
		return new GeneticAlgorithmIterationStrategy(this);
	}

	public void perfromIteration(EC ec) {
		// Cacluate the fitness
		for (Iterator<? extends Entity> i = ec.getTopology().iterator(); i.hasNext(); ) {
			Entity entity = i.next();
			entity.setFitness(ec.getOptimisationProblem().getFitness(entity.get(), true));
		}
		
		// Perform crossover
		List<Entity> crossedOver = this.crossoverStrategy.crossover(ec.getTopology());
				
		// Perform mutation on offspring
		this.mutationStrategy.mutate(crossedOver);
		
		// Evaluate the fitness values of the generated offspring
		for (Iterator<Entity> offspring = crossedOver.iterator(); offspring.hasNext(); ) {
			Entity entity = offspring.next();
			entity.setFitness(ec.getOptimisationProblem().getFitness(entity.get(), false));
		}
		
		// Perform new population selection
		Topology<Entity> topology = (Topology<Entity>) ec.getTopology();
		for (Iterator<Entity> i = crossedOver.iterator(); i.hasNext(); ) {
			Entity entity = i.next();
			topology.add(entity);
		} 
		
		Collections.sort(ec.getTopology(), new AscendingFitnessComparator());
		for (ListIterator<? extends Entity> i = ec.getTopology().listIterator(ec.getPopulationSize()); i.hasNext(); ) {
			i.next();
			i.remove();
		}

		crossedOver.clear();
		crossedOver = null;
	}

}
