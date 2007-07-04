package net.sourceforge.cilib.functions.continuous;

import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.problem.dataset.AssociatedPairDataSetBuilder;
import net.sourceforge.cilib.problem.dataset.CachedDistanceDataSetBuilder;
import net.sourceforge.cilib.problem.dataset.MockClusteringStringDataSet;
import net.sourceforge.cilib.type.types.MixedVector;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Vector;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class QuantisationErrorTest {

	private static ClusteringFitnessFunction function = null;
	private static AssociatedPairDataSetBuilder dataSetBuilder = null;
	private static Vector centroids = null;
	private static FunctionOptimisationProblem problem = null;
	
	@BeforeClass
	public static void intialise() {
		function = new QuantisationErrorFunction();
		function.setDomain("R(0.0, 8.0)^8");
		dataSetBuilder = new CachedDistanceDataSetBuilder();
		dataSetBuilder.setNumberOfClusters(4);
		dataSetBuilder.setDataSet(new MockClusteringStringDataSet());
		function.setDataSet(dataSetBuilder);
		problem = new FunctionMinimisationProblem();
		problem.setFunction(function);
		problem.setDataSetBuilder(dataSetBuilder);
		centroids = new MixedVector();
		centroids.append(new Real(0.5));
		centroids.append(new Real(5.0));
		centroids.append(new Real(2.5));
		centroids.append(new Real(3.5));
		centroids.append(new Real(5.5));
		centroids.append(new Real(6.0));
		centroids.append(new Real(4.5));
		centroids.append(new Real(2.5));
	}
	
	
	@AfterClass
	public static void destroy() {
		dataSetBuilder = null;
		centroids = null;
	}
	
	@Test
	public void testEvaluate() { // this is not correct
		assertEquals(1.88489999498783, function.evaluate(centroids), 0.000000000001);
	}
}
