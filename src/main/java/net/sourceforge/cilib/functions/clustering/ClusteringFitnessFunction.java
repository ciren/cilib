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

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.functions.clustering.clustercenterstrategies.ClusterCenterStrategy;
import net.sourceforge.cilib.functions.clustering.clustercenterstrategies.ClusterCentroidStrategy;
import net.sourceforge.cilib.problem.ClusteringProblem;
import net.sourceforge.cilib.problem.dataset.StaticDataSetBuilder;
import net.sourceforge.cilib.type.types.container.Cluster;
import net.sourceforge.cilib.type.types.container.Pattern;
import net.sourceforge.cilib.type.types.container.Vector;
import net.sourceforge.cilib.util.ClusteringUtils;

/**
 * This abstract class defines member variables and member functions that can be used by
 * subclasses to calculate the fitness of a particular clustering in their respective
 * evaluate methods.<br/>This class makes extensive use of the {@link ClusteringUtils}
 * helper class. The {@link #helper} variable is set at the beginning of the
 * {@link #evaluate(Vector)} method so that it can be accessible to the classes that inherit
 * from this one. <br/> This class makes use of a {@link ClusterCenterStrategy} to enable
 * the user of this class to specify what is meant by the <i>center</i> of a cluster,
 * because sometimes the <i>centroid</i> is used and other times the <i>mean</i> is used.
 * By default, the cluster center is interpreted as the cluster centroid.
 *
 * @author Theuns Cloete
 */
public abstract class ClusteringFitnessFunction extends ContinuousFunction {
    private static final long serialVersionUID = 4834673666638644106L;

    protected ClusteringProblem problem;
    protected ClusterCenterStrategy clusterCenterStrategy;
    protected ArrayList<Cluster<Vector>> significantClusters;
    protected int clustersFormed = 0;

    /**
     * This constructor cannot be called directly since this is an abstract class. Subclasses
     * call this constructor, from their constructor(s) via the super() command. The default
     * domain is not set, because it should be specified on the {@link ClusteringProblem}.
     */
    public ClusteringFitnessFunction() {
        this.clusterCenterStrategy = new ClusterCentroidStrategy();
    }

    @Override
    public abstract ClusteringFitnessFunction getClone();

    /**
     * This method is responsible for various things before the fitness can be returned:
     * <ol>
     * <li>Arrange the patterns in the dataset to belong to its closest centroid. We don't
     * care how this is done, since it is handled by the
     * {@link ClusteringUtils#arrangeClustersAndCentroids(net.sourceforge.cilib.type.types.container.Vector, net.sourceforge.cilib.problem.ClusteringProblem, net.sourceforge.cilib.problem.dataset.StaticDataSetBuilder)}
     * method. We also assume that this method only returns significant clusters</li>
     * <li>Sets the {@link #significantClusters} member variable which is available to
     * sub-classes.</li>
     * <li>Calculate the fitness using the given centroids {@linkplain Vector}. We don't
     * care how this is done, since it is handled by the abstraction (polymorphism) created
     * by the hierarchy of this class. This is achieved via the abstract
     * {@link #calculateFitness()} method</li>
     * </ol>
     * Steps 1 and 2 have to be performed before the fitness is calculated, using the given
     * <tt>centroids</tt> {@linkplain Vector}, in step 3.
     *
     * @param centroids The {@link Vector} representing the centroid vectors
     * @return the fitness that has been calculated
     */
    @Override
    public Double apply(Vector centroids) {
        //TODO: When we start using Guice, this statement should be updated (we want the main algorithm)
        this.problem = (ClusteringProblem) AbstractAlgorithm.getAlgorithmList().get(0).getOptimisationProblem();

        significantClusters = ClusteringUtils.arrangeClustersAndCentroids(centroids, this.problem, (StaticDataSetBuilder) this.problem.getDataSetBuilder());
        clustersFormed = significantClusters.size();

        /*
         * TODO: Figure out a nice OO way to determine whether this function is being
         * optimised as a FunctionMinimisationProblem or FunctionMaximisationProblem and then
         * return the appropriate value
         */
        if (clustersFormed < 2) {
            return worstFitness();
        }

        return calculateFitness();
//        return validateFitness(calculateFitness());
    }

    public abstract double calculateFitness();

    /**
     * Calculate the Quantisation Error.
     *
     * <p>This is explained in Section 4.1.1 on pages 104 & 105 of:<br/>
     *
     * @PhDThesis{ omran2004thesis, title = "Particle Swarm Optimization Methods for Pattern
     *             Recognition and Image Processing", author = "Mahamed G.H. Omran",
     *             institution = "University Of Pretoria", school = "Computer Science", year =
     *             "2004", month = nov, address = "Pretoria, South Africa", note =
     *             "Supervisor: A. P. Engelbrecht", }
     * @return the Quantisation Error of the particular clustering.
     */
    public double calculateQuantisationError() {
        double quantisationError = 0.0;

        for (Cluster<Vector> cluster : this.significantClusters) {
            double averageCompactness = 0.0;
            Vector center = this.clusterCenterStrategy.getCenter(cluster);

            for (Pattern<Vector> pattern : cluster) {
                averageCompactness += this.problem.calculateDistance(pattern.getData(), center);
            }
            averageCompactness /= cluster.size();
            quantisationError += averageCompactness;
        }
        quantisationError /= clustersFormed;
        return quantisationError;
    }

    /**
     * Calculate the Maximum Average Distance between the patterns in the dataset and the
     * centers learned so far.
     *
     * <p>
     * See Section 4.1.1 at the bottom of page 105 of:<br/>
     *
     * @PhDThesis{ omran2004thesis, title = "Particle Swarm Optimization Methods for Pattern
     *             Recognition and Image Processing", author = "Mahamed G.H. Omran",
     *             institution = "University Of Pretoria", school = "Computer Science", year =
     *             "2004", month = nov, address = "Pretoria, South Africa", note =
     *             "Supervisor: A. P. Engelbrecht", }
     * @return the maximum average distance between the patterns of a cluster and their
     *         associated center.
     */
    public double calculateMaximumAverageDistance() {
        double maximumAverageDistance = 0.0;

        for (Cluster<Vector> cluster : this.significantClusters) {
            double averageDistance = 0.0;
            Vector center = this.clusterCenterStrategy.getCenter(cluster);

            for (Pattern<Vector> pattern : cluster) {
                averageDistance += this.problem.calculateDistance(pattern.getData(), center);
            }
            averageDistance /= cluster.size();
            maximumAverageDistance = Math.max(maximumAverageDistance, averageDistance);
        }
        return maximumAverageDistance;
    }

    /**
     * Calculate the shortest distance between two clusters. In other words, the shortest
     * distance between the centroids of any two clusters.
     *
     * @return the minimum inter-cluster distance
     */
    public double calculateMinimumInterClusterDistance() {
        double minimumInterClusterDistance = Double.MAX_VALUE;

        for (int i = 0; i < clustersFormed - 1; ++i) {
            Vector leftCenter = this.clusterCenterStrategy.getCenter(this.significantClusters.get(i));

            for (int j = i + 1; j < clustersFormed; ++j) {
                Vector rightCenter = this.clusterCenterStrategy.getCenter(this.significantClusters.get(j));

                minimumInterClusterDistance = Math.min(minimumInterClusterDistance, this.problem.calculateDistance(leftCenter, rightCenter));
            }
        }
        return minimumInterClusterDistance;
    }

    /**
     * Calculate the longest distance between two clusters. In other words, the longest
     * distance between the centroids of any two clusters.
     *
     * @return the maximum inter-cluster distance.
     */
    public double calculateMaximumInterClusterDistance() {
        double maximumInterClusterDistance = -Double.MAX_VALUE;

        for (int i = 0; i < clustersFormed - 1; ++i) {
            Vector leftCenter = this.clusterCenterStrategy.getCenter(this.significantClusters.get(i));

            for (int j = i + 1; j < clustersFormed; ++j) {
                Vector rightCenter = this.clusterCenterStrategy.getCenter(this.significantClusters.get(j));

                maximumInterClusterDistance = Math.max(maximumInterClusterDistance, this.problem.calculateDistance(leftCenter, rightCenter));
            }
        }
        return maximumInterClusterDistance;
    }

    /**
     * Calculate the minimum distance between two clusters (sets).
     *
     * <p>
     * This is illustrated in Equation 20 of:<br/>
     *
     * @Article{ 678624, title = "Some New Indexes of Cluster Validity", author = "James C.
     *           Bezdek and Nikhil R. Pal", journal = "IEEE Transactions on Systems, Man, and
     *           Cybernetics, Part B: Cybernetics", pages = "301--315", volume = "28", number =
     *           "3", month = jun, year = "1998", issn = "1083-4419" }
     * @param i the index of the LHS cluster
     * @param j the index of the RHS cluster
     * @return the shortest distance between the patterns of two clusters (sets)
     */
    public double calculateMinimumSetDistance(int i, int j) {
        double distance = Double.MAX_VALUE;

        for (Pattern<Vector> leftPattern : this.significantClusters.get(i)) {
            for (Pattern<Vector> rightPattern : this.significantClusters.get(j)) {
                distance = Math.min(distance, this.problem.calculateDistance(leftPattern.getData(), rightPattern.getData()));
            }
        }
        return distance;
    }

    /**
     * Calculate the maximum distance between two clusters (sets).
     *
     * <p>Illustrated in Equation 21 of:<br/>
     *
     * @Article{ 678624, title = "Some New Indexes of Cluster Validity", author = "James C.
     *           Bezdek and Nikhil R. Pal", journal = "IEEE Transactions on Systems, Man, and
     *           Cybernetics, Part B: Cybernetics", pages = "301--315", volume = "28", number =
     *           "3", month = jun, year = "1998", issn = "1083-4419" }
     * @param i the index of the LHS cluster.
     * @param j the index of the RHS cluster.
     * @return the longest distance between the patterns of two clusters (sets).
     */
    public double calculateMaximumSetDistance(int i, int j) {
        double distance = -Double.MAX_VALUE;

        for (Pattern<Vector> leftPattern : this.significantClusters.get(i)) {
            for (Pattern<Vector> rightPattern : this.significantClusters.get(j)) {
                distance = Math.max(distance, this.problem.calculateDistance(leftPattern.getData(), rightPattern.getData()));
            }
        }
        return distance;
    }

    /**
     * Calculate the average distance between two clusters (sets).
     *
     * <p>
     * Illustrated in Equation 22 of:<br/>
     *
     * @Article{ 678624, title = "Some New Indexes of Cluster Validity", author = "James C.
     *           Bezdek and Nikhil R. Pal", journal = "IEEE Transactions on Systems, Man, and
     *           Cybernetics, Part B: Cybernetics", pages = "301--315", volume = "28", number =
     *           "3", month = jun, year = "1998", issn = "1083-4419" }
     * @param i the index of the LHS cluster.
     * @param j the index of the RHS cluster.
     * @return the average distance between the patterns of two clusters (sets).
     */
    public double calculateAverageSetDistance(int i, int j) {
        double distance = 0.0;

        for (Pattern<Vector> leftPattern : this.significantClusters.get(i)) {
            for (Pattern<Vector> rightPattern : this.significantClusters.get(j)) {
                distance += this.problem.calculateDistance(leftPattern.getData(), rightPattern.getData());
            }
        }
        return distance / (this.significantClusters.get(i).size() * this.significantClusters.get(j).size());
    }

    /**
     * Calculate the diameter of the given cluster, i.e. the distance between the two
     * patterns (in the set) that are furthest apart. There exists numerous references
     * for this calculation.
     *
     * @param k the index of the cluster for which the diameter should be calculated.
     * @return the diameter of the given cluster.
     */
    public double calculateClusterDiameter(int k) {
        double diameter = 0.0;
        Cluster<Vector> cluster = this.significantClusters.get(k);

        // these loops result in Big-O n (n - 1) but it can be Big-O (n (n - 1)) / 2
        for (Pattern<Vector> leftPattern : cluster) {
            for (Pattern<Vector> rightPattern : cluster) {
                if (leftPattern != rightPattern) {
                    diameter = Math.max(diameter, this.problem.calculateDistance(leftPattern.getData(), rightPattern.getData()));
                }
            }
        }
        return diameter;
    }

    /**
     * Calculate the intra-cluster distance. In other words, the sum of the distances between
     * all patterns of all clusters and their associated centroids. The calculation is
     * specified by Equation 13 in Section IV on page 124 of:<br/>
     *
     * @Article{ 923275, title = "Nonparametric Genetic Clustering: Comparison of Validity
     *           Indices", author = "Ujjwal Maulik and Sanghamitra Bandyopadhyay", journal =
     *           "IEEE Transactions on Systems, Man, and Cybernetics, Part C: Applications
     *           and Reviews", pages = "120--125", volume = "31", number = "1", month = feb,
     *           year = "2001", issn = "1094-6977" }
     * @return the average intra-cluster distance for all clusters
     */
    public double calculateIntraClusterDistance() {
        double intraClusterDistance = 0.0;

        for (Cluster<Vector> cluster : this.significantClusters) {
            Vector center = this.clusterCenterStrategy.getCenter(cluster);

            for (Pattern<Vector> pattern : cluster) {
                intraClusterDistance += this.problem.calculateDistance(pattern.getData(), center);
            }
        }
        return intraClusterDistance;
    }

    /**
     * Calculate the average intra-cluster distance. In other words, the average of the
     * distances between all patterns of all clusters and their associated centroids. The
     * calculation is specified in Section 3.2 on page 2 of:<br/>
     *
     * @Unpublished{ cal99, title = "Determination of Number of Clusters in K-Means
     *               Clustering and Application in Colour Image Segmentation", author =
     *               "Siddheswar Ray and Rose H. Turi", year = "2000", month = jul }
     * @return the average intra-cluster distance for all clusters.
     */
    public double calculateAverageIntraClusterDistance() {
        return calculateIntraClusterDistance() / ((StaticDataSetBuilder) this.problem.getDataSetBuilder()).getNumberOfPatterns();
    }

    @Override
    public Double getMinimum() {
        return 0.0;
    }

    @Override
    public Double getMaximum() {
        return Double.MAX_VALUE;
    }

    protected Double worstFitness() {
        return getMaximum();
    }
}
