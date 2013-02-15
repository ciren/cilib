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
import net.sourceforge.cilib.problem.solution.Fitness;
import net.sourceforge.cilib.problem.solution.MOFitness;

public class MOONeighbourhoodBestSentriesDetectionStrategy<E extends PopulationBasedAlgorithm> extends EnvironmentChangeDetectionStrategy<E> {
    private static final long serialVersionUID = 3598067152913033487L;

    public MOONeighbourhoodBestSentriesDetectionStrategy() {
        // super() is automatically called
    }

    public MOONeighbourhoodBestSentriesDetectionStrategy(MOONeighbourhoodBestSentriesDetectionStrategy<E> rhs) {
        super(rhs);
    }

    @Override
    public MOONeighbourhoodBestSentriesDetectionStrategy<E> getClone() {
        return new MOONeighbourhoodBestSentriesDetectionStrategy<E>(this);
    }

    @Override
    public boolean detect(PopulationBasedAlgorithm algorithm) {
        if (algorithm.getIterations() % interval == 0) {
            Set<? extends Entity> sentries = Topologies.getNeighbourhoodBestEntities(algorithm.getTopology());

            boolean detectedChange = false;
            for (Entity sentry : sentries) {
                if (sentry.getFitness().getClass().getName().matches("MinimisationFitness")) {
                    Fitness previousFitness = sentry.getFitness();
                    sentry.calculateFitness();
                    Fitness currentFitness = sentry.getFitness();

                    if (Math.abs(previousFitness.getValue() - currentFitness.getValue()) >= epsilon) {
                        detectedChange = true;

                    }
                }
                else if (sentry.getFitness().getClass().getName().matches("StandardMOFitness")) {
                        MOFitness previousFitness = (MOFitness)sentry.getFitness();
                        sentry.calculateFitness();
                        MOFitness currentFitness = (MOFitness)sentry.getFitness();

                        for (int k=0; k < previousFitness.getDimension(); k++)
                            if (Math.abs(previousFitness.getFitness(k).getValue() -
                		currentFitness.getFitness(k).getValue()) >= epsilon) {
                		detectedChange = true;
                                break;
                            }
                }
                if (detectedChange) {
                    System.out.println("Detected a change");
                    return true;
                }
        }
            }

        return false;
    }
}
