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
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.List;

import net.sourceforge.cilib.io.DataTable;
import net.sourceforge.cilib.io.pattern.StandardPattern;
import net.sourceforge.cilib.type.types.Bounds;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;

/**
 * This class defines some helper methods for working with clusters.
 *
 * @author Theuns Cloete
 */
public final class ClusteringFunctions {

    private ClusteringFunctions() {
    }

    /**
     * Cluster the {@link StandardPattern patterns} in the given {@link DataTable} (using the given centroids as cluster
     * centers and the given {@link DistanceMeasure}) into the specified number of clusters.
     * @param centroids the list of {@link Vector centroids} that should be used as cluster centers
     * @param dataTable the {@link DataTable} of {@link StandardPattern patterns}
     * @param distanceMeasure the {@link DistanceMeasure} that should be used to determine similarity between the
     * patterns and centroids
     * @param numberOfClusters the number of clusters that are desired
     * @return a {@link Collection} of {@link Cluster clusters}
     */
    public static List<Cluster> cluster(Collection<Vector> centroids, DataTable<StandardPattern, TypeList> dataTable, DistanceMeasure distanceMeasure, int numberOfClusters) {
        List<Cluster> clusters = Lists.newArrayListWithCapacity(numberOfClusters);

        for (Vector centroid : centroids) {
            clusters.add(new Cluster(centroid));
        }

        for (StandardPattern pattern : dataTable) {
            double minimum = Double.MAX_VALUE;

            for (Cluster cluster : clusters) {
                double distance = distanceMeasure.distance(pattern.getVector(), cluster.getCentroid());

                if (distance < minimum) {
                    minimum = distance;

                    for (Cluster other : clusters) {
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
     * Combine the given {@link Collection} of {@link Vector centroids} into a single {@link Vector} containing
     * all the centroids, one after the other. For example, the following vectors: [1,2,3], [4,5,6] and [7,8,9]<br/>
     * will be combined into: [1,2,3,4,5,6,7,8,9]
     *
     * @param separateCentroids the {@link Collection} of {@link Vector centroids} that should be combined into a
     * single {@link Vector}
     * @return a single {@link Vector} constructed by appending the given {@link Collection} of
     * {@link Vector centroids}
     */
    public static Vector assembleCentroids(Collection<Vector> separateCentroids) {
        Vector.Builder assembled = Vector.newBuilder();

        for (Vector centroid : separateCentroids) {
            assembled.copyOf(centroid);
        }
        return assembled.build();
    }

    /**
     * Split up the given combined {@link Vector centroids} based on the given number of centroids and add the separate
     * centroids to a {@link Collection} of {@link Vector centroids} such that each element in the collection consist of
     * a single {@link Vector centroid}. For example, the following vector: [1,2,3,4,5,6,7,8,9]<br/>
     * will be split up into:
     * [1,2,3], [4,5,6] and [7,8,9]<br/>
     * when the specified number of centroids is 3.
     *
     * @param combinedCentroids the {@link Vector centroids} that should be split up
     * @param numberOfCentroids the desired number of {@link Vector centroids}
     * @return a {@link Collection} containing the separate {@link Vector centroids}
     */
    public static List<Vector> disassembleCentroids(Vector combinedCentroids, int numberOfCentroids) {
        List<Vector> disassembled = Lists.newArrayListWithCapacity(numberOfCentroids);
        int dimension = combinedCentroids.size() / numberOfCentroids;

        for (int i = 0, n = numberOfCentroids; i < n; ++i) {
            disassembled.add(combinedCentroids.copyOfRange(i * dimension, (i * dimension) + dimension));
        }
        return disassembled;
    }

    /**
     * Determine and retrieve only the significant clusters from the given clusters. Empty clusters are a result of
     * centroids that are not associated with any of the patterns in the dataset. These empty clusters are insignificant
     * and should not be included in the calculation of fitness functions or validity indices. This method returns a
     * list of only the significant clusters, by ignoring the insignificant clusters in the given list of clusters.
     *
     * @param clusters a {@link Collection} of clusters, whether significant or not
     * @return a {@link Collection} of only significant (non-empty) {@link Cluster clusters}.
     */
    public static List<Cluster> significantClusters(Collection<Cluster> clusters) {
        List<Cluster> significantClusters = Lists.newArrayListWithCapacity(clusters.size());

        for (Cluster cluster : clusters) {
            if (!cluster.isEmpty()) {
                significantClusters.add(cluster);
            }
        }
        return significantClusters;
    }

    /**
     * Determine whether the given {@link Collection} of {@link Cluster clusters} is a valid <em>clustering</em>. If one
     * of the clusters {@link Cluster#isEmpty() is empty} then the clustering is invalid.
     * @param clusters the {@link Collection} of clusters that should be validated
     * @return true when the clustering is valid, false otherwise
     */
    public static boolean isValidClustering(Collection<Cluster> clusters) {
        for (Cluster cluster : clusters) {
            if (cluster.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Calculate the diameter of the given {@link Cluster}, i.e. the distance between the two patterns (in the cluster)
     * that are furthest apart. If the cluster size is <code>n</code>, then the complexity of this method is
     * <code>O(n<sup>2</sup>)</code>, although it is optimised to minimise the number of distance calculations such that
     * there will only be <code>(n * (n - 1)) / 2</code> distance calculations, e.g. (5 * (5 - 1)) / 2 = 10.
     *
     * @param distanceMeasure the {@link DistanceMeasure} that should be used to determine similarity
     * @param cluster the {@link Cluster} whose diameter should be calculated
     * @return the diameter of the given {@link Cluster}
     */
    public static double clusterDiameter(DistanceMeasure distanceMeasure, Cluster cluster) {
        Multimap<StandardPattern, StandardPattern> alreadyCalculated = HashMultimap.create();
        double diameter = 0.0;

        for (StandardPattern leftPattern : cluster) {
            for (StandardPattern rightPattern : cluster) {
                if (leftPattern != rightPattern && !alreadyCalculated.get(leftPattern).contains(rightPattern) && !alreadyCalculated.get(rightPattern).contains(leftPattern)) {
                    diameter = Math.max(diameter, distanceMeasure.distance(leftPattern.getVector(), rightPattern.getVector()));
                    alreadyCalculated.put(leftPattern, rightPattern);
                    alreadyCalculated.put(rightPattern, leftPattern);
                }
            }
        }
        return diameter;
    }

    /**
     * Calculate the average distance between two clusters. Illustrated in equation 22 of <em>Some New Indexes of Cluster
     * Validity</em> by <b>James C. Bezdek and Nikhil R. Pal</b> on pages 301-315 in IEEE Transactions on Systems, Man,
     * and Cybernetics, Part B: Cybernetics, Volume 28, Number 3, June 1998.
     * @param distanceMeasure the {@link DistanceMeasure} that should be used to determine similarity
     * @param lhs the LHS cluster
     * @param rhs the RHS cluster
     * @return the average distance between the patterns of the two clusters
     */
    public static double averageClusterDistance(DistanceMeasure distanceMeasure, Cluster lhs, Cluster rhs) {
        double distance = 0.0;

        for (StandardPattern leftPattern : lhs) {
            for (StandardPattern rightPattern : rhs) {
                distance += distanceMeasure.distance(leftPattern.getVector(), rightPattern.getVector());
            }
        }
        return distance / (lhs.size() * rhs.size());
    }

    /**
     * Calculate the maximum distance between two clusters. Illustrated in equation 21 of <em>Some New Indexes of Cluster
     * Validity</em> by <b>James C. Bezdek and Nikhil R. Pal</b> on pages 301-315 in IEEE Transactions on Systems, Man,
     * and Cybernetics, Part B: Cybernetics, Volume 28, Number 3, June 1998.
     * @param distanceMeasure the {@link DistanceMeasure} that should be used to determine similarity
     * @param lhs the LHS cluster
     * @param rhs the RHS cluster
     * @return the longest distance between the patterns of the two clusters
     */
    public static double maximumClusterDistance(DistanceMeasure distanceMeasure, Cluster lhs, Cluster rhs) {
        double distance = -Double.MAX_VALUE;

        for (StandardPattern leftPattern : lhs) {
            for (StandardPattern rightPattern : rhs) {
                distance = Math.max(distance, distanceMeasure.distance(leftPattern.getVector(), rightPattern.getVector()));
            }
        }
        return distance;
    }

    /**
     * Calculate the minimum distance between two clusters. This is illustrated in equation 20 of <em>Some New Indexes of
     * Cluster Validity</em> by <b>James C. Bezdek and Nikhil R. Pal</b> on pages 301-315 in IEEE Transactions on
     * Systems, Man, and Cybernetics, Part B: Cybernetics, Volume 28, Number 3, June 1998.<br/>
     * @param distanceMeasure the {@link DistanceMeasure} that should be used to determine similarity
     * @param lhs the LHS cluster
     * @param rhs the RHS cluster
     * @return the shortest distance between the patterns of the two clusters
     */
    public static double minimumClusterDistance(DistanceMeasure distanceMeasure, Cluster lhs, Cluster rhs) {
        double distance = Double.MAX_VALUE;

        for (StandardPattern leftPattern : lhs) {
            for (StandardPattern rightPattern : rhs) {
                distance = Math.min(distance, distanceMeasure.distance(leftPattern.getVector(), rightPattern.getVector()));
            }
        }
        return distance;
    }

    /**
     * Calculate the maximum value in the data set. In other words, calculate the maximum value possible for the given
     * prototype based on its upper and lower bounds. This can be seen as the <code>volume</code> of the given
     * prototype&apos;s domain. For example, if the domain is <code>R(0.0,5.0),R(2.0,5.0),R(1.0,3.0)</code> then the
     * <code>zMax</code> will be (5 - 0) x (5 - 2) x (3 - 1) = 5 x 3 x 2 = 30.
     * {@link Vector}.
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
