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
package net.sourceforge.cilib.entity.comparator;

import java.io.Serializable;
import java.util.Comparator;

import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.problem.solution.Fitness;

/**
 * Comparator to order {@linkplain Entity} instances based on fitness values.
 * This comparator results in a ascending order. This ordering is effectively
 * an ordering of entity instances from the "least fit" to the "most fit".
 *
 * @param <E> The {@code Entity} type.
 */
public class AscendingFitnessComparator<E extends Entity> implements Comparator<E>, Serializable {
    private static final long serialVersionUID = -4303050310093446507L;

    /**
     * Compare the {@linkplain Entity} objects returning the desired ordering.
     * @param e1 The first {@linkplain Entity} to be used in the comparison.
     * @param e2 The second {@linkplain Entity} to be used in the comparison.
     * @return -1 if e1 is less than e2;
     *         0 if e1 and e2 are equal
     *         1 if e2 is greater than e1
     */
    @Override
    public int compare(E e1, E e2) {
        Fitness f1 = e1.getFitness();
        Fitness f2 = e2.getFitness();

        return f1.compareTo(f2);
    }

}
