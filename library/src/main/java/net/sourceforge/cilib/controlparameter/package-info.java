/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */

package net.sourceforge.cilib.controlparameter;

/**
 * <p>A {@linkplain ControlParameter} is a generalised parameter type within CIlib.</p>
 * <p>
 * {@linkplain Algorithm}s require parameters in order to alter and configure the
 * manner in which they operate. These parameters could represent the number of
 * {@linkplain Individual}s within a Genetic Algorithm, or even the degree of mutation
 * which decreases over time as the {@linkplain Algorithm} runs to completion.
 * </p>
 * <p>
 * {@linkplain ControlParameter} instances provide a standard interface for these
 * parameter types, which can be updated over the course of an algorithm run.
 * </p>
 * <p>
 * Substituting differing {@linkplain ControlParameter} instances, therefore, results
 * in a simple modification to the desired {@linkplain Algorithm} without any code
 * changes.
 * </p>
 */
