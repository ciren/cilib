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
package net.sourceforge.cilib.util.calculator;

import net.sourceforge.cilib.problem.Fitness;
import net.sourceforge.cilib.util.Cloneable;

/**
 * Perform the calculation of the fitness for the given <code>Entity</code>, decoupling the
 * <code>Entity</code> from the <code>Problem</code>.
 * @param <T> The type to which a fitness calculation is to be performed.
 */
public interface FitnessCalculator<T> extends Cloneable {

    /**
     * {@inheritDoc}
     */
    @Override
    FitnessCalculator<T> getClone();

    /**
     * Get the fitness, given the <code>position</code>.
     * @param entity The <code>Type</code> to base the calculation on.
     * @return A <code>Fitness</code> object representing the fitness of the <code>position</code>.
     */
    Fitness getFitness(T entity);

}
