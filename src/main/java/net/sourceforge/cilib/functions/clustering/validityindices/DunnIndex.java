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
import java.util.Set;

import net.sourceforge.cilib.functions.clustering.ClusteringErrorFunction;
import net.sourceforge.cilib.functions.clustering.ClusteringFunctions;
import net.sourceforge.cilib.functions.clustering.clustercenterstrategies.ClusterMeanStrategy;
import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.Pattern;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;

/**
 * This is the Dunn Validity Index as described in:<br/>
 * @Article{ dunn1974vi, title = "Well Separated Clusters and Optimal Fuzzy Partitions", author =
 *           "J. C. Dunn", journal = "Journal of Cybernetics", pages = "95--104", volume = "4", year =
 *           "1974" }
 * @author Theuns Cloete
 */
public class DunnIndex extends ClusteringErrorFunction {
    private static final long serialVersionUID = -7440453719679272149L;

    public DunnIndex() {
        this.clusterCenterStrategy = new ClusterMeanStrategy();
    }

    /**
     * Sub-classes should override this method if the cluster scatter should be calculated differently.
     */
    protected double calculateClusterScatter(DistanceMeasure distanceMeasure, Cluster<Vector> cluster) {
        return ClusteringFunctions.clusterDiameter(distanceMeasure, cluster);
    }

    /**
     * Sub-classes should override this method if the cluster separation should be calculated differently.
     */
    protected double calculateClusterSeperation(DistanceMeasure distanceMeasure, Cluster<Vector> lhs, Cluster<Vector> rhs) {
        return ClusteringFunctions.minimumClusterDistance(distanceMeasure, lhs, rhs);
    }

    @Override
    public Double apply(ArrayList<Cluster<Vector>> clusters, Set<Pattern<Vector>> patterns, DistanceMeasure distanceMeasure, Vector dataSetMean, double dataSetVariance, double zMax) {
        double withinScatter = -Double.MAX_VALUE, betweenSeperation = Double.MAX_VALUE;
        int clustersFormed = clusters.size();

        for (Cluster<Vector> cluster : clusters) {
            withinScatter = Math.max(withinScatter, this.calculateClusterScatter(distanceMeasure, cluster));
        }

        for (int i = 0; i < clustersFormed - 1; ++i) {
            for (int j = i + 1; j < clustersFormed; ++j) {
                betweenSeperation = Math.min(betweenSeperation, this.calculateClusterSeperation(distanceMeasure, clusters.get(i), clusters.get(j)));
            }
        }

        return betweenSeperation / withinScatter;
    }
}
