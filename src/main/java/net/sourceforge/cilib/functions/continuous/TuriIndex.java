package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.math.random.RandomNumber;
import net.sourceforge.cilib.type.types.Vector;

public class TuriIndex extends ClusteringFitnessFunction {
	private double c = 0.0;
	private RandomNumber random = null;
	private double gaussian = 0.0;
	private double iteration = 0;

	public TuriIndex() {
		super();
		random = new RandomNumber();
		gaussian = random.getGaussian(2, 1);
		iteration = 0;
	}

	@Override
	public double evaluate(Vector centroids) {
		if(dataset == null)
			resetDataSet();
		//assign each pattern in the dataset to its closest centroid
		dataset.assign(centroids);
		
		int i = Algorithm.get().getIterations();
		if(iteration != i) {
			gaussian = random.getGaussian(2, 1);
			iteration = i;
		}

		return (c * gaussian + 1) * (calculateIntraClusterDistance(centroids) / calculateInterClusterDistance(centroids));
	}

	public void setC(double c) {
		this.c = c;
	}
}
