package net.sourceforge.cilib.measurement.single;

import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.measurement.Measurement;
import net.sourceforge.cilib.problem.dataset.ClusterableDataSet;
import net.sourceforge.cilib.problem.dataset.DataSetBuilder;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

/**
 * This measurement measures the number of clusters that were formed during a particular clustering.
 * For this measurement to work, the following is important:
 * <ol>
 * <li><tt>Algorithm.get()</tt> should not be <tt>null</tt>.</li>
 * <li>The algorithm's best solution (best position) should return a {@linkplain Vector}.</li>
 * <li>The algorithm's problem's {@linkplain DataSetBuilder} should be a
 * {@linkplain ClusterableDataSet}.</li>
 * <li>The <tt>arrangeClustersAndCentroids()</tt> method (defined in
 * {@linkplain ClusterableDataSet}) should be implemented to remove <i>empty clusters</i>.</li>
 * <li>The <tt>getArrangedClusters()</tt> method (defined in {@linkplain ClusterableDataSet})
 * should be implemented to return the list of non-empty clusters.</li>
 * </ol>
 * @author Theuns Cloete
 */
public class NumberOfClustersFormed implements Measurement {
	private static final long serialVersionUID = 2174807313995885918L;

	public NumberOfClustersFormed() {
	}

	public NumberOfClustersFormed(NumberOfClustersFormed rhs) {
	}

	public NumberOfClustersFormed getClone() {
		return new NumberOfClustersFormed(this);
	}

	public String getDomain() {
		return "Z";
	}

	public Type getValue() {
		Algorithm algorithm = Algorithm.get();
		Vector centroids = (Vector) algorithm.getBestSolution().getPosition();
		ClusterableDataSet dataset = (ClusterableDataSet) algorithm.getOptimisationProblem().getDataSetBuilder();
		dataset.arrangeClustersAndCentroids(centroids);
		return new Int(dataset.getArrangedClusters().size());
	}

}
