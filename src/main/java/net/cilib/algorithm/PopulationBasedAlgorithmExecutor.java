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
import com.google.inject.Inject;
import java.util.List;
import net.cilib.annotation.Initialized;
import net.cilib.entity.Entity;
import net.cilib.collection.Topology;

/**
 *
 * @author gpampara
 */
public class PopulationBasedAlgorithmExecutor implements AlgorithmExecutor {

    private final PopulationBasedAlgorithm algorithm;
    private final Topology<Entity> topology;

    @Inject
    public PopulationBasedAlgorithmExecutor(PopulationBasedAlgorithm algorithm,
            @Initialized Topology<Entity> initialTopology) {
        this.algorithm = algorithm;
        this.topology = initialTopology;
    }

    @Override
    public void execute(List<Predicate<Algorithm>> conditions) {
        Predicate<Algorithm> stoppingConditions = Predicates.or(conditions);
        Topology<Entity> current = topology; // Current topology is inital topology
        
        while (!stoppingConditions.apply(algorithm)) {
            current = algorithm.iterate(current);
        }
    }
}
