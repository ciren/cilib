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
package net.sourceforge.cilib.measurement.single.moo;

import com.google.common.collect.Lists;
import java.util.List;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.moo.archive.Archive;
import net.sourceforge.cilib.problem.MOFitness;
import net.sourceforge.cilib.problem.OptimisationSolution;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.problem.Fitnesses;

/**
 * <p>
 * Measures the distribution (in objective space) of the non-dominated solutions
 * in an archive.
 * </p>
 *
 * @author Wiehann Matthysen
 */
public class SolutionDistribution implements Measurement {

    private static final long serialVersionUID = -3434812635896475717L;

    public SolutionDistribution() {
    }

    public SolutionDistribution(SolutionDistribution copy) {
    }

    @Override
    public SolutionDistribution getClone() {
        return new SolutionDistribution(this);
    }

    @Override
    public String getDomain() {
        return "R";
    }

    @Override
    public Real getValue(Algorithm algorithm) {

        List<Double> distances = Lists.newArrayList();

        // Fist, calculate nearest distance between solution i and another solution in Pareto front.
        // Add every nearest distance in distances list.
        Archive archive = Archive.Provider.get();
        for (OptimisationSolution solution : archive) {
            Vector solutionFitness = Fitnesses.vectorOf((MOFitness) solution.getFitness());
            double nearestDistance = Double.MAX_VALUE;
            for (OptimisationSolution otherSolution : archive) {
                if (solution != otherSolution) {
                    Vector otherFitness = Fitnesses.vectorOf((MOFitness) otherSolution.getFitness());
                    double distance = otherFitness.subtract(solutionFitness).norm();
                    nearestDistance = Math.min(nearestDistance, distance);
                }
            }
            distances.add(nearestDistance);
        }

        // Then, calculate average nearest distance.
        double averageNearest = 0.0;
        for (Double distance : distances) {
            averageNearest += distance;
        }
        averageNearest /= archive.size();

        // Now calculate the standard deviation in nearest distances.
        double sumSqr = 0.0;
        for (Double distance : distances) {
            sumSqr += (distance - averageNearest) * (distance - averageNearest);
        }

        return Real.valueOf((1.0 / archive.size()) * Math.sqrt(sumSqr / archive.size()));
    }
}
