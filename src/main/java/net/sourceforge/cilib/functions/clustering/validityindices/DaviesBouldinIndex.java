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

import java.util.ArrayList;

import net.sourceforge.cilib.functions.clustering.ClusteringErrorFunction;
import net.sourceforge.cilib.functions.clustering.clustercenterstrategies.ClusterCentroidStrategy;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
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
 * NOTE: By default, the cluster center refers to the cluster centroid. See {@link ClusterCentroidStrategy}.
 * @author Theuns Cloete
 */
public class DaviesBouldinIndex extends ClusteringErrorFunction {
    private static final long serialVersionUID = -5167494843653998358L;

    public DaviesBouldinIndex() {
    }

    @Override
    public Double apply(ArrayList<Cluster> clusters, DataTable<StandardPattern, TypeList> dataTable, DistanceMeasure distanceMeasure, Vector dataSetMean, double dataSetVariance, double zMax) {
        double db = 0.0, max = -Double.MAX_VALUE;

        for (Cluster lhs : clusters) {
            double withinScatterLeft = this.calculateWithinClusterScatter(distanceMeasure, lhs);

            for (Cluster rhs : clusters) {
                if (lhs != rhs) {
                    double withinScatterRight = this.calculateWithinClusterScatter(distanceMeasure, rhs);
                    double betweenSeperation = this.calculateBetweenClusterSeperation(distanceMeasure, lhs, rhs);

                    max = Math.max(max, (withinScatterLeft + withinScatterRight) / betweenSeperation);
                }
            }
            db += max;
            max = -Double.MAX_VALUE;
        }
        return db / clusters.size();
    }

    private double calculateWithinClusterScatter(DistanceMeasure distanceMeasure, Cluster cluster) {
        double withinClusterScatter = 0.0;
        Vector center = this.clusterCenterStrategy.getCenter(cluster);

        for (StandardPattern pattern : cluster) {
            withinClusterScatter += distanceMeasure.distance(pattern.getVector(), center);
        }
        return withinClusterScatter / cluster.size();
    }

    /**
     * The <i>alpha</i> value of the distance measure should correspond to the <i>p</i> value of the
     * Davies-Bouldin Validity Index.
     */
    private double calculateBetweenClusterSeperation(DistanceMeasure distanceMeasure, Cluster lhs, Cluster rhs) {
        return distanceMeasure.distance(this.clusterCenterStrategy.getCenter(lhs), this.clusterCenterStrategy.getCenter(rhs));
    }
}
