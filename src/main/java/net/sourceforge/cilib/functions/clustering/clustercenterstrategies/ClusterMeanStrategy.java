package net.sourceforge.cilib.functions.clustering.clustercenterstrategies;

import java.util.ArrayList;

import net.sourceforge.cilib.functions.clustering.ClusteringFitnessFunction;
import net.sourceforge.cilib.problem.dataset.ClusterableDataSet.Pattern;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * The <i>center of a cluster</i> is interpreted as the <i>mean of a cluster</i>.
 * @author Theuns Cloete
 */
public class ClusterMeanStrategy extends ClusterCenterStrategy {
	private static final long serialVersionUID = 9080168372118441393L;

	public ClusterMeanStrategy(ClusteringFitnessFunction cff) {
		super(cff);
	}

	/**
	 * In this case, we are interested in the mean of the cluster.
	 * @param i The integer representing the cluster for which the mean should be returned 
	 * @return the mean of cluster i
	 */
	@Override
	public Vector getCenter(int i) {
		ArrayList<Pattern> cluster = clusteringFitnessFunction.getArrangedClusters().get(i);

		if (cluster.isEmpty())
			throw new IllegalArgumentException("Cannot calculate the mean for an empty cluster");

		Vector mean = cluster.get(0).data.clone();
		mean.reset();

		for (Pattern pattern : cluster) {
			mean = mean.plus(pattern.data);
		}
		return mean.divide(cluster.size());
	}
}
