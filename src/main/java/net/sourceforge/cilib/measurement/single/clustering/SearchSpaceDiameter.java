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
package net.sourceforge.cilib.measurement.single.clustering;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.problem.ClusteringProblem;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.ClusteringUtils;
import net.sourceforge.cilib.util.Vectors;

/**
 * The search space diameter is equal to the distance between the two furthest possible points in the search space.
 *
 * @author Theuns Cloete
 */
public class SearchSpaceDiameter implements Measurement<Real> {
    private Real zMax = null;

    @Override
    public Measurement getClone() {
        return this;
    }

    @Override
    public String getDomain() {
        return "R";
    }

    @Override
    public Real getValue(Algorithm algorithm) {
        // we only have to calculate it once as long as the search space remains static/unchanged
        if (zMax == null) {
            ClusteringProblem problem = (ClusteringProblem) algorithm.getOptimisationProblem();
            Vector functionDomain = (Vector) problem.getDomain().getBuiltRepresenation();
            Vector domain = ClusteringUtils.disassembleCentroids(functionDomain, problem.getNumberOfClusters()).get(0);

            this.zMax = Real.valueOf(Vectors.zMax(problem.getDistanceMeasure(), domain));
        }
        return this.zMax;
    }
}
