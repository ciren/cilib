package net.sourceforge.cilib.functions.continuous;

import java.io.Serializable;

import net.sourceforge.cilib.type.types.container.Vector;

/**
 * A strategy to determine what is meant by the <i>center of a cluster</i>. Sometimes,
 * <i>cluster center</i> refers to a cluster's {@link ClusterCentroidStrategy centroid} and other times it
 * refers to a cluster's {@link ClusterMeanStrategy mean}.
 * @author Theuns Cloete
 */
public abstract class ClusterCenterStrategy implements Serializable {
	/** So that we can get hold of the arranged clusters and centroids */
	protected ClusteringFitnessFunction clusteringFitnessFunction = null;

	public ClusterCenterStrategy() {
	}

	public ClusterCenterStrategy(ClusteringFitnessFunction cff) {
		clusteringFitnessFunction = cff;
	}

	/**
	 * Sub-classes should implement this method to return the desired center of cluster i.
	 * @param i The integer representing the cluster for which a center should be returned
	 * @return a {@link Vector} representing the center of cluster i
	 */
	public abstract Vector getCenter(int i);

	public void setClusteringFitnessFucntion(ClusteringFitnessFunction cff) {
		clusteringFitnessFunction = cff;
	}
}
