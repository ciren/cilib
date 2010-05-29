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

import net.sourceforge.cilib.functions.clustering.ClusteringFunctions;
import net.sourceforge.cilib.functions.clustering.clustercenterstrategies.ClusterMeanStrategy;
import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.Pattern;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;

/**
 * This is the Dunn Index 33, due to Equations 22 and 28 in:<br/>
 * @Article{ 678624, title = "Some New Indexes of Cluster Validity", author = "James C. Bezdek and
 *           Nikhil R. Pal", journal = "IEEE Transactions on Systems, Man, and Cybernetics, Part B:
 *           Cybernetics", pages = "301--315", volume = "28", number = "3", month = jun, year =
 *           "1998", issn = "1083-4419" }
 * NOTE: By default, the cluster center refers to the cluster mean. See {@link ClusterMeanStrategy}.
 * @author Theuns Cloete
 */
public class DunnIndex33 extends DunnIndex {
    private static final long serialVersionUID = -3307601269742583865L;

    public DunnIndex33() {
    }

    /**
     * This method implements Equation 28 in the above-mentioned article.
     */
    @Override
    protected double calculateClusterScatter(DistanceMeasure distanceMeasure, Cluster<Vector> cluster) {
        double distanceSum = 0.0;
        Vector center = this.clusterCenterStrategy.getCenter(cluster);

        for (Pattern<Vector> pattern : cluster) {
            distanceSum += distanceMeasure.distance(pattern.getData(), center);
        }
        return 2.0 * (distanceSum / cluster.size());
    }

    /**
     * This method implements Equation 22 in the above-mentioned article.
     */
    @Override
    protected double calculateClusterSeperation(DistanceMeasure distanceMeasure, Cluster<Vector> lhs, Cluster<Vector> rhs) {
        return ClusteringFunctions.averageClusterDistance(distanceMeasure, lhs, rhs);
    }
}
