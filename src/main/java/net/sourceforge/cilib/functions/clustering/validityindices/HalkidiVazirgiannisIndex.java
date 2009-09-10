/**
 * Copyright (C) 2003 - 2009
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.functions.clustering.validityindices;

import net.sourceforge.cilib.functions.clustering.ClusteringFitnessFunction;
import net.sourceforge.cilib.math.Stats;
import net.sourceforge.cilib.problem.dataset.ClusterableDataSet.Pattern;
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
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double calculateFitness() {
        return calculateWithinClusterScatter() + calculateBetweenClusterSeperation();
    }

    /**
     * The variance of the dataset is calculated using the dataset mean, whereas the variance of a specific cluster
     * is calculated using the specific cluster's center as determined by the {@link ClusterCenterStrategy}.
     * @return the within-cluster-scatter for the specific clustering
     */
    protected double calculateWithinClusterScatter() {
        double scattering = 0.0;
        double datasetVariance = helper.getDataSetVariance();
        double clusterVariance = 0.0;

        stdev = 0.0;
        for (int i = 0; i < clustersFormed; i++) {
            clusterVariance = Stats.variance(arrangedClusters.get(i).values(), clusterCenterStrategy.getCenter(i));
            scattering += clusterVariance;
            stdev += clusterVariance;
        }
        stdev = Math.sqrt(stdev);
        stdev /= clustersFormed;
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

        for (int i = 0; i < clustersFormed; i++) {
            leftCenter = clusterCenterStrategy.getCenter(i);
            for (int j = 0; j < clustersFormed; j++) {
                if (i != j) {
                    rightCenter = clusterCenterStrategy.getCenter(j);
                    midPoint = leftCenter.plus(rightCenter);
                    midPoint = midPoint.divide(2.0);
                    midDensity = leftDensity = rightDensity = 0;

                    for (Pattern pattern : arrangedClusters.get(i).values()) {
                        if (helper.calculateDistance(pattern.data, midPoint) <= stdev)
                            ++midDensity;
                        if (helper.calculateDistance(pattern.data, leftCenter) <= stdev)
                            ++leftDensity;
                    }

                    for (Pattern pattern : arrangedClusters.get(j).values()) {
                        if (helper.calculateDistance(pattern.data, midPoint) <= stdev)
                            ++midDensity;
                        if (helper.calculateDistance(pattern.data, rightCenter) <= stdev)
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
