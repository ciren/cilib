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

import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.clustering.clustercenterstrategies.ClusterCenterStrategy;
import net.sourceforge.cilib.problem.ClusteringProblem;
import net.sourceforge.cilib.problem.dataset.DataSetBuilder;
import net.sourceforge.cilib.problem.dataset.StaticDataSetBuilder;
import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * @author Theuns Cloete
 */
public class TotalErrorFunction extends ContinuousFunction implements ClusteringFunction {
    private static final long serialVersionUID = 3925553803564320678L;

    private final ClusteringFunction compactnessFunction;
    private final ClusteringFunction separationFunction;

    public TotalErrorFunction() {
        this.compactnessFunction = new TotalCompactnessFunction();
        this.separationFunction = new TotalSeparationFunction();
    }

    @Override
    public TotalErrorFunction getClone() {
        return this;
    }

    /**
     * It would be awesome if the ClusteringProblem and StaticDataSetBuilder could be injected.
     */
    @Override
    public Double apply(Vector input) {
        ClusteringProblem clusteringProblem = ClusteringFunctions.getClusteringProblem();
        DataSetBuilder dataSetBuilder = clusteringProblem.getDataSetBuilder();
        ArrayList<Cluster<Vector>> significantClusters = ClusteringFunctions.arrangeClustersAndCentroids(input, clusteringProblem, (StaticDataSetBuilder) dataSetBuilder);
        int clustersFormed = significantClusters.size();

        return this.apply(clusteringProblem, significantClusters, clustersFormed);
    }

    @Override
    public Double apply(ClusteringProblem clusteringProblem, ArrayList<Cluster<Vector>> significantClusters, int clustersFormed) {
        return this.compactnessFunction.apply(clusteringProblem, significantClusters, clustersFormed) - this.separationFunction.apply(clusteringProblem, significantClusters, clustersFormed);
    }

    @Override
    public void setClusterCenterStrategy(ClusterCenterStrategy clusterCenterStrategy) {
        this.compactnessFunction.setClusterCenterStrategy(clusterCenterStrategy);
        this.separationFunction.setClusterCenterStrategy(clusterCenterStrategy);
    }

    @Override
    public ClusterCenterStrategy getClusterCenterStrategy() {
        return this.compactnessFunction.getClusterCenterStrategy();
    }

/*
    protected double calculateAverageSeparation() {
        double numberOfDistanceCalculations = (clustersFormed * (clustersFormed - 1)) / 2;

        return this.calculateTotalSeparation() / numberOfDistanceCalculations;
    }

    // NOTE: Same as {@link ClusteringFitnessFunction#calculateAverageIntraClusterDistance()}
    protected double calculateAverageCompactness() {
        return this.calculateTotalCompactness() / ((StaticDataSetBuilder) this.problem.getDataSetBuilder()).getNumberOfPatterns();
    }
*/
}
