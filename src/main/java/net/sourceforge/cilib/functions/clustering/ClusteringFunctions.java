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

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.cilib.problem.dataset.StaticDataSetBuilder;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.Pattern;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;

/**
 * A class that simplifies clustering when making use of a {@link ClusteringProblem}, a
 * {@link StaticDataSetBuilder} and a {@link ClusteringFitnessFunction}. <br/> This
 * class is not dependent on a {@link ClusteringFitnessFunction}, but the
 * {@link ClusteringFitnessFunction}s use this class extensively.
 *
 * @author Theuns Cloete
 */
public final class ClusteringFunctions {

    private ClusteringFunctions() {
    }

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
/*
    public static ArrayList<Cluster<Vector>> arrangeClustersAndCentroids(Vector combinedCentroids, ClusteringProblem problem, StaticDataSetBuilder dataSetBuilder) {
        ArrayList<Vector> separateCentroids = disassembleCentroids(combinedCentroids, problem.getNumberOfClusters());

        return arrangeClustersAndCentroids(separateCentroids, problem, dataSetBuilder);
    }

    public static ArrayList<Cluster<Vector>> arrangeClustersAndCentroids(ArrayList<Vector> separateCentroids, ClusteringProblem problem, StaticDataSetBuilder dataSetBuilder) {
        ArrayList<Cluster<Vector>> clusters = formClusters(separateCentroids, problem, dataSetBuilder.getPatterns());

        return significantClusters(clusters);
    }
*/

    /**
     * Cluster the given patterns using the given centroids (as cluster centers) and the given {@link DistanceMeasure}
     * into the specified number of clusters.
     * @param centroids the list of {@link Vector centroids} that should be used as cluster centers
     * @param patterns the {@link Set} of {@link Pattern patterns} contained in the data set
     * @param distanceMeasure the {@link DistanceMeasure} that should be used to determine similarity between the
     * patterns and centroids
     * @param numberOfClusters the number of clusters that are desired
     * @return a list of {@link Cluster clusters}
     */
    public static ArrayList<Cluster<Vector>> cluster(ArrayList<Vector> centroids, Set<Pattern<Vector>> patterns, DistanceMeasure distanceMeasure, int numberOfClusters) {
        ArrayList<Cluster<Vector>> clusters = new ArrayList<Cluster<Vector>>(numberOfClusters);

        for (Vector centroid : centroids) {
            clusters.add(new Cluster<Vector>(centroid));
        }

        for (Pattern<Vector> pattern : patterns) {
            double minimum = Double.MAX_VALUE;

            for (Cluster<Vector> cluster : clusters) {
                double distance = distanceMeasure.distance(pattern.getData(), cluster.getCentroid());

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
/*
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
*/
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

    public static boolean isValidClustering(ArrayList<Cluster<Vector>> clusters) {
        for (Cluster<Vector> cluster : clusters) {
            if (cluster.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Calculate the diameter of the given cluster, i.e. the distance between the two patterns (in the cluster) that are
     * furthest apart. There exists numerous references for this calculation.
     *
     * @param distanceMeasure the {@link DistanceMeasure} that should be used to determine similarity
     * @param cluster the {@link Cluster} for which the diameter should be calculated
     * @return the diameter of the given cluster.
     */
    public static double clusterDiameter(DistanceMeasure distanceMeasure, Cluster<Vector> cluster) {
        double diameter = 0.0;

        // these loops result in Big-O n (n - 1) but it can be Big-O (n (n - 1)) / 2
        for (Pattern<Vector> leftPattern : cluster) {
            for (Pattern<Vector> rightPattern : cluster) {
                if (leftPattern != rightPattern) {
                    diameter = Math.max(diameter, distanceMeasure.distance(leftPattern.getData(), rightPattern.getData()));
                }
            }
        }
        return diameter;
    }

    /**
     * Calculate the average distance between two clusters. Illustrated in Equation 22 of:<br/>
     *
     * @Article{ 678624, title = "Some New Indexes of Cluster Validity", author = "James C.
     *           Bezdek and Nikhil R. Pal", journal = "IEEE Transactions on Systems, Man, and
     *           Cybernetics, Part B: Cybernetics", pages = "301--315", volume = "28", number =
     *           "3", month = jun, year = "1998", issn = "1083-4419" }
     * @param distanceMeasure the {@link DistanceMeasure} that should be used to determine similarity
     * @param lhs the LHS cluster
     * @param rhs the RHS cluster
     * @return the average distance between the patterns of the two clusters
     */
    public static double averageClusterDistance(DistanceMeasure distanceMeasure, Cluster<Vector> lhs, Cluster<Vector> rhs) {
        double distance = 0.0;

        for (Pattern<Vector> leftPattern : lhs) {
            for (Pattern<Vector> rightPattern : rhs) {
                distance += distanceMeasure.distance(leftPattern.getData(), rightPattern.getData());
            }
        }
        return distance / (lhs.size() * rhs.size());
    }

    /**
     * Calculate the maximum distance between two clusters. Illustrated in Equation 21 of:<br/>
     *
     * @Article{ 678624, title = "Some New Indexes of Cluster Validity", author = "James C.
     *           Bezdek and Nikhil R. Pal", journal = "IEEE Transactions on Systems, Man, and
     *           Cybernetics, Part B: Cybernetics", pages = "301--315", volume = "28", number =
     *           "3", month = jun, year = "1998", issn = "1083-4419" }
     * @param distanceMeasure the {@link DistanceMeasure} that should be used to determine similarity
     * @param lhs the LHS cluster
     * @param rhs the RHS cluster
     * @return the longest distance between the patterns of the two clusters
     */
    public static double maximumClusterDistance(DistanceMeasure distanceMeasure, Cluster<Vector> lhs, Cluster<Vector> rhs) {
        double distance = -Double.MAX_VALUE;

        for (Pattern<Vector> leftPattern : lhs) {
            for (Pattern<Vector> rightPattern : rhs) {
                distance = Math.max(distance, distanceMeasure.distance(leftPattern.getData(), rightPattern.getData()));
            }
        }
        return distance;
    }

    /**
     * Calculate the minimum distance between two clusters. This is illustrated in Equation 20 of:<br/>
     *
     * @Article{ 678624, title = "Some New Indexes of Cluster Validity", author = "James C.
     *           Bezdek and Nikhil R. Pal", journal = "IEEE Transactions on Systems, Man, and
     *           Cybernetics, Part B: Cybernetics", pages = "301--315", volume = "28", number =
     *           "3", month = jun, year = "1998", issn = "1083-4419" }
     * @param distanceMeasure the {@link DistanceMeasure} that should be used to determine similarity
     * @param lhs the LHS cluster
     * @param rhs the RHS cluster
     * @return the shortest distance between the patterns of the two clusters
     */
    public static double minimumClusterDistance(DistanceMeasure distanceMeasure, Cluster<Vector> lhs, Cluster<Vector> rhs) {
        double distance = Double.MAX_VALUE;

        for (Pattern<Vector> leftPattern : lhs) {
            for (Pattern<Vector> rightPattern : rhs) {
                distance = Math.min(distance, distanceMeasure.distance(leftPattern.getData(), rightPattern.getData()));
            }
        }
        return distance;
    }

    /**
     * Calculate the maximum value in the data set. In other words, calculate the maximum value possible for the given
     * prototype based on its upper and lower bounds.
     * @return the maximum value possible for the given prototype
     */
    public static double zMax(Vector prototype) {
        Preconditions.checkState(prototype != null && !prototype.isEmpty(), "Cannot calculate the zMax for a null or empty prototype");
        double zMax = 1.0;

        for (Numeric feature : prototype) {
            Bounds bounds = feature.getBounds();

            zMax *= (bounds.getUpperBound() - bounds.getLowerBound());
        }
        return zMax;
    }
}
