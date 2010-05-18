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
package net.sourceforge.cilib.pso.dynamic.detectionstrategies;


import java.util.Set;

import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topologies;

public class NeighbourhoodBestSentriesDetectionStrategy<E extends PopulationBasedAlgorithm> extends EnvironmentChangeDetectionStrategy<E> {
    private static final long serialVersionUID = 3598067152913033487L;

    public NeighbourhoodBestSentriesDetectionStrategy() {
        // super() is automatically called
    }

    public NeighbourhoodBestSentriesDetectionStrategy(NeighbourhoodBestSentriesDetectionStrategy<E> rhs) {
        super(rhs);
    }

    @Override
    public NeighbourhoodBestSentriesDetectionStrategy<E> getClone() {
        return new NeighbourhoodBestSentriesDetectionStrategy<E>(this);
    }

    @Override
    public boolean detect(PopulationBasedAlgorithm algorithm) {
        if (algorithm.getIterations() % interval == 0) {
            Set<? extends Entity> sentries = Topologies.getNeighbourhoodBestEntities(algorithm.getTopology());

            for (Entity sentry : sentries) {
                double previousFitness = sentry.getFitness().getValue();
                sentry.calculateFitness();
                double currentFitness = sentry.getFitness().getValue();

                if(Math.abs(previousFitness - currentFitness) >=  epsilon) {
                    return true;
                }
            }
        }
        return false;
    }
}
