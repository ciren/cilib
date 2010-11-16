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

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.inject.Inject;
import com.google.inject.Key;
import java.util.List;
import net.cilib.entity.Entity;
import net.cilib.collection.Topology;
import net.cilib.inject.SimulationScope;

/**
 *
 * @author gpampara
 */
public class PopulationBasedAlgorithmExecutor {

    private final SimulationScope scope;

    @Inject
    public PopulationBasedAlgorithmExecutor(SimulationScope scope) {
        this.scope = scope;
    }

    /**
     * Perform iterations of the provided {@code PopulationBasedAlgorithm},
     * using the given {@code Topology} whilst adhering to the given
     * stopping conditions.
     */
    public void execute(final PopulationBasedAlgorithm algorithm,
            final Topology<? extends Entity> initial,
            final List<Predicate<Algorithm>> stoppingConditions) {
        Preconditions.checkNotNull(algorithm, "Reference to algorithm is null.");
        Preconditions.checkNotNull(initial);
        Preconditions.checkNotNull(stoppingConditions);
        Preconditions.checkArgument(stoppingConditions.size() >= 1, "At least 1 stopping condition is requried.");

        scope.enter();
        scope.seed(Key.get(Topology.class), initial);

        try {
            Predicate<Algorithm> aggregate = Predicates.or(stoppingConditions);
            Topology<Entity> current = (Topology<Entity>) initial; //currentTopology.get(); // Current topology is inital topology

            while (aggregate.apply(algorithm)) {
                current = algorithm.iterate(current);

                // Reset the current scope's managed instance. It needs to be updated.
                scope.seed(Key.get(Topology.class), current);
            }
        } finally {
            scope.exit();
        }
    }
}
