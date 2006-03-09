package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.PopulationBasedAlgorithm;
import net.sourceforge.cilib.functions.ContinuousFunction;
import net.sourceforge.cilib.problem.dataset.AssociatedPairDataSetBuilder;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.MixedVector;
import net.sourceforge.cilib.type.types.Vector;
import net.sourceforge.cilib.util.DistanceMeasure;
import net.sourceforge.cilib.util.EuclideanDistanceMeasure;

public class QuantisationErrorFunction extends ContinuousFunction {

	int clusters;
	DistanceMeasure distanceMeasure;
	
	public QuantisationErrorFunction() {
		//the last dimension (^) specifies in how many clusters (or classes) the patterns will be clustered into 
		setDomain("R(-30.0, 30.0)^10");
		clusters = 10;
		distanceMeasure = new EuclideanDistanceMeasure();
	}

	public double evaluate(Vector particle) {
		PopulationBasedAlgorithm algorithm = (PopulationBasedAlgorithm) Algorithm.get();
		AssociatedPairDataSetBuilder dataset = (AssociatedPairDataSetBuilder)algorithm.getOptimisationProblem().getDataSetBuilder();

		//update pattern assignations (determine in which cluster a pattern belongs based on its closest centroid)
		//run through the entire particle vector (i.e. all the clusters)
		for(int i = 0; i < clusters; i++) {
			double minimum = Double.MAX_VALUE;
			//determine which part of the particle (i.e. which centroid) the patterns should be compared to
			Vector centroid = centroidFromParticle(particle, i);
			for(int j = 0; j < dataset.getNumberOfPatterns(); j++) {
//				System.out.println("centroid size = " + centroid.size() + "; pattern size = " + dataset.getPattern(j).size());
				double distance = distanceMeasure.distance(centroid, dataset.getPattern(j));
				if(distance < minimum) {
					minimum = distance;
					//assign pattern j to cluster i
					dataset.setKey(j, new Int(i));
				}
			}
		}
		
		//build up a list (Vector) of all the patterns that belong to a certain cluster
		//only the patterns that belong to a certain cluster must be used in quantisation error calculation
		Vector patternsInCluster = new MixedVector();
		double quantisationError = 0.0;
		for(int i = 0; i < clusters; i++) {
			for(int j = 0; j < dataset.getNumberOfPatterns(); j++) {
				if(dataset.getKey(j).getInt() == i) {
					patternsInCluster.add(dataset.getPattern(j));
				}
			}
			//no need to do the following if no patterns belong to this cluster (save some time)
			if(patternsInCluster.size() > 0) {
				//determine which part of the particle (i.e. which centroid) the patterns belonging to this cluster should be compared to
				Vector centroid = centroidFromParticle(particle, i);
				double averageDistance = 0.0;
				for(int j = 0; j < patternsInCluster.size(); j++) {
					averageDistance += distanceMeasure.distance(centroid, (Vector)patternsInCluster.get(j));
				}
//				averageDistance /=  patternsInCluster.size();
				quantisationError += averageDistance;
				quantisationError /= patternsInCluster.size();
				patternsInCluster.clear();
			}
		}
		quantisationError /= clusters;
//		System.out.println(quantisationError);
		return quantisationError;
	}
	
	/**
	 * Set the number of clusters that exist in the dataset that will be optimised. 
	 */
	public void setClusters(int c) {
		if(c < 1)
			throw new IllegalArgumentException("The number of clusters cannot be less than or equal to 0");
		clusters = c;
	}
	
	/**
	 * Set the type of DistanceMeasure that should be used for determining the Quantisation Error.
	 * @param distanceMeasure any class that inherits from DistanceMeasure can be used
	 */
	public void setDistanceMeasure(DistanceMeasure distanceMeasure) {
		this.distanceMeasure = distanceMeasure;
	}
	
	/**
	 * Determine which part of the particle (i.e. which centroid) the patterns should be compared to
	 * @param particle the current particle that contains the possible cluster centroids
	 * @param index the position at which the centroid should be started
	 * @return a vector representing the specific centroid in the given particle
	 */
	private Vector centroidFromParticle(Vector particle, int cluster) {
		Vector centroid = new MixedVector();
		int dimension = this.getDomainRegistry().getDimension() / clusters;
		for(int i = cluster * dimension; i < (cluster * dimension) + dimension ; i++) {
			centroid.add(particle.get(i));
		}
		return centroid;
	}
}
