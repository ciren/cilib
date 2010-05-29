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
import net.sourceforge.cilib.problem.clustering.PartitionalClusteringProblem;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * Combines and measures the centroid vectors of the clusters optimised by the given algorithm.
 *
 * @author Theuns Cloete
 */
public class ClusterCentroids implements Measurement {
    @Override
    public Measurement getClone() {
        return this;
    }

    @Override
    public String getDomain() {
        return "(R^?)^?";
    }

    /**
     * The {@link PartitionalClusteringProblem} only permits the correct number of clusters and therefore we do not have
     * to cluster the data set in order to get the centroids. The {@link Algorithm algorithm&apos;s} solution is the
     * centroids.
     * @param algorithm the {@link Algorithm} used to cluster
     * @return the centroids
     */
    @Override
    public Type getValue(Algorithm algorithm) {
        return (Vector) algorithm.getBestSolution().getPosition();
    }
}
