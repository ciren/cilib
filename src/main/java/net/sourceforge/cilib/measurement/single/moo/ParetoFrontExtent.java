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
 * Measures the distance (in objective space) between the two non-dominated
 * solutions located furthest from one another in an archive.
 * </p>
 *
 * @author Wiehann Matthysen
 */
public class ParetoFrontExtent implements Measurement {

    private static final long serialVersionUID = 1264405714078826569L;

    public ParetoFrontExtent() {
    }

    public ParetoFrontExtent(ParetoFrontExtent copy) {
    }

    @Override
    public ParetoFrontExtent getClone() {
        return new ParetoFrontExtent(this);
    }

    @Override
    public String getDomain() {
        return "R";
    }

    @Override
    public Real getValue(Algorithm algorithm) {
        Archive archive = Archive.Provider.get();

        double maximumDistance = 0.0;

        for (OptimisationSolution solution : archive) {
            Vector solutionFitness = Fitnesses.vectorOf((MOFitness) solution.getFitness());
            for (OptimisationSolution otherSolution : archive) {
                Vector otherFitness = Fitnesses.vectorOf((MOFitness) otherSolution.getFitness());
                double distance = otherFitness.subtract(solutionFitness).norm();
                maximumDistance = Math.max(maximumDistance, distance);
            }
        }

        return Real.valueOf(maximumDistance);
    }
}
