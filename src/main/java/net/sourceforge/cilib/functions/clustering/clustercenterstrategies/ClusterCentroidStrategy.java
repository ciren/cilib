package net.sourceforge.cilib.functions.clustering.clustercenterstrategies;

import net.sourceforge.cilib.functions.clustering.ClusteringFitnessFunction;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * The <i>center of a cluster</i> is interpreted as the <i>centroid of a cluster</i>.
 * @author Theuns Cloete
 */
public class ClusterCentroidStrategy extends ClusterCenterStrategy {
	private static final long serialVersionUID = -7831635507079248268L;

	public ClusterCentroidStrategy(ClusteringFitnessFunction cff) {
		super(cff);
	}

	/**
	 * In this case, we are interested in the centroid of the cluster.
	 * @param i The integer representing the cluster for which the centroid should be returned 
	 * @return the centroid of cluster i
	 */
	@Override
	public Vector getCenter(int i) {
		return clusteringFitnessFunction.getArrangedCentroids().get(i);
	}
}
