/*
 * ClusteringFitnessFunction.java
 * 
 * Created on July 18, 2007
 *
 * Copyright (C) 2003 - 2007
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
package net.sourceforge.cilib.functions.continuous;

import java.util.ArrayList;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.InitialisationException;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.problem.dataset.ClusterableDataSet;
import net.sourceforge.cilib.problem.dataset.ClusterableDataSet.Pattern;
import net.sourceforge.cilib.type.types.container.Vector;

import org.apache.log4j.Logger;

/**
 * This abstract class defines member variables and member functions that can be used by subclasses
 * to calculate the fitness of a particular clustering in their respective evaluate methods.
 * @author Theuns Cloete
 */
public abstract class ClusteringFitnessFunction extends ContinuousFunction {
	private static Logger log = Logger.getLogger(ClusteringFitnessFunction.class);
	protected ClusterableDataSet dataset = null;
	protected ArrayList<ArrayList<Pattern>> arrangedClusters = null;
	protected ArrayList<Vector> arrangedCentroids = null;
	protected int clustersFormed = 0;

	/**
	 * This constructor cannot be called directly since this is an abstract class. Subclasses call
	 * this constructor, from their constructor(s) via the super() command. The default domain is set
	 * to "R(-5.0, 5.0)^2" but it will probably be overwritten in the XML file.
	 */
	public ClusteringFitnessFunction() {
		setDomain("R(-5.0, 5.0)^2");
	}

	/**
	 * This method is responsible for various things before the fitness can be returned:
	 * <ol>
	 * <li>Reset the dataset.
	 * <li>Arrange the patterns in the dataset to belong to its closest centroid. We don't care how
	 * this is done, since it is handled by the abstract <tt>arrangeClustersAndCentroids</tt>
	 * method defined in {@link net.sourceforge.cilib.problem.dataset.ClusterableDataSet}. We also
	 * assume that this method removes empty clusters and their associated centroids from the
	 * <i>arranged lists</i></li>
	 * <li>Sets the <tt>arrangedClusters</tt> member variable which is available to sub-classes.</li>
	 * <li>Sets the <tt>arrangedCentroids</tt> member variable which is available to sub-classes.</li>
	 * <li>Calculate the fitness using the given centroids {@linkplain Vector}. We don't care how
	 * this is done, since it is handled by the abstraction (polymorphism) created by the hierarchy
	 * of this class. This is achieved via the abstact <tt>calculateFitness</tt> method</li>
	 * <li>Validate the fitness, i.e. make sure the fitness is positive (>= 0.0).</li>
	 * </ol>
	 * Steps 2 - 4 have to be performed before the fitness is calculated, using the given
	 * <tt>centroids</tt> {@linkplain Vector}, in step 5.
	 * @param centroids The {@linkplain Vector} representing the centroid vectors
	 * @return the fitness that has been calculated
	 */
	@Override
	public double evaluate(Vector centroids) {
		if (dataset == null)
			resetDataSet();

		dataset.arrangeClustersAndCentroids(centroids);
		arrangedClusters = dataset.getArrangedClusters();
		arrangedCentroids = dataset.getArrangedCentroids();
		clustersFormed = arrangedClusters.size();

		return validateFitness(calculateFitness());
	}

	public abstract double calculateFitness();

	public ArrayList<ArrayList<Pattern>> getArrangedClusters() {
		return arrangedClusters;
	}

	public ArrayList<Vector> getArrangedCentroids() {
		return arrangedCentroids;
	}

	/**
	 * Calculate the Quantisation Error as explained in Section 4.1.1 on pages 104 & 105 of:<br/>
	 * @PhDThesis{ omran2004thesis, title = "Particle Swarm Optimization Methods for Pattern
	 *             Recognition and Image Processing", author = "Mahamed G.H. Omran", institution =
	 *             "University Of Pretoria", school = "Computer Science", year = "2004", month = nov,
	 *             address = "Pretoria, South Africa", note = "Supervisor: A. P. Engelbrecht", }
	 * @return the Quantisation Error of the particular clustering
	 */
	public double calculateQuantisationError() {
		double quantisationError = 0.0;

		for (int i = 0; i < clustersFormed; i++) {
			double averageDistance = 0.0;
			ArrayList<Pattern> cluster = arrangedClusters.get(i);
			Vector centroid = arrangedCentroids.get(i);

			for (Pattern pattern : cluster) {
				averageDistance += dataset.calculateDistance(pattern.data, centroid);
			}
			averageDistance /= cluster.size();
			quantisationError += averageDistance;
		}
		quantisationError /= clustersFormed;
		return quantisationError;
	}

	/**
	 * Calculate the Maximum Average Distance between the patterns in the dataset and the centroids
	 * learned so far; see Section 4.1.1 at the bottom of page 105 of:<br/>
	 * @PhDThesis{ omran2004thesis, title = "Particle Swarm Optimization Methods for Pattern
	 *             Recognition and Image Processing", author = "Mahamed G.H. Omran", institution =
	 *             "University Of Pretoria", school = "Computer Science", year = "2004", month = nov,
	 *             address = "Pretoria, South Africa", note = "Supervisor: A. P. Engelbrecht", }
	 * @return the maximum average distance between the patterns of a cluster and their associated
	 *         centroid
	 */
	public double calculateMaximumAverageDistance() {
		double maximumAverageDistance = 0.0;

		for (int i = 0; i < clustersFormed; i++) {
			double averageDistance = 0.0;
			ArrayList<Pattern> cluster = arrangedClusters.get(i);
			Vector centroid = arrangedCentroids.get(i);

			for (Pattern pattern : cluster) {
				averageDistance += dataset.calculateDistance(pattern.data, centroid);
			}
			averageDistance /= cluster.size();
			maximumAverageDistance = Math.max(maximumAverageDistance, averageDistance);
		}
		return maximumAverageDistance;
	}

	/**
	 * Calculate the shortest distance between two clusters. In other words, the shortest distance
	 * between the centroids of any two clusters.
	 * @return the minimum inter-cluster distance
	 */
	public double calculateMinimumInterClusterDistance() {
		if (clustersFormed < 2)
			return -Double.MAX_VALUE;

		double minimumInterClusterDistance = Double.MAX_VALUE;

		for (int i = 0; i < clustersFormed - 1; i++) {
			Vector leftCentroid = arrangedCentroids.get(i);
			for (int j = i + 1; j < clustersFormed; j++) {
				Vector rightCentroid = arrangedCentroids.get(j);
				minimumInterClusterDistance = Math.min(minimumInterClusterDistance, dataset.calculateDistance(leftCentroid, rightCentroid));
			}
		}
		return minimumInterClusterDistance;
	}

	/**
	 * Calculate the longest distance between two clusters. In other words, the longest distance
	 * between the centroids of any two clusters.
	 * @return the maximum inter-cluster distance
	 */
	public double calculateMaximumInterClusterDistance() {
		if (clustersFormed < 2)
			return Double.MAX_VALUE;

		double maximumInterClusterDistance = -Double.MAX_VALUE;

		for (int i = 0; i < clustersFormed - 1; i++) {
			Vector leftCentroid = arrangedCentroids.get(i);
			for (int j = i + 1; j < clustersFormed; j++) {
				Vector rightCentroid = arrangedCentroids.get(j);
				maximumInterClusterDistance = Math.max(maximumInterClusterDistance, dataset.calculateDistance(leftCentroid, rightCentroid));
			}
		}
		return maximumInterClusterDistance;
	}

	/**
	 * Calculate the minimum distance between two clusters (sets) as shown in Equation 20 of:<br/>
	 * @Article{ 678624, title = "Some New Indexes of Cluster Validity", author = "James C. Bezdek
	 *           and Nikhil R. Pal", journal = "IEEE Transactions on Systems, Man, and Cybernetics,
	 *           Part B: Cybernetics", pages = "301--315", volume = "28", number = "3", month = jun,
	 *           year = "1998", issn = "1083-4419" }
	 * @param i the index of the LHS cluster
	 * @param j the index of the RHS cluster
	 * @return the shortest distance between the patterns of two clusters (sets)
	 */
	public double calculateMinimumSetDistance(int i, int j) {
		double distance = Double.MAX_VALUE;

		for (Pattern leftPattern : arrangedClusters.get(i)) {
			for (Pattern rightPattern : arrangedClusters.get(j)) {
				distance = Math.min(distance, dataset.calculateDistance(leftPattern.index, rightPattern.index));
			}
		}
		return distance;
	}

	/**
	 * Calculate the maximum distance between two clusters (sets) as shown in Equation 21 of:<br/>
	 * @Article{ 678624, title = "Some New Indexes of Cluster Validity", author = "James C. Bezdek
	 *           and Nikhil R. Pal", journal = "IEEE Transactions on Systems, Man, and Cybernetics,
	 *           Part B: Cybernetics", pages = "301--315", volume = "28", number = "3", month = jun,
	 *           year = "1998", issn = "1083-4419" }
	 * @param i the index of the LHS cluster
	 * @param j the index of the RHS cluster
	 * @return the longest distance between the patterns of two clusters (sets)
	 */
	public double calculateMaximumSetDistance(int i, int j) {
		double distance = -Double.MAX_VALUE;

		for (Pattern leftPattern : arrangedClusters.get(i)) {
			for (Pattern rightPattern : arrangedClusters.get(j)) {
				distance = Math.max(distance, dataset.calculateDistance(leftPattern.index, rightPattern.index));
			}
		}
		return distance;
	}

	/**
	 * Calculate the average distance between two clusters (sets) as shown in Equation 22 of:<br/>
	 * @Article{ 678624, title = "Some New Indexes of Cluster Validity", author = "James C. Bezdek
	 *           and Nikhil R. Pal", journal = "IEEE Transactions on Systems, Man, and Cybernetics,
	 *           Part B: Cybernetics", pages = "301--315", volume = "28", number = "3", month = jun,
	 *           year = "1998", issn = "1083-4419" }
	 * @param i the index of the LHS cluster
	 * @param j the index of the RHS cluster
	 * @return the average distance between the patterns of two clusters (sets)
	 */
	public double calculateAverageSetDistance(int i, int j) {
		double distance = 0.0;

		for (Pattern leftPattern : arrangedClusters.get(i)) {
			for (Pattern rightPattern : arrangedClusters.get(j)) {
				distance += dataset.calculateDistance(leftPattern.index, rightPattern.index);
			}
		}
		return distance / (arrangedClusters.get(i).size() * arrangedClusters.get(j).size());
	}

	/**
	 * Calculate the diameter of the given cluster, i.e. the distance between the two patterns (in
	 * the dataset) that are furthest apart. There exists numerous references for this calculation.
	 * @param k the index of the cluster for which the diameter should be calculated
	 * @return the diameter of the given cluster
	 */
	public double calculateClusterDiameter(int k) {
		double diameter = 0.0;
		ArrayList<Pattern> cluster = arrangedClusters.get(k);
		int numberOfPatterns = cluster.size();

		for (int i = 0; i < numberOfPatterns - 1; i++) {
			int leftPattern = cluster.get(i).index;
			for (int j = i + 1; j < numberOfPatterns; j++) {
				int rightPattern = cluster.get(j).index;
				diameter = Math.max(diameter, dataset.calculateDistance(leftPattern, rightPattern));
			}
		}
		return diameter;
	}

	/**
	 * Calculate the intra-cluster distance. In other words, the average of the distances between all
	 * patterns of all clusters and their associated centroids. The calculation is specified in
	 * Section 3.2 on page 2 of:<br/>
	 * @Unpublished{ cal99, title = "Determination of Number of Clusters in K-Means Clustering and
	 *               Application in Colour Image Segmentation", author = "Siddheswar Ray and Rose H.
	 *               Turi", year = "2000", month = jul }
	 * @return the inter-cluster distance for all clusters
	 */
	public double calculateIntraClusterDistance() {
		double intraClusterDistance = 0.0;

		for (int k = 0; k < clustersFormed; k++) {
			ArrayList<Pattern> cluster = arrangedClusters.get(k);
			Vector centroid = arrangedCentroids.get(k);

			for (Pattern pattern : cluster) {
				intraClusterDistance += dataset.calculateDistance(pattern.data, centroid);
			}
		}
		return intraClusterDistance / dataset.getNumberOfPatterns();
	}

	public ClusterableDataSet getDataSet() {
		return dataset;
	}

	public void setDataSet(ClusterableDataSet ds) {
		dataset = ds;
	}

	/**
	 * When starting an Algorithm, the Function that is used to optimise the OptimisationProblem
	 * does not know about the problem it was assigned to and therefore it does not know about the DataSet
	 * that contains the Patterns for which a clustering should be found. This method finds
	 * the current executing Algorithm via the <tt>Algorithm.get()</tt> construct. Then we find the
	 * OptimisationProblem and then we find the DataSetBuilder. This method will hopefully only
	 * be called when the <tt>dataset</tt> member variable is <tt>null</tt>.
	 */
	private void resetDataSet() {
		// get the Algorithm we are working with
		Algorithm algorithm = Algorithm.get();

		if (algorithm != null) {
			// get the ClusterableDataSet we are working with
			dataset = (ClusterableDataSet) algorithm.getOptimisationProblem().getDataSetBuilder();
		}
		else {
			throw new InitialisationException("Algorithm not initialised yet.");
		}
	}

	@Override
	public Object getMinimum() {
		return new Double(0.0);
	}

	/**
	 * This method logs the cases when the fitness is less than zero. We do not want the Parametric
	 * fitness to be less than zero. Fitnesses drop below zero when the centroids are outside the
	 * given domain which causes {@linkplain zMax} to be too small to compensate.
	 * @param fitness the fitness value that will be validated
	 * @return the fitness
	 * TODO: Should this function always return the fitness?
	 * TODO: Or should it return NaN when the fitness drops below 0.0?
	 * TODO: Or should we throw an exception?
	 */
	protected double validateFitness(double fitness) {
		if (fitness < 0.0) {
			log.fatal(this.getClass().getSimpleName() + " fitness < 0.0 : " + fitness);
			log.fatal("Number of clusters formed = " + clustersFormed);
		}
		System.gc();
		return fitness;
	}
}
