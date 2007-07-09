package net.sourceforge.cilib.functions.continuous;

import java.util.ArrayList;

import net.sourceforge.cilib.problem.dataset.ClusterableDataSet.Pattern;
import net.sourceforge.cilib.type.types.container.Vector;

public class ScatterDensityIndex extends ClusteringFitnessFunction {
	private static final long serialVersionUID = 1164537525165848345L;

	/**
	 * The constructor, nothing much is done here, except that it calls the base class' constructor.
	 * {@link net.sourceforge.cilib.functions.continuous.ClusteringFitnessFunction}
	 */
	public ScatterDensityIndex() {
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

		Vector datasetVariance = dataset.getVariance(), clusterVariance = null;
		double scattering = 0.0, stdev = 0.0;
		for(int i = 0; i < numberOfClusters; i++) {
			clusterVariance = dataset.getSetVariance(clusters.get(i));
			if(clusterVariance != null && datasetVariance != null)
			{
				double cvNorm = clusterVariance.norm();
				scattering += cvNorm / datasetVariance.norm();
				stdev += cvNorm;
			}
		}
		scattering /= numberOfClusters;
		stdev = Math.sqrt(stdev);
		stdev /= numberOfClusters;

		Vector midPoint = null, centerI = null, centerJ = null;
		double density = 0.0;
		int midPointDensity = 0, clusterIDensity = 0, clusterJDensity = 0;
		for(int i = 0; i < numberOfClusters - 1; i++) {
			centerI = dataset.getSetMean(clusters.get(i));
			for(int j = i + 1; j < numberOfClusters; j++) {
				centerJ = dataset.getSetMean(clusters.get(j));

				//handle different cases of empty clusters
				if(centerI == null && centerJ == null)
					continue;
				else if(centerI == null || centerJ == null)
				{
					density++;
					continue;
				}

				//if we get here, then both clusters have patterns belonging to them
				midPoint = centerI.plus(centerJ);
				midPoint = midPoint.divide(2.0);
				midPointDensity = clusterIDensity = clusterJDensity = 0;
				for(Pattern pattern : clusters.get(i)) {
					if(dataset.calculateDistance(pattern.data, midPoint) <= stdev)
						++midPointDensity;
					if(dataset.calculateDistance(pattern.data, centerI) <= stdev)
						++clusterIDensity;
					if(dataset.calculateDistance(pattern.data, centerJ) <= stdev)
						++clusterJDensity;
				}
				for(Pattern pattern : clusters.get(j)) {
					if(dataset.calculateDistance(pattern.data, midPoint) <= stdev)
						++midPointDensity;
					if(dataset.calculateDistance(pattern.data, centerI) <= stdev)
						++clusterIDensity;
					if(dataset.calculateDistance(pattern.data, centerJ) <= stdev)
						++clusterJDensity;
				}
				double denominator = (double)Math.max(clusterIDensity, clusterJDensity);
				if(denominator == 0.0)
					continue;
				density += midPointDensity / denominator;
			}
		}
		density /= (numberOfClusters * (numberOfClusters - 1));
		return scattering + density;
	}
}
