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

import net.sourceforge.cilib.functions.clustering.clustercenterstrategies.ClusterCenterStrategy;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;

/**
 * Calculate the average compactness; sometimes referred to as the average <i>intra-cluster distance</i>. In other words,
 * the average of the distances between all patterns of all clusters and their associated centroids. The calculation is
 * specified in Section 3.2 on page 2 of:<br/>
 *
 * @Unpublished{ cal99, title = "Determination of Number of Clusters in K-Means
 *               Clustering and Application in Colour Image Segmentation", author =
 *               "Siddheswar Ray and Rose H. Turi", year = "2000", month = jul }
 * @author Theuns Cloete
 */
public class AverageCompactnessFunction extends ClusteringErrorFunction {
    private static final long serialVersionUID = -4185205766188040942L;

    private final ClusteringFunction totalCompactnessFunction;

    public AverageCompactnessFunction() {
        this.totalCompactnessFunction = new TotalCompactnessFunction();
    }

    /**
     * Calculate the average compactness (<i>intra-cluster distance</i>) of the given clusters.
     * @return the average compactness of the given clusters
     */
    @Override
    public Double apply(ArrayList<Cluster> clusters, DataTable<StandardPattern, TypeList> dataTable, DistanceMeasure distanceMeasure, Vector dataSetMean, double dataSetVariance, double zMax) {
        return this.totalCompactnessFunction.apply(clusters, dataTable, distanceMeasure, dataSetMean, dataSetVariance, zMax) / dataTable.getNumRows();
    }

    @Override
    public void setClusterCenterStrategy(ClusterCenterStrategy clusterCenterStrategy) {
        this.clusterCenterStrategy = clusterCenterStrategy;
        this.totalCompactnessFunction.setClusterCenterStrategy(clusterCenterStrategy);
    }
}
