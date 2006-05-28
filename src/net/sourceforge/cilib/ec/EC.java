/*
 * EC.java
 * 
 * Created on 28 May, 2006.
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
package net.sourceforge.cilib.ec;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.cilib.algorithm.PopulationBasedAlgorithm;
import net.sourceforge.cilib.algorithm.initialisation.ClonedEntityInitialisationStrategy;
import net.sourceforge.cilib.algorithm.initialisation.InitialisationStrategy;
import net.sourceforge.cilib.ec.ea.Individual;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.comparator.AscendingFitnessComparator;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.UniformCrossoverStrategy;
import net.sourceforge.cilib.entity.operators.mutation.GaussianMutationStrategy;
import net.sourceforge.cilib.entity.operators.mutation.MutationStrategy;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;

/**
 * 
 * @author Gary Pampara
 */
public class EC extends PopulationBasedAlgorithm {
	
	//private static Logger log = Logger.getLogger(EC.class);
	
	private OptimisationProblem problem;
	private InitialisationStrategy intialisationStrategy;
	private Topology<Individual> topology;
	
	private Entity bestEntity;
	
	//Refactor? Iteration strategy???
	private CrossoverStrategy crossoverStrategy;
	private MutationStrategy mutationStrategy;
		
	
	public EC() {
		this.intialisationStrategy = new ClonedEntityInitialisationStrategy();
		this.intialisationStrategy.setEntityType(new Individual());
		
		this.topology = new GBestTopology<Individual>();
		
		this.crossoverStrategy = new UniformCrossoverStrategy();
		this.mutationStrategy = new GaussianMutationStrategy();
	}
	
	
	@Override
	public void performInitialisation() {
		this.intialisationStrategy.intialise(this.topology, this.problem);
	}
	

	@Override
	protected void performIteration() {
		bestEntity = null;
		
		// Cacluate the fitness
		for (Individual indiv : topology) {
			indiv.setFitness(problem.getFitness(indiv.get(), true));
		}
		
		// Perform crossover
		List<Entity> crossedOver = this.crossoverStrategy.crossover(topology.getAll());
				
		// Perform mutation on offspring
		this.mutationStrategy.mutate(crossedOver);
		
		//System.out.println(crossedOver);
		
		// Perform new population selection
		for (Entity entity : crossedOver) {
			topology.add((Individual) entity);
		}
		
		//log.info("topology size: " + topology.size());
		
		Collections.sort(topology, new AscendingFitnessComparator());
		
		//log.info("population size: " + this.getPopulationSize());

		for (ListIterator<Individual> iterator = topology.listIterator(this.getPopulationSize()); iterator.hasNext(); ) {
			iterator.next();
			iterator.remove();
		}
		
		//log.info("new toipology size: " + topology.size());
	}

	@Override
	public int getPopulationSize() {
		return this.intialisationStrategy.getEntities();
	}

	@Override
	public void setPopulationSize(int populationSize) {
		this.intialisationStrategy.setEntities(populationSize);
	}

	@Override
	public Topology<? extends Entity> getTopology() {
		return this.topology;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setTopology(Topology topology) {
		this.topology = topology;
	}

	@Override
	public double getDiameter() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getRadius() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setOptimisationProblem(OptimisationProblem problem) {
		this.problem = problem;		
	}

	public OptimisationProblem getOptimisationProblem() {
		return this.problem;
	}

	public OptimisationSolution getBestSolution() {
		OptimisationSolution solution = new OptimisationSolution(problem, getBestEntity().get().clone());
        
        return solution;
	}

	public Collection<OptimisationSolution> getSolutions() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Entity getBestEntity() {
		 if (bestEntity == null) {
	        	Iterator<? extends Entity> i = topology.iterator();
	            bestEntity =  i.next();
	            Fitness bestFitness = bestEntity.getFitness();
	            while (i.hasNext()) {
	                Entity entity = i.next();
	                if (entity.getFitness().compareTo(bestFitness) > 0) {
	                    bestEntity = entity;
	                    bestFitness = bestEntity.getFitness();
	                }
	            }
	        }
	        return bestEntity;
	}

}
