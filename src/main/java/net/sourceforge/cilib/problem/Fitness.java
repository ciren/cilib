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
package net.sourceforge.cilib.problem;

import java.io.Serializable;

import net.sourceforge.cilib.type.types.Type;

/**
 * This interface is an abstraction for the fitness of a solution to an optimisation problem.
 * The actual fitness value (as determined by the {@link OptimisationProblem} in question)
 * can be obtained by calling {@link #getValue()} while fitnesses can be compared using the
 * standard Java <code>Comparable</code> interface.
 * <p />
 * <b>Example:</b> <br />
 * <code>
 * Fitness a = ...; <br />
 * Fitness b = ...; <br />
 * <br />
 * int result = a.compareTo(b); <br />
 * if (result > 0) { <br />
 *   // a is a superior fitness to b <br />
 * } <br />
 * else if (result < 0) { <br />
 *   // b is a superior fitness to a <br />
 * } <br />
 * else { <br />
 *  // a and b are equally fit <br />
 * } <br />
 * </code> <br />
 *
 */
public interface Fitness extends Type, Comparable<Fitness>, Serializable {

    /**
     * {@inheritDoc}
     */
    @Override
    Fitness getClone();

    /**
     * Returns the underlying fitness value.
     * @return the actual fitness value.
     */
    Double getValue();

    /**
     * Creation method that maintains Fitness object immutability by returning
     * a new instance of the current class type.
     * @param value The desired value of the {@code Fitness} object.
     */
    Fitness newInstance(Double value);

}
