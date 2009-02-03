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
package net.sourceforge.cilib.entity;

import java.io.Serializable;

import java.util.Comparator;
import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.pso.positionupdatestrategies.NeighbourhoodBestUpdateStrategy;
import net.sourceforge.cilib.type.types.Blackboard;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.util.Cloneable;

/**
 * This is the super interface which all corresponding entity implementation and extending interfaces,
 * for use in population based algorithms, must implement/extend from. 
 */
public interface Entity extends Comparable<Entity>, Cloneable, Serializable {
	
	/**
	 * Make a clone of the Entity the exact semantics of the clone method will be defined by the classes
	 * that implements this interface.
	 * @return the cloned object
	 */
	@Override
	public Entity getClone();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(Entity o);
	
	/**
	 * Get the candidate solution of the entity. The contents will depend on the subclass.
	 * Eg. genotypes for Individual
	 *     position for Particle
	 *     tour for Ant
	 *     etc...
	 *     
	 * @return The {@linkplain Type} representing the contents of the {@linkplain Entity}.
	 */ 
	public StructuredType getCandidateSolution();
	
	/**
	 * Set the candidate solution of the current {@linkplain Entity} to the provided {@linkplain Type}.
	 * @param type the {@linkplain Type} to be set as the contents of the {@linkplain Entity}.
	 */
	public void setCandidateSolution(StructuredType type);
	
	/**
	 * Calculate the fitness of the <code>Entity</code> incrementing the
	 * number of fitness evaluations for the algorithm.
	 */
	public void calculateFitness();
	
	/**
	 * Calculate the fitness of the <code>Entity</code>. This method may or may not
	 * increment the number of fitness evaluations of the algorithm.
	 * @param count Add or do not add this fitness evaluation to the algorithm global count.
	 */
	public void calculateFitness(boolean count);
	
	/**
	 * Returns the {@linkplain Entity} fitness.
	 * @return The {@linkplain Fitness} of the {@linkplain Entity}.
	 */
	public Fitness getFitness();
	
	/**
	 * Return the best fitness associated with this {@linkplain Entity}, provided a best
	 * fitness is defined on the {@linkplain Entity}. If a best fitness is not defined, the
	 * current fitness is returned as it is the best current fitness. {@linkplain Entity}
	 * objects that need to use this method need to override it in their implementation,
	 * for example with {@linkplain Particle} objects.
	 * @return The associated best {@linkplain Fitness} value. 
	 */
	public Fitness getBestFitness();
	
	/**
	 * Get the social best fitness of the {@linkplain Entity}. The social best is determined
	 * but the predefined {@linkplain NeighbourhoodBestUpdateStrategy}.
	 * @return The social best fitness value.
	 */
	public Fitness getSocialBestFitness();
	
	/**
	 * Intialise the Entity to something meaningful and within the problem space.
	 * The exact semantics of this method is defined by the classes that implements this interface.
	 * Take note. The Intialiser is obsolete the new Type System handles it now. Init can be left out now.
	 * 
	 * @param problem The {@linkplain OptimisationProblem} to based the initialisation on.
	 */
	public void initialise(OptimisationProblem problem);
	
	/**
	 * Returns the dimension of the {@linkplain Entity}.
	 * @return The dimension of the {@linkplain Entity}.
	 */
	public int getDimension();
	
	/**
	 * Re-initialise the given {@linkplain Entity} within the defined domain.
	 */
	public void reinitialise();
	
	/**
	 * Get the properties associate with the <code>Entity</code>.
	 * @return The {@linkplain Blackboard} containing the properties.
	 */
	public Blackboard<Enum<?>, Type> getProperties();

	/**
	 * Set the properties for the current <code>Entity</code>.
	 * @param properties The properties to set.
	 */
	public void setProperties(Blackboard<Enum<?>, Type> properties);

	/**
	 * Get the identifier associated with the {@code Entity} instance.
	 * @return The associated identifier.
	 */
	public long getId();

	/**
	 * Obtain the required {@link Comparator}. In general this method
	 * will return the type of {@link Comparator} based on the
	 * {@link Fitness} object assocaited with the current {@link Entity}.
	 * @return The correct comparator for the {@link Entity}. In general
	 *         a {@link net.sourceforge.cilib.entity.comparator.AscendingFitnessComparator} 
	 *         will be returned for minimization problems, with a
	 *         {@link net.sourceforge.cilib.entity.comparator.DescendingFitnessComparator}
	 *         returned for maximization problems.
	 */
	public Comparator<Entity> getComparator();
}
