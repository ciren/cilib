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
 * Calculate the total compactness; sometimes referred to as the <em>intra-cluster distance</em>. In other words, the
 * sum of the distances between all patterns of all clusters and their associated centroids. The calculation is
 * specified by equation 13 in Section IV on page 124 of <em>Nonparametric Genetic Clustering: Comparison of Validity
 * Indices</em> by <b>Ujjwal Maulik and Sanghamitra Bandyopadhyay</b> in IEEE Transactions on Systems, Man, and
 * Cybernetics, Part C: Applications and Reviews, Volume 31, Number 1, pages 120-125, February 2001.
 *
 * @author Theuns Cloete
 */
public class TotalCompactnessFunction implements ClusteringFunction<Double> {
    private static final long serialVersionUID = -8511228982780183714L;

    /**
     * Calculate the total compactness (<i>intra-cluster distance</i>) of the given clusters.
     *
     * TODO: When we start using Guice, then only the required parameters have to be injected when the class is
     * instantiated and this method will not need all these parameters.
     *
     * @param clusters the clusters containing their associated patterns and centroids
     * @param dataTable the {@link DataTable data set} containing all the {@link StandardPattern patterns}
     * @param distanceMeasure the {@link DistanceMeasure distance measure} that should be used
     * @param clusterCenterStrategy the {@link ClusterCenterStrategy cluster center strategy} that should be used
     * @param dataSetMean the {@link Vector mean vector} of the data set that should be used if necessary
     * @param dataSetVariance the {@link Vector variance vector} of the data set that should be used if necessary
     * @param zMax the maximum value in the domain that should be used if necessary
     * @return the total compactness of the given clusters
     */
    @Override
    public Double apply(List<Cluster> clusters, DataTable<StandardPattern, TypeList> dataTable, DistanceMeasure distanceMeasure, ClusterCenterStrategy clusterCenterStrategy, Vector dataSetMean, double dataSetVariance, double zMax) {
        double compactness = 0.0;

        for (Cluster cluster : clusters) {
            Vector center = clusterCenterStrategy.getCenter(cluster);

            for (StandardPattern pattern : cluster) {
                compactness += distanceMeasure.distance(pattern.getVector(), center);
            }
        }
        return compactness;
    }
}
