package net.sourceforge.cilib.functions.continuous;

import static org.junit.Assert.assertEquals;
import net.sourceforge.cilib.problem.dataset.AssociatedPairDataSetBuilder;
import net.sourceforge.cilib.problem.dataset.MockClusteringStringDataSet;
import net.sourceforge.cilib.type.types.MixedVector;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Vector;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class QuantisationErrorTest {

	private static AssociatedPairDataSetBuilder dataSetBuilder = null;
	private static Vector centroids = null;
	
	@BeforeClass
	public static void intialise() {
		dataSetBuilder = new AssociatedPairDataSetBuilder();
		dataSetBuilder.setNumberOfClusters(4);
		dataSetBuilder.setDataSet(new MockClusteringStringDataSet());
		dataSetBuilder.initialise();
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
	public void testEvaluate() {
		ClusteringFitnessFunction function = new QuantisationErrorFunction();
		function.setDomain("R(0.0, 8.0)^8");
		function.setDataSet(dataSetBuilder);
		assertEquals(1.88489999498783, function.evaluate(centroids), 0.000000000001);
	}
}
