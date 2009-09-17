/**
 * Copyright (C) 2003 - 2009
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

import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.problem.OptimisationProblem;
import net.sourceforge.cilib.type.types.Blackboard;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.util.Cloneable;
import net.sourceforge.cilib.util.calculator.FitnessCalculator;

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
     * Get the current {@code FitnessCalculator} for the current {@code Entity}.
     * @return The {@code FitnessCalculator} associated with this {@code Entity}.
     */
    public FitnessCalculator getFitnessCalculator();

}
