/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */

package net.sourceforge.cilib.problem.changestrategy;

/**
 * Dynamic environments need to know when it should be applicable to change
 * the problem.
 * <p>
 * Various strategies exist to do this, but for CIlib the simple process
 * to enable this is to implement the {@code ChangeStrategy} and attach
 * it to the problem.
 * </p>
 * <p>
 * {@code ChangeStrategy} classes implement a simple boolean function which
 * determines if a change should occur or not.
 * </p>
 * <p>
 * {@code ChangeStrategy} classes are used as follows:
 * </p>
 * <pre>
 * if (changeStrategy.shouldApply(problem))
 *     problem.changeEnvironment();
 * </pre>
 */
