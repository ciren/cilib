package net.sourceforge.cilib.functions.continuous;

import java.util.ArrayList;

import net.sourceforge.cilib.problem.dataset.ClusterableDataSet.Pattern;
import net.sourceforge.cilib.type.types.Vector;

public class DaviesBouldinIndex extends ClusteringFitnessFunction {

	/**
	 * The constructor, nothing much is done here, except that it calls the base class' constructor.
	 * {@link net.sourceforge.cilib.functions.continuous.ClusteringFitnessFunction}
	 */
	public DaviesBouldinIndex() {
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

		//cache the cluster diameters
		double diameters[] = new double[numberOfClusters];
		//cache the cluster dissimilarities
		double dissimilarities[] = new double[(numberOfClusters * (numberOfClusters - 1)) / 2];
		for(int y = 0; y < numberOfClusters; y++) {
			diameters[y] = calculateClusterDiameter(clusters.get(y));
			for(int x = y + 1; x < numberOfClusters; x++) {
				dissimilarities[x + (numberOfClusters * y) - (((y + 1) * (y + 2)) / 2)] = calculateClusterDissimilarity(clusters.get(x), clusters.get(y)); 
			}
		}

		double db = 0.0, max = 0.0, tmp = 0.0;
		for(int y = 0; y < numberOfClusters - 1; y++) {
			for(int x = y + 1; x < numberOfClusters; x++) {
				tmp = diameters[y] + diameters[x];
				tmp /= dissimilarities[x + (numberOfClusters * y) - (((y + 1) * (y + 2)) / 2)];
				if(tmp > max) {
					max = tmp;
				}
			}
			db += max;
			max = 0.0;
		}
		return db / numberOfClusters;
	}
}
