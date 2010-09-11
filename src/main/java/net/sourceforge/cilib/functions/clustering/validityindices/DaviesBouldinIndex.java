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
package net.sourceforge.cilib.functions.clustering.validityindices;

import java.util.List;

import net.sourceforge.cilib.functions.clustering.ClusteringFunction;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.problem.clustering.clustercenterstrategies.ClusterCenterStrategy;
import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;

/**
 * This is the Davies-Bouldin Validity Index as described in:<br/>
 *
 * @Article{ daviesbouldin1979vi, title = "A Cluster Seperation Measure", author = "David L. Davies
 *           and Donald W. Bouldin", journal = "IEEE Transactions on Pattern Analysis and Machine
 *           Intelligence", volume = "1", number = "2", year = "1979", pages = "224--227", month =
 *           apr, issn = "0162-8828" }
 * @author Theuns Cloete
 */
public class DaviesBouldinIndex implements ClusteringFunction<Double> {
    private static final long serialVersionUID = -5167494843653998358L;

    public DaviesBouldinIndex() {
    }

    @Override
    public Double apply(List<Cluster> clusters, DataTable<StandardPattern, TypeList> dataTable, DistanceMeasure distanceMeasure, ClusterCenterStrategy clusterCenterStrategy, Vector dataSetMean, double dataSetVariance, double zMax) {
        double db = 0.0, max = -Double.MAX_VALUE;

        for (Cluster lhs : clusters) {
            double withinScatterLeft = this.calculateWithinClusterScatter(distanceMeasure, clusterCenterStrategy, lhs);

            for (Cluster rhs : clusters) {
                if (lhs != rhs) {
                    double withinScatterRight = this.calculateWithinClusterScatter(distanceMeasure, clusterCenterStrategy, rhs);
                    double betweenSeperation = this.calculateBetweenClusterSeperation(distanceMeasure, clusterCenterStrategy, lhs, rhs);

                    max = Math.max(max, (withinScatterLeft + withinScatterRight) / betweenSeperation);
                }
            }
            db += max;
            max = -Double.MAX_VALUE;
        }
        return db / clusters.size();
    }

    private double calculateWithinClusterScatter(DistanceMeasure distanceMeasure, ClusterCenterStrategy clusterCenterStrategy, Cluster cluster) {
        double withinClusterScatter = 0.0;
        Vector center = clusterCenterStrategy.getCenter(cluster);

        for (StandardPattern pattern : cluster) {
            withinClusterScatter += distanceMeasure.distance(pattern.getVector(), center);
        }
        return withinClusterScatter / cluster.size();
    }

    /**
     * The <i>alpha</i> value of the distance measure should correspond to the <i>p</i> value of the
     * Davies-Bouldin Validity Index.
     */
    private double calculateBetweenClusterSeperation(DistanceMeasure distanceMeasure, ClusterCenterStrategy clusterCenterStrategy, Cluster lhs, Cluster rhs) {
        return distanceMeasure.distance(clusterCenterStrategy.getCenter(lhs), clusterCenterStrategy.getCenter(rhs));
    }
}
