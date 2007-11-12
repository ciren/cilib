package net.sourceforge.cilib.functions.clustering;

import net.sourceforge.cilib.problem.dataset.ClusterableDataSet.Pattern;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This is the k-harmonic means clustering fitness function.
 * NOTE: By default, the cluster center refers to the cluster centroid. See {@link ClusterCenterStrategy}.
 */
public class KHarmonicMeansFunction extends ClusteringFitnessFunction {
	private static final long serialVersionUID = 2680037315045146954L;

	public KHarmonicMeansFunction() {
		super();
	}

	@Override
	public double calculateFitness() {
		double harmonicMean = 0.0;

		for (Pattern pattern : dataset.getPatterns()) {
			double sumOfReciprocals = 0.0;

			for (int i = 0; i < arrangedClusters.size(); i++) {
				Vector center = clusterCenterStrategy.getCenter(i);
				sumOfReciprocals += 1.0 / Math.max(calculateDistance(pattern.data, center), Double.MIN_VALUE);		// if the distance == 0.0, use a very small value
			}
			harmonicMean += clustersFormed / sumOfReciprocals;
		}
		return harmonicMean;
	}
}
