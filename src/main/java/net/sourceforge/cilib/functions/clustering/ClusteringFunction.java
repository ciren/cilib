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

import net.sourceforge.cilib.functions.Function;
import net.sourceforge.cilib.functions.clustering.clustercenterstrategies.ClusterCenterStrategy;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;

/**
 * @author Theuns Cloete
 */
public interface ClusteringFunction extends Function<ArrayList<Cluster>, Double> {

    /**
     * Calculate the error or fitness of the clusters using the provided parameters.
     *
     * TODO: When we start using Guice, then only the required parameters have to be injected when the class is
     * instantiated and this method will not need all these parameters.
     *
     * @param clusters the clusters containing their associated patterns and centroids
     * @param dataTable the data set containing all the patterns
     * @param distanceMeasure the distance measure that should be used
     * @param dataSetMean the mean vector of the data set that should be used if necessary
     * @param dataSetVariance the variance of the data set that should be used if necessary
     * @param zMax the maximum value in the domain that should be used if necessary
     * @return the error or fitness of the clustering as a double value
     */
    Double apply(ArrayList<Cluster> clusters, DataTable<StandardPattern, TypeList> dataTable, DistanceMeasure distanceMeasure, Vector dataSetMean, double dataSetVariance, double zMax);

    /**
     * Specify the {@link ClusterCenterStrategy} that should be used to represent the center of the clusters.
     * @param clusterCenterStrategy the strategy that should be used to determine the center of the clusters.
     */
    void setClusterCenterStrategy(ClusterCenterStrategy clusterCenterStrategy);

    /**
     * Retrieve the {@link ClusterCenterStrategy} that is used to represent the center of the clusters.
     * @return the configured {@link ClusterCenterStrategy} used to determine the center of the clusters.
     */
    ClusterCenterStrategy getClusterCenterStrategy();
}
