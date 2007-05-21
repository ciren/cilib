package net.sourceforge.cilib.functions.continuous;

import java.util.ArrayList;

import net.sourceforge.cilib.problem.dataset.ClusterableDataSet.Pattern;
import net.sourceforge.cilib.type.types.Vector;

public class DunnIndex extends ClusteringFitnessFunction {
	private static final long serialVersionUID = -7440453719679272149L;

	/**
	 * The constructor, nothing much is done here, except that it calls the base class' constructor.
	 * {@link net.sourceforge.cilib.functions.continuous.ClusteringFitnessFunction}
	 */
	public DunnIndex() {
		super();
	}

	@Override
	public double evaluate(Vector centroids) {
		if(dataset == null)
			resetDataSet();
		//assign each pattern in the dataset to its closest centroid
		dataset.assign(centroids);
		
		ArrayList<ArrayList<Pattern>> clusters = dataset.arrangedClusters();
		int numberOfClusters = dataset.getNumberOfClusters();
		assert clusters.size() == numberOfClusters;
		assert numberOfClusters > 0;

		double diameter = 0.0, tmp = 0.0;
		for(ArrayList<Pattern> cluster : clusters) {
			tmp = calculateClusterDiameter(cluster);
			if(tmp > diameter) {
				diameter = tmp;
			}
		}
		if(diameter <= 0)
			throw new ArithmeticException("Bad things (devision by zero) will happen when the maximum cluster diameter is zero!");

		double dunn = Double.MAX_VALUE;
		for(int i = 0; i < numberOfClusters - 1; i++) {
			ArrayList<Pattern> clusterI = clusters.get(i);
			for(int j = i + 1; j < numberOfClusters; j++) {
				ArrayList<Pattern> clusterJ = clusters.get(j);
				tmp = calculateClusterDissimilarity(clusterI, clusterJ) / diameter;
				if(tmp < dunn) {
					dunn = tmp;
				}
			}
		}
		return dunn;
	}
}
