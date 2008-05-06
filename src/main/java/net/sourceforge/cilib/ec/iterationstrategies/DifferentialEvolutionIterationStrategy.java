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
package net.sourceforge.cilib.ec.iterationstrategies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.cilib.algorithm.population.IterationStrategy;
import net.sourceforge.cilib.controlparameter.ConstantControlParameter;
import net.sourceforge.cilib.controlparameter.ControlParameter;
import net.sourceforge.cilib.ec.EC;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.entity.operators.crossover.CrossoverStrategy;
import net.sourceforge.cilib.entity.operators.crossover.DifferentialEvolutionBinomialCrossover;
import net.sourceforge.cilib.entity.operators.selection.RandomSelectionStrategy;
import net.sourceforge.cilib.entity.operators.selection.SelectionStrategy;
import net.sourceforge.cilib.math.random.RandomNumber;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Evolutionary Strategy to implement the Differential Evolutionary Algorithm.
 * 
 * @author Gary Pampara
 */
public class DifferentialEvolutionIterationStrategy extends IterationStrategy<EC> {
	private static final long serialVersionUID = 8019668923312811974L;	
	private RandomNumber random;
	private ControlParameter crossoverProbability;
	private ControlParameter scaleParameter;
	private ControlParameter numberOfDifferenceVectors;
	
	private SelectionStrategy targetVectorSelectionStrategy; // x
	private CrossoverStrategy crossoverStrategy; // z

	/**
	 * Create an instance of the {@linkplain DifferentialEvolutionIterationStrategy}.
	 */
	public DifferentialEvolutionIterationStrategy() {
		this.random = new RandomNumber();
		
		this.crossoverProbability = new ConstantControlParameter(0.6);
		this.scaleParameter = new ConstantControlParameter(0.5);
		this.numberOfDifferenceVectors = new ConstantControlParameter(1);
		
		this.targetVectorSelectionStrategy = new RandomSelectionStrategy();
		this.crossoverStrategy = new DifferentialEvolutionBinomialCrossover();
	}
	
	/**
	 * Copy constructor. Create a copy of the given instance.
	 * @param copy The instance to copy.
	 */
	public DifferentialEvolutionIterationStrategy(DifferentialEvolutionIterationStrategy copy) {
		this.random = copy.random.getClone();
		
		this.crossoverProbability = copy.crossoverProbability.getClone();
		this.scaleParameter = copy.scaleParameter.getClone();
		this.numberOfDifferenceVectors = copy.numberOfDifferenceVectors.getClone();
		
		this.targetVectorSelectionStrategy = copy.targetVectorSelectionStrategy.getClone();
		this.crossoverStrategy = copy.crossoverStrategy.getClone();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public DifferentialEvolutionIterationStrategy getClone() {
		return new DifferentialEvolutionIterationStrategy(this);
	}

	/**
	 * Perform an iteration of the DE algorithm defined as the DE/x/y/z implementation.
	 * @param ec The {@linkplain EC} on which to perform this iteration.
	 */
	public void performIteration(EC ec) {
		Topology<? extends Entity> topology = ec.getTopology();
		
		for (Entity current : topology) {
			current.calculateFitness();
			
			// Create the trial vector by applying mutation
			Entity targetEntity = targetVectorSelectionStrategy.select(topology);
			while (targetEntity == current)
				targetEntity = targetVectorSelectionStrategy.select(topology);
			
			List<Entity> participants = selectEntities(current, topology);
			Vector differenceVector = determineDistanceVector(participants);
			
			Vector targetVector = (Vector) targetEntity.getContents();
			Vector trialVector = targetVector.plus(differenceVector.multiply(scaleParameter.getParameter()));
			
			// Create the offspring by applying cross-over
			Entity trialEntity = current.getClone();
			trialEntity.setContents(trialVector);
			List<Entity> offspring = this.crossoverStrategy.crossover(Arrays.asList(current, trialEntity)); // Order is VERY important here!!
			
			// Replace the parent (current) if the offspring is better
			Entity offspringEntity = offspring.get(0);
			offspringEntity.calculateFitness(false);
			
			if (offspringEntity.getFitness().compareTo(current.getFitness()) > 0) { // the trial vector is better than the parent
				current.setContents(offspring.get(0).getContents());
			}
		}
	}	

	/**
	 * Calculate the {@linkplain Vector} that is the resultant of several difference vectors.
	 * @param participants The {@linkplain Entity} list to create the difference vectors from. It
	 *        is very important to note that the {@linkplain Entity} objects within this list
	 *        should not contain duplicates. If duplicates are contained, this will severely
	 *        reduce the diversity of the population as not all entities will be considered.
	 * @return A {@linkplain Vector} representing the resultant of all calculated difference vectors.
	 */
	private Vector determineDistanceVector(List<Entity> participants) {
		Vector distanceVector = new Vector(participants.get(0).getContents().getDimension(), new Real(0.0));
		Iterator<Entity> iterator = participants.iterator();
		
		while (iterator.hasNext()) {
			Vector first = (Vector) iterator.next().getContents();
			Vector second = (Vector) iterator.next().getContents();
			
			Vector difference = first.subtract(second);
			distanceVector = distanceVector.plus(difference);
		}
		
		return distanceVector;
	}

	/**
	 * This private method implements the "y" part of the DE/x/y/z structure.
	 * @param current The current {@linkplain Entity} in the {@linkplain Topology}
	 * @param topology The current population.
	 * @return {@linkplain List} containing the entities to be used in the calculation of
	 *         the difference vectors.
	 */
	private List<Entity> selectEntities(Entity current, Topology<? extends Entity> topology) {
		SelectionStrategy randomSelectionStrategy = new RandomSelectionStrategy();
		List<Entity> participants = new ArrayList<Entity>();
		
		int total = 2 * Double.valueOf(this.numberOfDifferenceVectors.getParameter()).intValue();
		int i = 0;
		
		while (i < total) {
			Entity entity = randomSelectionStrategy.select(topology);
		
			if (participants.contains(entity)) continue;
			if (current == entity) continue;
			
			participants.add(entity);
			i++;
		}
		
		return participants;
	}

	/**
	 * Get the {@linkplain ControlParameter} defining the cross-over probability.
	 * @return The cross-over probability.
	 */
	public ControlParameter getCrossoverProbability() {
		return crossoverProbability;
	}

	/**
	 * Set the cross-over probability {@linkplain ControlParameter}.
	 * @param crossoverProbability The cross-over probability to set.
	 */
	public void setCrossoverProbability(ControlParameter crossoverProbability) {
		this.crossoverProbability = crossoverProbability;
	}

	/**
	 * Get the {@linkplain ControlParameter} defining the scale parameter.
	 * @return The scale parameter.
	 */
	public ControlParameter getScaleParameter() {
		return scaleParameter;
	}

	/**
	 * Set the {@linkplain ControlParameter} defining the scale parameter.
	 * @param scaleParameter The {@linkplain ControlParameter} to set.
	 */
	public void setScaleParameter(ControlParameter scaleParameter) {
		this.scaleParameter = scaleParameter;
	}

	/**
	 * Get the number of difference vectors to create.
	 * @return The {@linkplain ControlParameter} representing the number of 
	 *         difference vectors to generate.
	 */
	public ControlParameter getNumberOfDifferenceVectors() {
		return numberOfDifferenceVectors;
	}

	/**
	 * Set the number of difference vectors to create during the calculation
	 * of the trial vector within the DE. 
	 * @param numberOfDifferenceVectors The {@linkplain ControlParameter} defining the 
	 *        number of difference vectors to create. 
	 */
	public void setNumberOfDifferenceVectors(ControlParameter numberOfDifferenceVectors) {
		this.numberOfDifferenceVectors = numberOfDifferenceVectors;
	}

	/**
	 * Obtain the {@linkplain SelectionStrategy} used to select the target vector.
	 * @return The {@linkplain SelectionStrategy} of the target vector.
	 */
	public SelectionStrategy getTargetVectorSelectionStrategy() {
		return targetVectorSelectionStrategy;
	}

	/**
	 * Set the {@linkplain SelectionStrategy} used to select the target vector within the DE.
	 * @param targetVectorSelectionStrategy The {@linkplain SelectionStrategy} to use for the 
	 *        selection of the target vector.
	 */
	public void setTargetVectorSelectionStrategy(SelectionStrategy targetVectorSelectionStrategy) {
		this.targetVectorSelectionStrategy = targetVectorSelectionStrategy;
	}

	/**
	 * Get the {@linkplain CrossoverStrategy} used to create offspring entities.
	 * @return The {@linkplain CrossoverStrategy} used to create offspring.
	 */
	public CrossoverStrategy getCrossoverStrategy() {
		return crossoverStrategy;
	}

	/**
	 * Set the {@linkplain CrossoverStrategy} used to create offspring entities.
	 * @param crossoverStrategy The {@linkplain CrossoverStrategy} to create entities.
	 */
	public void setCrossoverStrategy(CrossoverStrategy crossoverStrategy) {
		this.crossoverStrategy = crossoverStrategy;
	}
	
}
