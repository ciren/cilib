package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.type.types.container.Vector;

public class IntraClusterDistance extends ClusteringFitnessFunction {
	private static final long serialVersionUID = -4185205766188040942L;

	@Override
	public double evaluate(Vector centroids) {
		if(dataset == null)
			resetDataSet();
		//assign each pattern in the dataset to its closest centroid
		dataset.assign(centroids);
		return calculateIntraClusterDistance(centroids);
	}
}
