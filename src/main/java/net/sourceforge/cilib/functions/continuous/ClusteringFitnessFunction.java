package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.population.PopulationBasedAlgorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.problem.dataset.ClusterableDataSet;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Type;
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
	protected double minimumCentroidDistance = 0.0;
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
		setDataSet(null);
	}

	/**
	 * Calculate the Quantisation Error as explained in Section 4.1.1 on pages 104 & 105 of "Particle Swarm Optimization
	 * Methods for Pattern Recognition and Image Processing" by Mahamed G.H. Omran.
	 * @param dataset The dataset containing the patterns that should be classified 
	 * @param centroids The vector representing the centroid vectors
	 */
	public void calculateQuantisationError(Vector centroids)	{
		//we need a Vector to hold all the patterns that belong to a certain cluster
		Vector patternsInCluster = null;
		Vector centroid = null;
		//reset the quantisation error to be 0.0
		quantisationError = 0.0;
		int numberOfClusters = dataset.getNumberOfClusters();
		double averageDistance = 0.0;
		//run through all the different clusters
		for(int i = 0; i < numberOfClusters; i++) {
			//get a Vector with all the patterns belonging to cluster i
			patternsInCluster = dataset.patternsInCluster(new Int(i));
			//we don't have to do the following if there are no patterns that belong to this cluster (save some time)
			if(patternsInCluster.size() > 0) {
				//extract the i'th centroid from the given centroids Vector that contains all the centroids
				centroid = dataset.getSubCentroid(centroids, i);
				//we have to calculate the average distance between the centroid and all the patterns belonging to the centroid
				averageDistance = 0.0;
				//run through all the patterns that belong to cluster i
				for(Type pattern : patternsInCluster) {
					//calculate the sum of the distances between the centroid and all patterns j 
					averageDistance += dataset.getDistanceMeasure().distance((Vector)pattern, centroid);
				}
				//devide the sum by the number of patterns belonging to the centroid
				averageDistance /= patternsInCluster.size();
				//calculate the sum of all the average distances between a centroid and the patterns that belong to that centroid 
				quantisationError += averageDistance;
				//clear out the patterns because we are going to work with another cluster in the next iteration
				patternsInCluster.clear();
			}
		}
		//devide the sum by the number of clusters
		quantisationError /= numberOfClusters;
	}

	/**
	 * Calculate the Maximum Average Distance between the patterns in the dataset and the centroids learned so far; see
	 * Section 4.1.1 at the bottom of page 105 of "Particle Swarm Optimization Methods for Pattern Recognition
	 * and Image Processing" by Mahamed G.H. Omran. The Maximum Average Distance is needed for the calculation of the
	 * parametric clustering fitness function {@link net.sourceforge.cilib.functions.continuous.ParametricClusteringFunction}
	 * @param dataset The dataset containing the patterns that should be classified 
	 * @param centroids The vector representing the centroid vectors
	 */
	public void calculateMaximumAverageDistanceBetweenPatternsAndCentroids(Vector centroids)	{
		//we need a Vector to hold all the patterns that belong to a certain cluster
		Vector patternsInCluster = null;
		Vector centroid = null;
		//reset the maximum average distance to be the smallest double possible
		maximumAverageDistance = Double.MIN_VALUE;
		int numberOfClusters = dataset.getNumberOfClusters();
		double averageDistance = 0.0;
		//run through all the different clusters
		for(int i = 0; i < numberOfClusters; i++) {
			//get a Vector with all the patterns belonging to cluster i
			patternsInCluster = dataset.patternsInCluster(new Int(i));
			//we don't have to do the following if there are no patterns that belong to this cluster (save some time)
			if(patternsInCluster.size() > 0) {
				//extract the i'th centroid from the given centroids Vector that contains all the centroids
				centroid = dataset.getSubCentroid(centroids, i);
				//we have to calculate the average distance between the centroid and all the patterns belonging to the centroid
				averageDistance = 0.0;
				//run through all the patterns that belong to cluster i
				for(Type pattern : patternsInCluster) {
					//calculate the sum of the distances between the centroid and all patterns j
					averageDistance += dataset.getDistanceMeasure().distance((Vector)pattern, centroid);
				}
				//devide the sum by the number of patterns belonging to the centroid
				averageDistance /= patternsInCluster.size();
				//remember what the maximum average distance is so far
				if(averageDistance > maximumAverageDistance) {
					maximumAverageDistance = averageDistance;
				}
				//clear out the patterns because we are going to work with another cluster in the next iteration
				patternsInCluster.clear();
			}
		}
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
		//we need a Vector to hold all the patterns that belong to a certain cluster
		Vector patternsInCluster = null;
		Vector centroid = null;
		//reset the quantisation error to be 0.0
		quantisationError = 0.0;
		//reset the maximum average distance to be the smallest double possible
		maximumAverageDistance = Double.MIN_VALUE;
		int numberOfClusters = dataset.getNumberOfClusters();
		double averageDistance = 0.0;
		//run through all the different clusters
		for(int i = 0; i < numberOfClusters; i++) {
			//get a Vector with all the patterns belonging to cluster i
			patternsInCluster = dataset.patternsInCluster(new Int(i));
			//we don't have to do the following if there are no patterns that belong to this cluster (save some time)
			if(patternsInCluster.size() > 0) {
				//extract the i'th centroid from the given centroids Vector that contains all the centroids
				centroid = dataset.getSubCentroid(centroids, i);
				//we have to calculate the average distance between the centroid and all the patterns belonging to the centroid
				averageDistance = 0.0;
				//run through all the patterns that belong to cluster i
				for(Type pattern : patternsInCluster) {
					//calculate the sum of the distances between the centroid and all patterns j
					averageDistance += dataset.getDistanceMeasure().distance(centroid, (Vector)pattern);
				}
				//devide the sum by the number of patterns belonging to the centroid
				averageDistance /=  patternsInCluster.size();
				//remember what the maximum average distance is so far
				if(averageDistance > maximumAverageDistance) {
					maximumAverageDistance = averageDistance;
				}
				//calculate the sum of all the average distances between a centroid and the patterns that belong to that centroid
				quantisationError += averageDistance;
				//clear out the patterns because we are going to work with another cluster in the next iteration
				patternsInCluster.clear();
			}
		}
		//devide the sum by the number of clusters
		quantisationError /= numberOfClusters;
	}

	/**
	 * Calculate the Minimum Distance between centroid pairs (in other words, calculate the Minimum Distance from all
	 * centroids to all other centroids) that have been learned so far; see Section 4.1.1 at the top of page 106 of "Particle
	 * Swarm Optimization Methods for Pattern Recognition and Image Processing" by Mahamed G.H. Omran. The Minimum Centoid
	 * Distance is needed for the calculation of the parametric clustering fitness function
	 * {@link net.sourceforge.cilib.functions.continuous.ParametricClusteringFunction}
	 * @param dataset The dataset containing the patterns that should be classified 
	 * @param centroids The vector representing the centroid vectors
	 */
	public void calculateMinimumDistanceBetweenCentroidPairs(Vector centroids) {
		Vector centroid1 = null, centroid2 = null;
		//reset the minimum distance between centroid pairs to be the largets double possible
		minimumCentroidDistance = Double.MAX_VALUE;
		double distance = 0.0;
		int numberOfClusters = dataset.getNumberOfClusters();
		//run through all the different clusters, starting with the first and stopping with the second last cluster 
		for(int i = 0; i < numberOfClusters - 1; i++) {
			//extract the i'th centroid from the given centroids Vector that contains all the centroids
			centroid1 = dataset.getSubCentroid(centroids, i);
			//run through all the different clusters, starting to the right of i and stopping with the last cluster
			for(int j = i + 1; j < numberOfClusters; j++) {
				//extract the j'th centroid from the given centroids Vector that contains all the centroids
				centroid2 = dataset.getSubCentroid(centroids, j);
				//calculate the distane between the two centroids
				distance = dataset.getDistanceMeasure().distance(centroid1, centroid2);
				//remember what the minimum distance is so far
				if(distance < minimumCentroidDistance) {
					minimumCentroidDistance = distance;
				}
			}
		}
	}

	public ClusterableDataSet getDataSet() {
		return dataset;
	}

	public void setDataSet(ClusterableDataSet ds) {
		dataset = ds;
		if(dataset == null && Algorithm.get() != null)
		{
			//get the Algorithm we are working with
			PopulationBasedAlgorithm algorithm = (PopulationBasedAlgorithm) Algorithm.get();
			//get the ClusterableDataSet we are working with
			dataset = (ClusterableDataSet)(algorithm.getOptimisationProblem().getDataSetBuilder());
		}
	}
}
