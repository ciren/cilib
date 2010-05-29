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

import java.util.ArrayList;
import java.util.Set;

import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.Pattern;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;

/**
 * Calculate the total compactness; sometimes referred to as the <i>intra-cluster distance</i>. In other words, the sum
 * of the distances between all patterns of all clusters and their associated centroids. The calculation is specified by
 * Equation 13 in Section IV on page 124 of:<br/>
 *
 * @Article{ 923275, title = "Nonparametric Genetic Clustering: Comparison of Validity
 *           Indices", author = "Ujjwal Maulik and Sanghamitra Bandyopadhyay", journal =
 *           "IEEE Transactions on Systems, Man, and Cybernetics, Part C: Applications
 *           and Reviews", pages = "120--125", volume = "31", number = "1", month = feb,
 *           year = "2001", issn = "1094-6977" }
 * @author Theuns Cloete
 */
public class TotalCompactnessFunction extends ClusteringErrorFunction {
    private static final long serialVersionUID = -8511228982780183714L;

    /**
     * Calculate the total compactness (<i>intra-cluster distance</i>) of the given clusters.
     * @return the total compactness of the given clusters
     */
    @Override
    public Double apply(ArrayList<Cluster<Vector>> clusters, Set<Pattern<Vector>> patterns, DistanceMeasure distanceMeasure, Vector dataSetMean, double dataSetVariance, double zMax) {
        double compactness = 0.0;

        for (Cluster<Vector> cluster : clusters) {
            Vector center = this.clusterCenterStrategy.getCenter(cluster);

            for (Pattern<Vector> pattern : cluster) {
                compactness += distanceMeasure.distance(pattern.getData(), center);
            }
        }
        return compactness;
    }
}
