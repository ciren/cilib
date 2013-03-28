/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity;

import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.type.types.Blackboard;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.util.Cloneable;
import net.sourceforge.cilib.util.calculator.FitnessCalculator;

/**
 * This is the super interface which all corresponding entity implementation and
 * extending interfaces, for use in population based algorithms, must
 * implement/extend from.
 */
public interface Entity extends Comparable<Entity>, Cloneable {

    /**
     * Make a clone of the {@linkplain Entity} the exact semantics of the clone
     * method will be defined by the classes that implements this interface.
     *
     * @return the cloned object
     */
    @Override
    Entity getClone();

    /**
     * Get the candidate solution of the entity. The contents will depend on the subclass.
     * Eg. genotypes for Individual, position for Particle, tour for Ant, etc...
     *
     * @return The {@linkplain Type} representing the contents of the {@linkplain Entity}.
     */
    StructuredType getCandidateSolution();

    /**
     * Set the candidate solution of the current {@linkplain Entity} to the provided {@linkplain Type}.
     * @param type the {@linkplain Type} to be set as the contents of the {@linkplain Entity}.
     */
    void setCandidateSolution(StructuredType type);

    /**
     * Calculate the fitness of the {@linkplain Entity} incrementing the
     * number of fitness evaluations for the algorithm.
     */
    void calculateFitness();

    /**
     * Returns the {@linkplain Entity} fitness.
     *
     * @return The {@linkplain Fitness} of the {@linkplain Entity}.
     */
    Fitness getFitness();

    /**
     * Return the best fitness associated with this {@linkplain Entity}, provided a best
     * fitness is defined on the {@linkplain Entity}. If a best fitness is not defined, the
     * current fitness is returned as it is the best current fitness. {@linkplain Entity}
     * objects that need to use this method need to override it in their implementation,
     * for example with {@linkplain net.sourceforge.cilib.pso.particle.Particle}
     * objects.
     *
     * @return The associated best {@linkplain Fitness} value.
     */
    Fitness getBestFitness();

    /**
     * Intialise the Entity to something meaningful and within the problem
     * space. The exact semantics of this method is defined by the classes that
     * implements this interface. Take note. The Intialiser is obsolete the new
     * {@linkplain Type Type System} handles it now. Init can be left out now.
     *
     * @param problem The {@linkplain Problem} to based the initialisation on.
     */
    void initialise(Problem problem);

    /**
     * Returns the dimension of the {@linkplain Entity}.
     *
     * @return The dimension of the {@linkplain Entity}.
     */
    int getDimension();

    /**
     * Re-initialise the given {@linkplain Entity} within the defined domain.
     */
    void reinitialise();

    /**
     * Get the properties associate with the {@linkplain Entity}.
     *
     * @return The {@linkplain Blackboard} containing the properties.
     */
    Blackboard<Enum<?>, Type> getProperties();

    /**
     * Get the identifier associated with the {@linkplain Entity} instance.
     *
     * @return The associated identifier.
     */
    long getId();

    /**
     * Get the current {@code FitnessCalculator} for the current {@code Entity}.
     *
     * @return The {@code FitnessCalculator} associated with this {@code Entity}.
     */
    FitnessCalculator getFitnessCalculator();

}
