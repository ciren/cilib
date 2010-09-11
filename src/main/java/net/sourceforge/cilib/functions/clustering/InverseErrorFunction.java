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
 * @author Theuns Cloete
 */
public class InverseErrorFunction implements ClusteringFunction<Double> {
    private static final long serialVersionUID = 2680037315045146954L;

    /**
     * 
     * TODO: When we start using Guice, then only the required parameters have to be injected into this class and this
     * method will not need all these parameters.
     *
     * @param clusters the clusters containing their associated patterns and centroids
     * @param dataTable the data set containing all the patterns
     * @param distanceMeasure the distance measure that should be used
     * @param dataSetMean the mean vector of the data set that should be used if necessary
     * @param dataSetVariance the variance of the data set that should be used if necessary
     * @param zMax the maximum value in the domain that should be used if necessary
     * @return
     */
    @Override
    public Double apply(List<Cluster> clusters, DataTable<StandardPattern, TypeList> dataTable, DistanceMeasure distanceMeasure, ClusterCenterStrategy clusterCenterStrategy, Vector dataSetMean, double dataSetVariance, double zMax) {
        double inverseError = 0.0;

        for (Cluster cluster : clusters) {
            Vector center = clusterCenterStrategy.getCenter(cluster);

            for (StandardPattern pattern : cluster) {
                inverseError += 1.0 / (distanceMeasure.distance(pattern.getVector(), center) + 1.0);
            }
        }
        return inverseError;
    }
}
