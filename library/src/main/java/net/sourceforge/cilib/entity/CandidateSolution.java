/**           __  __
 *    _____ _/ /_/ /_    Computational Intelligence Library (CIlib)
 *   / ___/ / / / __ \   (c) CIRG @ UP
 *  / /__/ / / / /_/ /   http://cilib.net
 *  \___/_/_/_/_.___/
 */
package net.sourceforge.cilib.entity;

import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.type.types.Blackboard;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.util.Cloneable;

/**
 * Interface to describe the notion of a <code>CandidateSoution</code>. In general
 * a <code>CandidateSolution</code> contains a {@linkplain Type} to describe the
 * solution the <code>CandidateSolution</code> represents together with its
 * associated {@linkplain Fitness} value.
 */
public interface CandidateSolution extends Cloneable {

    /**
     * Get the contents of the <code>CandidateSoltion</code>. i.e.: The
     * potential solution.
     * @return A {@linkplain Type} representing the solution.
     */
    StructuredType getCandidateSolution();

    /**
     * Set the solution that the <code>CandidateSolution</code> represents.
     * @param contents The potential solution to set.
     */
    void setCandidateSolution(StructuredType contents);

    /**
     * Obtain the {@linkplain Fitness} of the current <code>CandidateSolution</code>.
     * @return The current {@linkplain Fitness} value.
     */
    Fitness getFitness();

    /**
     * Get the properties associated with this {@code CandidateSolution}.
     * @return The properties of the {@code CandidateSolution}.
     */
    Blackboard<Enum<?>, Type> getProperties();

}
