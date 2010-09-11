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
package net.sourceforge.cilib.functions.clustering;

import java.util.List;

import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.problem.clustering.clustercenterstrategies.ClusterCenterStrategy;
import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;

/**
 * Calculate the maximum of the average distances between the patterns in the dataset and the centers of their
 * associated clusters. See Section 4.1.1 at the bottom of page 105 of:<br/>
 *
 * @PhDThesis{ omran2004thesis, title = "Particle Swarm Optimization Methods for Pattern
 *             Recognition and Image Processing", author = "Mahamed G.H. Omran",
 *             institution = "University Of Pretoria", school = "Computer Science", year =
 *             "2004", month = nov, address = "Pretoria, South Africa", note =
 *             "Supervisor: A. P. Engelbrecht", }
 * @author Theuns Cloete
 */
public class MaximumAverageDistanceFunction implements ClusteringFunction<Double> {
    private static final long serialVersionUID = -594602171669603714L;

    /**
     * Calculate the maximum of the average distances between the patterns in the dataset and the centers of their
     * associated clusters.
     * @return the maximum of the average distances between the patterns of a cluster and their associated center.
     */
    @Override
    public Double apply(List<Cluster> clusters, DataTable<StandardPattern, TypeList> dataTable, DistanceMeasure distanceMeasure, ClusterCenterStrategy clusterCenterStrategy, Vector dataSetMean, double dataSetVariance, double zMax) {
        double maximumAverageDistance = 0.0;

        for (Cluster cluster : clusters) {
            double averageDistance = 0.0;
            Vector center = clusterCenterStrategy.getCenter(cluster);

            for (StandardPattern pattern : cluster) {
                averageDistance += distanceMeasure.distance(pattern.getVector(), center);
            }
            averageDistance /= cluster.size();
            maximumAverageDistance = Math.max(maximumAverageDistance, averageDistance);
        }
        return maximumAverageDistance;
    }
}
