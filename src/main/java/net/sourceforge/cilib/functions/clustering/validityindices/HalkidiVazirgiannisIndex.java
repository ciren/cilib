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

import java.util.List;

import net.sourceforge.cilib.functions.clustering.ClusteringFunction;
import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.problem.clustering.clustercenterstrategies.ClusterCenterStrategy;
import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;

/**
 * This is the Halkidi-Vazirgiannis Validity Index as described in:
 * @InProceedings{ 657864, author = "Maria Halkidi and Michalis Vazirgiannis", title = "Clustering
 *                 Validity Assessment: Finding the Optimal Partitioning of a Data Set", booktitle =
 *                 "Proceedings of the IEEE International Conference on Data Mining", year = "2001",
 *                 isbn = "0-7695-1119-8", pages = "187--194", publisher = "IEEE Computer Society",
 *                 address = "Washington, DC, USA", }
 * @author Theuns Cloete
 */
public class HalkidiVazirgiannisIndex implements ClusteringFunction<Double> {
    private static final long serialVersionUID = 1164537525165848345L;

    private double stdev;

    /**
     * Create a new instance of {@linkplain HalkidiVazirgiannisIndex}.
     */
    public HalkidiVazirgiannisIndex() {
        this.stdev = 0.0;
    }

    @Override
    public Double apply(List<Cluster> clusters, DataTable<StandardPattern, TypeList> dataTable, DistanceMeasure distanceMeasure, ClusterCenterStrategy clusterCenterStrategy, Vector dataSetMean, double dataSetVariance, double zMax) {
        return this.calculateWithinClusterScatter(clusters, clusterCenterStrategy, dataSetVariance) + this.calculateBetweenClusterSeperation(clusters, distanceMeasure, clusterCenterStrategy);
    }

    /**
     * The variance of the dataset is calculated using the dataset mean, whereas the variance of a specific cluster
     * is calculated using the specific cluster's center as determined by the {@link ClusterCenterStrategy}.
     * @return the within-cluster-scatter for the specific clustering
     */
    private double calculateWithinClusterScatter(List<Cluster> clusters, ClusterCenterStrategy clusterCenterStrategy, double dataSetVariance) {
        double scattering = 0.0;
        int clustersFormed = clusters.size();

        this.stdev = 0.0;

        for (Cluster cluster : clusters) {
            double clusterVariance = cluster.getVariance(clusterCenterStrategy.getCenter(cluster));

            scattering += clusterVariance;
            this.stdev += clusterVariance;
        }

        this.stdev = Math.sqrt(this.stdev) / clustersFormed;
        scattering /= dataSetVariance;
        return scattering / clustersFormed;
    }

    /**
     * Calculate the distances between cluster separation.
     * @return The distance between cluster separation.
     */
    private double calculateBetweenClusterSeperation(List<Cluster> clusters, DistanceMeasure distanceMeasure, ClusterCenterStrategy clusterCenterStrategy) {
        int clustersFormed = clusters.size();
        double density = 0.0;

        for (Cluster leftCluster : clusters) {
            Vector leftCenter = clusterCenterStrategy.getCenter(leftCluster);

            for (Cluster rightCluster : clusters) {
                if (leftCluster != rightCluster) {
                    int midDensity = 0, leftDensity = 0, rightDensity = 0;
                    Vector rightCenter = clusterCenterStrategy.getCenter(rightCluster);
                    Vector midPoint = leftCenter.plus(rightCenter).divide(2.0);

                    for (StandardPattern pattern : leftCluster) {
                        if (distanceMeasure.distance(pattern.getVector(), midPoint) <= stdev) {
                            ++midDensity;
                        }

                        if (distanceMeasure.distance(pattern.getVector(), leftCenter) <= stdev) {
                            ++leftDensity;
                        }
                    }

                    for (StandardPattern pattern : rightCluster) {
                        if (distanceMeasure.distance(pattern.getVector(), midPoint) <= stdev) {
                            ++midDensity;
                        }

                        if (distanceMeasure.distance(pattern.getVector(), rightCenter) <= stdev) {
                            ++rightDensity;
                        }
                    }

                    // prevent division by zero (ArithmeticExceptions)
                    // leftDensity + rightDensity == 0 can mean one of two things:
                    // 1. both clusters didn't have any patterns in it or
                    // 2. the distance between the pattern and midPoint was not > stdev (for both
                    // clusters)
                    if (leftDensity + rightDensity > 0.0) {
                        density += midDensity / (double) Math.max(leftDensity, rightDensity);
                    }
                }
            }
        }
        return density / (clustersFormed * (clustersFormed - 1));
    }
}
