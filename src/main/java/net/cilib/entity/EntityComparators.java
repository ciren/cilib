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
package net.cilib.entity;

import java.util.Comparator;

/**
 *
 * @author gpampara
 */
public final class EntityComparators {

    /**
     * Comparator based on current {@link Fitness} instances. Any instances
     * that implement {@link HasFitness}, may be compared.
     */
    public static final Comparator<HasFitness> FITNESS_COMPARATOR = new Comparator<HasFitness>() {
        @Override
        public int compare(HasFitness o1, HasFitness o2) {
            return o1.fitness().compareTo(o2.fitness());
        }
    };

    private EntityComparators() {}
}
