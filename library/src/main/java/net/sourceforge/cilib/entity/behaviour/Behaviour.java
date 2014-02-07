/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity.behaviour;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.util.Cloneable;
import net.sourceforge.cilib.util.calculator.FitnessCalculator;

/**
 * Interface for behaviours of entities. These are the behaviours that makes
 * an entity behave in accordance to a particular algorithm.
 */
public interface Behaviour extends Comparable<Behaviour>, Cloneable {

    /**
     * {@inheritDoc}
     */
    @Override
    public Behaviour getClone();

    /**
     * Performs a single iteration of an algorithm on an entity.
     *
     * @param entity The {@link Entity} to have the behaviour applied.
     */
    public Entity performIteration(Entity entity);

    /**
     * Gets the fitness calculator.
     * 
     * @return The fitness calculator.
     */
    public FitnessCalculator getFitnessCalculator();

    public void setFitnessCalculator(FitnessCalculator fitnessCalculator);

    /**
     * Increment the number of times this behavior was successful
     */
    public void incrementSuccessCounter();

    /**
     * Increment the number of times this behavior has been selected
     */
    public void incrementSelectedCounter();

    /**
     * Get the number of times this behavior has been selected
     */
    public int getSelectedCounter();

    /**
     * Get the number of times this behavior was successful
     */
    public int getSuccessCounter();

    /**
     * Reset the selected counter to zero.
     */
    public void resetSelectedCounter();

    /**
     * Reset the success counter to zero.
     */
    public void resetSuccessCounter();

    /**
     * Get the number of times this behavior has been selected
     */
    public void setSelectedCounter(int n);

    /**
     * Get the number of times this behavior was successful
     */
    public void setSuccessCounter(int n);

    /**
     * Compare two behaviors with regards to how successful they were in finding
     * better fitness values.
     * @param o The {@link ParticleBehavior} object to compare with this object.
     * @return -1 if this behavior was less successful, 0 if the two behaviors were equally successful, 1 otherwise.
     */
    @Override
    public int compareTo(Behaviour o);
}
