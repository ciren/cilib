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
package net.sourceforge.cilib.util;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.cilib.functions.clustering.ClusteringFitnessFunction;
import net.sourceforge.cilib.problem.ClusteringProblem;
import net.sourceforge.cilib.problem.dataset.DataSetBuilder;
import net.sourceforge.cilib.problem.dataset.StaticDataSetBuilder;
import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.Pattern;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * A class that simplifies clustering when making use of a {@link ClusteringProblem}, a
 * {@link StaticDataSetBuilder} and a {@link ClusteringFitnessFunction}. <br/> This
 * class is not dependent on a {@link ClusteringFitnessFunction}, but the
 * {@link ClusteringFitnessFunction}s use this class extensively.
 *
 * @author Theuns Cloete
 */
public final class ClusteringUtils {

    /**
     * The three methods called in this method must be called in that specific order, i.e.
     * <ol>
     * <li>Disassemble the centroids (split them up to be manageable)</li>
     * <li>Form the clusters (assign patterns to their closest centroids) (depends on Step 1)</li>
     * <li>Remove the empty clusters and their associated centroids from the <i>arranged
     * lists</i>, thereby finalizing the arranging of clusters (depends on both Steps 1 & 2)</li>
     * </ol>
     *
     * @param combinedCentroids the {@link Vector} that represents all the centroids
     * @param problem the {@link ClusteringProblem}
     * @param dataSetBuilder the {@link DataSetBuilder} that contains the patterns
     * @return an {@link ArrayList} of {@link Cluster clusters}
     */
    public static ArrayList<Cluster<Vector>> arrangeClustersAndCentroids(Vector combinedCentroids, ClusteringProblem problem, StaticDataSetBuilder dataSetBuilder) {
        ArrayList<Vector> separateCentroids = disassembleCentroids(combinedCentroids, problem.getNumberOfClusters());

        return arrangeClustersAndCentroids(separateCentroids, problem, dataSetBuilder);
    }

    public static ArrayList<Cluster<Vector>> arrangeClustersAndCentroids(ArrayList<Vector> separateCentroids, ClusteringProblem problem, StaticDataSetBuilder dataSetBuilder) {
        ArrayList<Cluster<Vector>> clusters = formClusters(separateCentroids, problem, dataSetBuilder.getPatterns());

        return significantClusters(clusters);
    }

    /**
     * Take the given list of centroid vectors and combine them such that a single {@link Vector} is constructed
     * containing all of the centroids vectors, one after the other.
     *
     * @param separateCentroids the list of centroid vectors that should be combined into a single vector
     * @return a single {@link Vector} constructed by appending the given list of centroids
     */
    public static Vector assembleCentroids(ArrayList<Vector> separateCentroids) {
        Vector.Builder assembled = Vector.newBuilder();

        for (Vector centroid : separateCentroids) {
            assembled.copyOf(centroid);
        }
        return assembled.build();
    }

    /**
     * Take the given centroids {@link Vector}, split it up and construct a {@link List} of vectors such that each
     * element in the list consist of a single centroid.
     *
     * @param combinedCentroids the centroids {@link Vector} that should be split up
     * @return a {@link List} of centroid {@link Vector vectors}
     */
    public static ArrayList<Vector> disassembleCentroids(Vector combinedCentroids, int numberOfClusters) {
        ArrayList<Vector> disassembled = Lists.newArrayList();
        int dimension = combinedCentroids.size() / numberOfClusters;

        for (int i = 0, n = numberOfClusters; i < n; ++i) {
            disassembled.add(combinedCentroids.copyOfRange(i * dimension, (i * dimension) + dimension));
        }
        return disassembled;
    }

    /**
     * Create an {@link ArrayList} of {@link Cluster clusters} where each cluster contains the {@link Pattern patterns}
     * that are closest to its {@link Cluster#getCentroid() associated centroid}.
     *
     * @return an {@link ArrayList} of {@link Cluster clusters} where each {@link Pattern} have been assigned to its
     *         closest centroid
     */
    public static ArrayList<Cluster<Vector>> formClusters(ArrayList<Vector> centroids, ClusteringProblem problem, Set<Pattern<Vector>> patterns) {
        ArrayList<Cluster<Vector>> clusters = Lists.newArrayList();

        for (Vector centroid : centroids) {
            clusters.add(new Cluster<Vector>(centroid));
        }

        for (Pattern<Vector> pattern : patterns) {
            double minimum = Double.MAX_VALUE;

            for (Cluster<Vector> cluster : clusters) {
                double distance = problem.calculateDistance(pattern.getData(), cluster.getCentroid());

                if (distance < minimum) {
                    minimum = distance;

                    for (Cluster<Vector> other : clusters) {
                        // a cluster is a set and can therefore contain the pattern only once
                        if (other.remove(pattern)) {
                            break;  // only one cluster can contain the pattern
                        }
                    }
                    cluster.add(pattern);
                }
            }
        }
        return clusters;
    }

    /**
     * Retrieve the significant clusters. Empty clusters are a result of centroids that are not associated with any
     * of the patterns in the dataset. These empty clusters are insignificant and should not be included in the
     * calculation of fitness functions or validity indices. This method returns a list of only
     * the significant clusters, by ignoring the insignificant clusters in the given list of clusters.
     *
     * @param clusters a list containing clusters, whether significant or not
     * @return an {@link ArrayList} of significant (non-empty) {@link Cluster clusters}.
     */
    public static ArrayList<Cluster<Vector>> significantClusters(ArrayList<Cluster<Vector>> clusters) {
        ArrayList<Cluster<Vector>> significantClusters = Lists.newArrayList();

        for (Cluster<Vector> cluster : clusters) {
            if (!cluster.isEmpty()) {
                significantClusters.add(cluster);
            }
        }
        return significantClusters;
    }
}
