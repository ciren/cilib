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
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import java.util.List;
import net.cilib.entity.Entity;
import net.cilib.collection.Topology;
import net.cilib.measurement.Measurement;

/**
 *
 * @author gpampara
 */
public class AlgorithmExecutor {

    private final List<Predicate<Measurement>> stoppingConditions;

    public AlgorithmExecutor(List<Predicate<Measurement>> stoppingConditions) {
        this.stoppingConditions = ImmutableList.copyOf(stoppingConditions);
    }

    public void execute(PopulationBasedAlgorithm algorithm, Topology<Entity> topology) {
        Predicate<Measurement> combined = Predicates.or(stoppingConditions);

        for (Predicate<Measurement> measurement : stoppingConditions) {
        }

        // First, evaluate the fitnesses of all the Entity instances.
        // for (Entity e : topology) evaluateTheFitness()

//        while (stoppingConditions.apply(Boolean.TRUE)) {
        Topology<Entity> nextTopology = algorithm.iterate(topology);
//        }
    }
}
