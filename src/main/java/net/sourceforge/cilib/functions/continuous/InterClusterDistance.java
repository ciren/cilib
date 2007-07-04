package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.type.types.Vector;

public class InterClusterDistance extends ClusteringFitnessFunction {
	private static final long serialVersionUID = 6533014298881438534L;

	@Override
	public double evaluate(Vector centroids) {
		if(dataset == null)
			resetDataSet();
		//assign each pattern in the dataset to its closest centroid
		dataset.assign(centroids);
		return calculateInterClusterDistance(centroids);
	}
}
