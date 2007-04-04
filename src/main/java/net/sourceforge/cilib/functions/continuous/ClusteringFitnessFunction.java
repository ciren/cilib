package net.sourceforge.cilib.functions.continuous;

import java.util.ArrayList;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.InitialisationException;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.problem.dataset.ClusterableDataSet;
import net.sourceforge.cilib.problem.dataset.ClusterableDataSet.Pattern;
import net.sourceforge.cilib.type.types.Vector;

/**
 * This abstract class defines member variables and member functions that can be used by subclasses to calculate the
 * fitness of a particular clustering in their respective evaluate methods.<br/>
 * References:<br/>
 * <p>
 * Mahamed G.H. Omran, "Particle Swarm Optimization Methods for Pattern Recognition and Image Processing",
 * University Of Pretoria, Faculty of Engineering, Built Environment and Information Technology, 251. November 2004
 * </p>
 * Each method below specifies on what page(s) in the above thesis the technique can be found.
 * @author Theuns Cloete
 */
public abstract class ClusteringFitnessFunction extends ContinuousFunction {
	/**Stores the Maximum Average Distance between centroids and patterns*/
	protected double maximumAverageDistance = 0.0;
	/**Stores the Minimum Distance between centroid pairs*/
	protected double interClusterDistance = 0.0;
	protected double intraClusterDistance = 0.0;
	/**Stores the Quantisation Error*/
	protected double quantisationError = 0.0;
	protected ClusterableDataSet dataset = null;

	/**
	 * This constructor cannot be called directly since this is an abstract class. Subclasses call this constructor, from
	 * their constructor(s) via the super() command. The default domain is set to "R(-5.0, 5.0)^2" but it will probably be
	 * overwritten in the XML file. 
	 */
	public ClusteringFitnessFunction() {
		setDomain("R(-5.0, 5.0)^2");
	}

	/**
	 * Calculate the Quantisation Error as explained in Section 4.1.1 on pages 104 & 105 of "Particle Swarm Optimization
	 * Methods for Pattern Recognition and Image Processing" by Mahamed G.H. Omran.
	 * @param dataset The dataset containing the patterns that should be classified 
	 * @param centroids The vector representing the centroid vectors
	 */
	public double calculateQuantisationError(Vector centroids)	{
		int k = 0;
		double averageDistance = 0.0;
		quantisationError = 0.0;
		for(ArrayList<Pattern> cluster : dataset.arrangedClusters()) {
			int numberOfPatterns = cluster.size();
			if(numberOfPatterns > 0) {
				Vector centroid = dataset.getSubCentroid(centroids, k);
				averageDistance = 0.0;
				for(Pattern pattern : cluster) {
					averageDistance += dataset.calculateDistance(pattern.data, centroid);
				}
				averageDistance /= numberOfPatterns;
				quantisationError += averageDistance;
			}
			++k;
		}
		quantisationError /= dataset.getNumberOfClusters();
		return quantisationError;
	}

	/**
	 * Calculate the Maximum Average Distance between the patterns in the dataset and the centroids learned so far; see
	 * Section 4.1.1 at the bottom of page 105 of "Particle Swarm Optimization Methods for Pattern Recognition
	 * and Image Processing" by Mahamed G.H. Omran. The Maximum Average Distance is needed for the calculation of the
	 * parametric clustering fitness function {@link net.sourceforge.cilib.functions.continuous.ParametricClusteringFunction}
	 * @param dataset The dataset containing the patterns that should be classified 
	 * @param centroids The vector representing the centroid vectors
	 */
	public double calculateMaximumAverageDistanceBetweenPatternsAndCentroids(Vector centroids)	{
		int k = 0;
		double averageDistance = 0.0;
		maximumAverageDistance = 0.0;
		for(ArrayList<Pattern> cluster : dataset.arrangedClusters()) {
			int numberOfPatterns = cluster.size();
			if(numberOfPatterns > 0) {
				Vector centroid = dataset.getSubCentroid(centroids, k);
				averageDistance = 0.0;
				for(Pattern pattern : cluster) {
					averageDistance += dataset.calculateDistance(pattern.data, centroid);
				}
				averageDistance /= numberOfPatterns;
				if(averageDistance > maximumAverageDistance) {
					maximumAverageDistance = averageDistance;
				}
			}
			++k;
		}
		return maximumAverageDistance;
	}

	/**
	 * Calculate BOTH the Maximum Average Distance between the patterns in the dataset and the centroids learned so far AND
	 * the Quantisation Error; see Section 4.1.1 on pages 104 & 105 of "Particle Swarm Optimization Methods for Pattern
	 * Recognition and Image Processing" by Mahamed G.H. Omran. The Maximum Average Distance and the Quantisation Error are
	 * both needed for the calculation of the improved parametric clustering fitness function
	 * {@link net.sourceforge.cilib.functions.continuous.ParametricWithQuantisationErrorFunction} as well as the non-parametric
	 * clustering fitness function {@link net.sourceforge.cilib.functions.continuous.NonParametricClusteringFunction}
	 * @param dataset The dataset containing the patterns that should be classified 
	 * @param centroids The vector representing the centroid vectors
	 */
	public void calculateQuantisationErrorAndMaximumAverageDistanceBetweenPatternsAndCentroids(Vector centroids)	{
		int k = 0;
		double averageDistance = 0.0;
		quantisationError = 0.0;
		maximumAverageDistance = 0.0;
		for(ArrayList<Pattern> cluster : dataset.arrangedClusters()) {
			int numberOfPatterns = cluster.size();
			if(numberOfPatterns > 0) {
				Vector centroid = dataset.getSubCentroid(centroids, k);
				averageDistance = 0.0;
				for(Pattern pattern : cluster) {
					averageDistance += dataset.calculateDistance(pattern.data, centroid);
				}
				averageDistance /= numberOfPatterns;
				quantisationError += averageDistance;
				if(averageDistance > maximumAverageDistance) {
					maximumAverageDistance = averageDistance;
				}
			}
			++k;
		}
		quantisationError /= dataset.getNumberOfClusters();
	}

	/**
	 * Calculate the Minimum Distance between centroid pairs (in other words, calculate the Minimum Distance from all
	 * centroids to all other centroids, also known as the Inter Cluster Distance) that have been learned so far; see Section 4.1.1 at the top of page 106 of "Particle
	 * Swarm Optimization Methods for Pattern Recognition and Image Processing" by Mahamed G.H. Omran. The Minimum Centoid
	 * Distance is needed for the calculation of the parametric clustering fitness function
	 * {@link net.sourceforge.cilib.functions.continuous.ParametricClusteringFunction}
	 * @param dataset The dataset containing the patterns that should be classified 
	 * @param centroids The vector representing the centroid vectors
	 */
	public double calculateInterClusterDistance(Vector centroids) {
		Vector centroid1 = null, centroid2 = null;
		double distance = 0.0;
		interClusterDistance = Double.MAX_VALUE;
		int numberOfClusters = dataset.getNumberOfClusters();
		for(int i = 0; i < numberOfClusters - 1; i++) {
			centroid1 = dataset.getSubCentroid(centroids, i);
			for(int j = i + 1; j < numberOfClusters; j++) {
				centroid2 = dataset.getSubCentroid(centroids, j);
				distance = dataset.calculateDistance(centroid1, centroid2);
				if(distance < interClusterDistance) {
					interClusterDistance = distance;
				}
			}
		}
		return interClusterDistance;
	}

	public double calculateClusterDissimilarity(ArrayList<Pattern> lhs, ArrayList<Pattern> rhs) {
		double dissimilarity = Double.MAX_VALUE, distance = 0.0;
		for(Pattern patternA : lhs) {
			for(Pattern patternB : rhs) {
				distance = dataset.calculateDistance(patternA.index, patternB.index);
				if(distance < dissimilarity) {
					dissimilarity = distance;
				}
			}
		}
		return dissimilarity;
	}

	public double calculateClusterDiameter(ArrayList<Pattern> cluster) {
		double diameter = 0.0, distance = 0.0;
		int patterns = cluster.size();
		for(int i = 0; i < patterns - 1; i++) {
			for(int j = i + 1; j < patterns; j++) {
				distance = dataset.calculateDistance(cluster.get(i).index, cluster.get(j).index);
				if(distance > diameter) {
					diameter = distance;
				}
			}
		}
		return diameter;
	}

	public double calculateIntraClusterDistance(Vector centroids) {
		int k = 0;
		intraClusterDistance = 0.0;
		for(ArrayList<Pattern> cluster : dataset.arrangedClusters()) {
			int numberOfPatterns = cluster.size();
			if(numberOfPatterns > 0) {
				Vector centroid = dataset.getSubCentroid(centroids, k);
				for(Pattern pattern : cluster) {
					intraClusterDistance += dataset.calculateDistance(pattern.data, centroid);
				}
			}
			++k;
		}
		intraClusterDistance /= dataset.getNumberOfPatterns();
		return intraClusterDistance;
	}

	public ClusterableDataSet getDataSet() {
		return dataset;
	}

	public void setDataSet(ClusterableDataSet ds) {
		dataset = ds;
	}

	public void resetDataSet() {
		//get the Algorithm we are working with
		Algorithm algorithm = Algorithm.get();
		if(algorithm != null)
			//get the ClusterableDataSet we are working with
			dataset = (ClusterableDataSet)algorithm.getOptimisationProblem().getDataSetBuilder();
		else
			throw new InitialisationException("Algorithm hasn't been initialised yet.");
	}

	@Override
	public Object getMinimum() {
		return new Double(0.0);
	}
}
