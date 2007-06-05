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

import java.util.Iterator;
import java.util.List;

import net.sourceforge.cilib.algorithm.initialisation.ClonedEntityInitialisationStrategy;
import net.sourceforge.cilib.algorithm.initialisation.InitialisationStrategy;
import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.cooperative.ParticipatingAlgorithm;
import net.sourceforge.cilib.ec.iterationstrategies.GeneticAlgorithmIterationStrategy;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.topologies.GBestTopology;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.problem.OptimisationSolution;

/**
 * Generic EC skeleton agorithm. The algorithm is altered by defining the
 * appropriate {@link net.sourceforge.cilib.ec.iterationstrategies.IterationStrategy}.
 * 
 * @author Gary Pampara
 */
public class EC extends PopulationBasedAlgorithm implements ParticipatingAlgorithm{
	private static final long serialVersionUID = -4324446523858690744L;
	
	private OptimisationProblem problem;
	private InitialisationStrategy initialisationStrategy;
	private IterationStrategy<EC> iterationStrategy;
	private Topology<? extends Entity> topology;
	private Entity bestEntity;
	protected boolean participation;
	
	
	public EC() {
		this.initialisationStrategy = new ClonedEntityInitialisationStrategy();
		this.initialisationStrategy.setEntityType(new Individual());
		
		this.iterationStrategy = new GeneticAlgorithmIterationStrategy();			
		this.topology = new GBestTopology<Individual>();
		this.participation = false;
	}
	
	@SuppressWarnings("unchecked")
	public EC(EC copy) {
		this.initialisationStrategy = copy.initialisationStrategy.clone();
		this.iterationStrategy = copy.iterationStrategy.clone();
		this.topology = copy.topology.clone();
		this.participation = copy.participation;
	}
	
	public EC clone() {
		return new EC(this);
	}
	
	@Override
	public void performInitialisation() {
		this.initialisationStrategy.initialise(this.topology, this.problem);
	}

	@Override
	public void algorithmIteration() {
		bestEntity = null;
		
		for (Iterator<? extends Entity> i = this.getTopology().iterator(); i.hasNext(); ) {
			Entity entity = i.next();
			//entity.setFitness(this.getOptimisationProblem().getFitness(entity.get(), true));
			entity.calculateFitness();
		}
		
		iterationStrategy.performIteration(this);
	}

	@Override
	public int getPopulationSize() {
		return this.initialisationStrategy.getEntities();
	}

	@Override
	public void setPopulationSize(int populationSize) {
		this.initialisationStrategy.setEntities(populationSize);
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

	@Override
	public void setOptimisationProblem(OptimisationProblem problem) {
		this.problem = problem;		
	}

	@Override
	public OptimisationProblem getOptimisationProblem() {
		return this.problem;
	}

	public OptimisationSolution getBestSolution() {
		OptimisationSolution solution = new OptimisationSolution(problem, getBestEntity().get().clone());
        
        return solution;
	}
	
	public IterationStrategy getIterationStrategy() {
		return iterationStrategy;
	}

	@SuppressWarnings("unchecked")
	public void setIterationStrategy(IterationStrategy iterationStrategy) {
		this.iterationStrategy = iterationStrategy;
	}

	public List<OptimisationSolution> getSolutions() {
		return null;
	}
	
	public Entity getBestEntity() {
		if (bestEntity == null) {
			Iterator<? extends Entity> i = topology.iterator();
			bestEntity = i.next();
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

	public Entity getContribution() {
		//TODO: This might not be what you want, change as desired
		return getBestEntity();
	}

	public Fitness getContributionFitness() {
		//TODO: This might not be what you want, change as desired
		return getBestEntity().getFitness();
	}

	public void updateContributionFitness(Fitness fitness) {
		//TODO: This might not be what you want, change as desired
		//getBestEntity().setFitness(fitness);
		getBestEntity().calculateFitness();
	}

	public boolean participated() {
		return participation;
	}

	public void participated(boolean p) {
		participation = p;
	}
}
