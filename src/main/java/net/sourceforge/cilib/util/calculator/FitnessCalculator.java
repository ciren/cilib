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
    public FitnessCalculator<T> getClone();

    /**
     * Get the fitness, given the <code>position</code>.
     * @param entity The <code>Type</code> to base the calculation on.
     * @return A <code>Fitness</code> object representing the fitness of the <code>position</code>.
     */
    public Fitness getFitness(T entity);

}
