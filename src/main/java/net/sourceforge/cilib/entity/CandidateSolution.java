/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.entity;

import net.sourceforge.cilib.problem.Fitness;
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
