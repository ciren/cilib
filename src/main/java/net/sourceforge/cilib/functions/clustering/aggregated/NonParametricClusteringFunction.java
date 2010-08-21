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
package net.sourceforge.cilib.functions.clustering.aggregated;

import java.util.ArrayList;

import net.sourceforge.cilib.functions.clustering.ClusteringErrorFunction;
import net.sourceforge.cilib.functions.clustering.ClusteringFunction;
import net.sourceforge.cilib.functions.clustering.MaximumAverageDistanceFunction;
import net.sourceforge.cilib.functions.clustering.MinimumSeparationFunction;
import net.sourceforge.cilib.functions.clustering.QuantisationErrorFunction;
import net.sourceforge.cilib.functions.clustering.clustercenterstrategies.ClusterCenterStrategy;
import net.sourceforge.cilib.functions.clustering.clustercenterstrategies.ClusterCentroidStrategy;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;

/**
 * Calculates a non-parameterised error of a particular clustering. See:<br/>
 * @PhDThesis{ omran2004thesis, title = "Particle Swarm Optimization Methods for Pattern Recognition
 *             and Image Processing", author = "Mahamed G.H. Omran", institution = "University Of
 *             Pretoria", school = "Computer Science", year = "2004", month = nov, pages = "128 &
 *             129" address = "Pretoria, South Africa", note = "Supervisor: A. P. Engelbrecht" }
 * NOTE: By default, the cluster center refers to the cluster centroid. See {@link ClusterCentroidStrategy}.
 * @author Theuns Cloete
 */
public class NonParametricClusteringFunction extends ClusteringErrorFunction {
    private static final long serialVersionUID = 5712216719378084294L;

    private final ClusteringFunction maximumAverageDistance;
    private final ClusteringFunction quantisationError;
    private final ClusteringFunction minimumSeparation;

    public NonParametricClusteringFunction() {
        this.maximumAverageDistance = new MaximumAverageDistanceFunction();
        this.quantisationError = new QuantisationErrorFunction();
        this.minimumSeparation = new MinimumSeparationFunction();
    }

    @Override
    public Double apply(ArrayList<Cluster> clusters, DataTable<StandardPattern, TypeList> dataTable, DistanceMeasure distanceMeasure, Vector dataSetMean, double dataSetVariance, double zMax) {
        return (this.maximumAverageDistance.apply(clusters, dataTable, distanceMeasure, dataSetMean, dataSetVariance, zMax) + this.quantisationError.apply(clusters, dataTable, distanceMeasure, dataSetMean, dataSetVariance, zMax)) / this.minimumSeparation.apply(clusters, dataTable, distanceMeasure, dataSetMean, dataSetVariance, zMax);
    }

    @Override
    public void setClusterCenterStrategy(ClusterCenterStrategy clusterCenterStrategy) {
        this.clusterCenterStrategy = clusterCenterStrategy;
        this.maximumAverageDistance.setClusterCenterStrategy(this.clusterCenterStrategy);
        this.quantisationError.setClusterCenterStrategy(this.clusterCenterStrategy);
        this.minimumSeparation.setClusterCenterStrategy(this.clusterCenterStrategy);
    }
}
