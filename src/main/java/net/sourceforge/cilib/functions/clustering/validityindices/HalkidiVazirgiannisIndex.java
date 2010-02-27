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

import net.sourceforge.cilib.functions.clustering.ClusteringFitnessFunction;
import net.sourceforge.cilib.problem.dataset.StaticDataSetBuilder;
import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.Pattern;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This is the Halkidi-Vazirgiannis Validity Index.
 *
 * The index is described in:
 * @InProceedings{ 657864, author = "Maria Halkidi and Michalis Vazirgiannis", title = "Clustering
 *                 Validity Assessment: Finding the Optimal Partitioning of a Data Set", booktitle =
 *                 "Proceedings of the IEEE International Conference on Data Mining", year = "2001",
 *                 isbn = "0-7695-1119-8", pages = "187--194", publisher = "IEEE Computer Society",
 *                 address = "Washington, DC, USA", }
 * NOTE: By default, the cluster center refers to the cluster centroid. See {@link ClusterCenterStrategy}.
 */
public class HalkidiVazirgiannisIndex extends ClusteringFitnessFunction {
    private static final long serialVersionUID = 1164537525165848345L;
    private double stdev = 0.0;

    /**
     * Create a new instance of {@linkplain HalkidiVazirgiannisIndex}.
     */
    public HalkidiVazirgiannisIndex() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double calculateFitness() {
        return this.calculateWithinClusterScatter() + this.calculateBetweenClusterSeperation();
    }

    /**
     * The variance of the dataset is calculated using the dataset mean, whereas the variance of a specific cluster
     * is calculated using the specific cluster's center as determined by the {@link ClusterCenterStrategy}.
     * @return the within-cluster-scatter for the specific clustering
     */
    protected double calculateWithinClusterScatter() {
        double scattering = 0.0;
        double datasetVariance = ((StaticDataSetBuilder) this.problem.getDataSetBuilder()).getVariance();
        double clusterVariance = 0.0;

        this.stdev = 0.0;
        for (Cluster<Vector> cluster : this.significantClusters) {
            clusterVariance = cluster.getVariance(this.clusterCenterStrategy.getCenter(cluster));
            scattering += clusterVariance;
            this.stdev += clusterVariance;
        }
        this.stdev = Math.sqrt(this.stdev);
        this.stdev /= clustersFormed;
        scattering /= datasetVariance;
        return scattering / clustersFormed;
    }

    /**
     * Calculate the distances between cluster separation.
     * @return The distance between cluster separation.
     */
    protected double calculateBetweenClusterSeperation() {
        Vector midPoint = null, leftCenter = null, rightCenter = null;
        double density = 0.0;
        int midDensity = 0, leftDensity = 0, rightDensity = 0;

        for (Cluster<Vector> leftCluster : this.significantClusters) {
            leftCenter = this.clusterCenterStrategy.getCenter(leftCluster);
            for (Cluster<Vector> rightCluster : this.significantClusters) {
                if (leftCluster != rightCluster) {
                    rightCenter = this.clusterCenterStrategy.getCenter(rightCluster);
                    midPoint = leftCenter.plus(rightCenter);
                    midPoint = midPoint.divide(2.0);
                    midDensity = leftDensity = rightDensity = 0;

                    for (Pattern<Vector> pattern : leftCluster) {
                        if (this.problem.calculateDistance(pattern.getData(), midPoint) <= stdev)
                            ++midDensity;
                        if (this.problem.calculateDistance(pattern.getData(), leftCenter) <= stdev)
                            ++leftDensity;
                    }

                    for (Pattern<Vector> pattern : rightCluster) {
                        if (this.problem.calculateDistance(pattern.getData(), midPoint) <= stdev)
                            ++midDensity;
                        if (this.problem.calculateDistance(pattern.getData(), rightCenter) <= stdev)
                            ++rightDensity;
                    }

                    // prevent devision by zero (ArithmeticExceptions)
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

    /**
     * {@inheritDoc}
     */
    @Override
    public HalkidiVazirgiannisIndex getClone() {
        return new HalkidiVazirgiannisIndex();
    }
}
