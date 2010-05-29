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
import net.sourceforge.cilib.functions.clustering.clustercenterstrategies.ClusterCenterStrategy;
import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.Pattern;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;

/**
 * This is the Halkidi-Vazirgiannis Validity Index as described in:
 * @InProceedings{ 657864, author = "Maria Halkidi and Michalis Vazirgiannis", title = "Clustering
 *                 Validity Assessment: Finding the Optimal Partitioning of a Data Set", booktitle =
 *                 "Proceedings of the IEEE International Conference on Data Mining", year = "2001",
 *                 isbn = "0-7695-1119-8", pages = "187--194", publisher = "IEEE Computer Society",
 *                 address = "Washington, DC, USA", }
 * NOTE: By default, the cluster center refers to the cluster centroid. See {@link ClusterCenterStrategy}.
 * @author Theuns Cloete
 */
public class HalkidiVazirgiannisIndex extends ClusteringErrorFunction {
    private static final long serialVersionUID = 1164537525165848345L;

    private double stdev;

    /**
     * Create a new instance of {@linkplain HalkidiVazirgiannisIndex}.
     */
    public HalkidiVazirgiannisIndex() {
        this.stdev = 0.0;
    }

    @Override
    public Double apply(ArrayList<Cluster<Vector>> clusters, Set<Pattern<Vector>> patterns, DistanceMeasure distanceMeasure, Vector dataSetMean, double dataSetVariance, double zMax) {
        return this.calculateWithinClusterScatter(clusters, dataSetVariance) + this.calculateBetweenClusterSeperation(clusters, distanceMeasure);
    }

    /**
     * The variance of the dataset is calculated using the dataset mean, whereas the variance of a specific cluster
     * is calculated using the specific cluster's center as determined by the {@link ClusterCenterStrategy}.
     * @return the within-cluster-scatter for the specific clustering
     */
    private double calculateWithinClusterScatter(ArrayList<Cluster<Vector>> clusters, double dataSetVariance) {
        double scattering = 0.0;
        int clustersFormed = clusters.size();

        this.stdev = 0.0;

        for (Cluster<Vector> cluster : clusters) {
            double clusterVariance = cluster.getVariance(this.clusterCenterStrategy.getCenter(cluster));

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
    private double calculateBetweenClusterSeperation(ArrayList<Cluster<Vector>> clusters, DistanceMeasure distanceMeasure) {
        int clustersFormed = clusters.size();
        double density = 0.0;

        for (Cluster<Vector> leftCluster : clusters) {
            Vector leftCenter = this.clusterCenterStrategy.getCenter(leftCluster);

            for (Cluster<Vector> rightCluster : clusters) {
                if (leftCluster != rightCluster) {
                    int midDensity = 0, leftDensity = 0, rightDensity = 0;
                    Vector rightCenter = this.clusterCenterStrategy.getCenter(rightCluster);
                    Vector midPoint = leftCenter.plus(rightCenter).divide(2.0);

                    for (Pattern<Vector> pattern : leftCluster) {
                        if (distanceMeasure.distance(pattern.getData(), midPoint) <= stdev) {
                            ++midDensity;
                        }

                        if (distanceMeasure.distance(pattern.getData(), leftCenter) <= stdev) {
                            ++leftDensity;
                        }
                    }

                    for (Pattern<Vector> pattern : rightCluster) {
                        if (distanceMeasure.distance(pattern.getData(), midPoint) <= stdev) {
                            ++midDensity;
                        }

                        if (distanceMeasure.distance(pattern.getData(), rightCenter) <= stdev) {
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
