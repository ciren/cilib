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
package net.cilib.algorithm;

import com.google.common.base.Predicate;
import java.util.List;

/**
 * {@code Simulation} provides an abstraction for any type of simulation. For
 * example, a {@code PopulationBasedAlgorithm} running on a given problem with
 * a set of defined measurements.
 * @author gpampara
 */
public interface Simulation {

    void execute(List<Predicate<Algorithm>> stoppingConditions);

    static interface Builder {
        Simulation build();
    }
}
