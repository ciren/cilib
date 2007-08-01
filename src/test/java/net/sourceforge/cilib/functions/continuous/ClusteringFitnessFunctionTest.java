package net.sourceforge.cilib.functions.continuous;

import static org.junit.Assert.assertEquals;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.problem.dataset.AssociatedPairDataSetBuilder;
import net.sourceforge.cilib.problem.dataset.CachingDataSetBuilder;
import net.sourceforge.cilib.problem.dataset.MockClusteringStringDataSet;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.container.MixedVector;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ClusteringFitnessFunctionTest {
	private static ClusteringFitnessFunction function = null;
	private static AssociatedPairDataSetBuilder dataSetBuilder = null;
	private static Vector centroids = null;
	private static FunctionOptimisationProblem problem = null;
	private static final double roundingError = 0.000000000001;
	
	@BeforeClass
	public static void intialise() {
		function = new QuantisationErrorFunction();
		function.setDomain("Z(0, 37),Z(0, 51),Z(0, 37),Z(0, 51),Z(0, 37),Z(0, 51),Z(0, 37),Z(0, 51),Z(0, 37),Z(0, 51),Z(0, 37),Z(0, 51),Z(0, 37),Z(0, 51)");
		dataSetBuilder = new CachingDataSetBuilder();
		dataSetBuilder.setNumberOfClusters(7);
		dataSetBuilder.setDataSet(new MockClusteringStringDataSet());
		function.setDataSet(dataSetBuilder);
		problem = new FunctionMinimisationProblem();
		problem.setFunction(function);
		problem.setDataSetBuilder(dataSetBuilder);
		centroids = new MixedVector();
		centroids.append(new Int(1));
		centroids.append(new Int(1));
		centroids.append(new Int(33));
		centroids.append(new Int(8));
		centroids.append(new Int(20));
		centroids.append(new Int(19));
		centroids.append(new Int(11));
		centroids.append(new Int(22));
		centroids.append(new Int(30));
		centroids.append(new Int(27));
		centroids.append(new Int(19));
		centroids.append(new Int(40));
		centroids.append(new Int(5));
		centroids.append(new Int(50));
	}
	
	
	@AfterClass
	public static void destroy() {
		function = null;
		dataSetBuilder = null;
		centroids = null;
	}

	@Test
	public void testEvaluate() {
		// the evaluate method arranges the clusters and centroids
		assertEquals(4.27188655918496, function.evaluate(centroids), roundingError);
	}

	@Test
	public void testQuantisationError() {
		assertEquals(4.27188655918496, function.calculateQuantisationError(), roundingError);
	}

	@Test
	public void testMaximumAverageDistance() {
		assertEquals(5.765984935187037, function.calculateMaximumAverageDistance(), roundingError);
	}

	@Test
	public void testMinimumInterClusterDistance() {
		assertEquals(Math.sqrt(90), function.calculateMinimumInterClusterDistance());
	}

	@Test
	public void testMaximumInterClusterDistance() {
		assertEquals(Math.sqrt(1845), function.calculateMaximumInterClusterDistance());
	}

	@Test
	public void testMinimumSetDistance() {
		assertEquals(Math.sqrt(388), function.calculateMinimumSetDistance(0, 1));
		assertEquals(Math.sqrt(389), function.calculateMinimumSetDistance(0, 2));
		assertEquals(Math.sqrt(405), function.calculateMinimumSetDistance(0, 3));
		assertEquals(Math.sqrt(925), function.calculateMinimumSetDistance(0, 4));
		assertEquals(Math.sqrt(1066), function.calculateMinimumSetDistance(0, 5));
		assertEquals(1.0, function.calculateMinimumSetDistance(1, 2));
		assertEquals(Math.sqrt(181), function.calculateMinimumSetDistance(1, 3));
		assertEquals(Math.sqrt(169), function.calculateMinimumSetDistance(1, 4));
		assertEquals(Math.sqrt(450), function.calculateMinimumSetDistance(1, 5));
		assertEquals(1.0, function.calculateMinimumSetDistance(2, 3));
		assertEquals(2.0, function.calculateMinimumSetDistance(2, 4));
		assertEquals(Math.sqrt(29), function.calculateMinimumSetDistance(2, 5));
		assertEquals(Math.sqrt(90), function.calculateMinimumSetDistance(3, 4));
		assertEquals(Math.sqrt(85), function.calculateMinimumSetDistance(3, 5));
		assertEquals(Math.sqrt(41), function.calculateMinimumSetDistance(4, 5));
	}

	@Test
	public void testAverageSetDistance() {
		assertEquals(28.74157583325409, function.calculateAverageSetDistance(0, 1), roundingError);
		assertEquals(23.48964010748228, function.calculateAverageSetDistance(0, 2), roundingError);
		assertEquals(22.3251767476371, function.calculateAverageSetDistance(0, 3), roundingError);
		assertEquals(31.41056805514129, function.calculateAverageSetDistance(0, 4), roundingError);
		assertEquals(40.7867709331524, function.calculateAverageSetDistance(0, 5), roundingError);
		assertEquals(18.55559976582913, function.calculateAverageSetDistance(1, 2), roundingError);
		assertEquals(27.60024942795051, function.calculateAverageSetDistance(1, 3), roundingError);
		assertEquals(21.74671419268618, function.calculateAverageSetDistance(1, 4), roundingError);
		assertEquals(38.91030178983154, function.calculateAverageSetDistance(1, 5), roundingError);
		assertEquals(10.52656908164985, function.calculateAverageSetDistance(2, 3), roundingError);
		assertEquals(9.524130794948, function.calculateAverageSetDistance(2, 4), roundingError);
		assertEquals(22.78200247378393, function.calculateAverageSetDistance(2, 5), roundingError);
		assertEquals(13.81371117279074, function.calculateAverageSetDistance(3, 4), roundingError);
		assertEquals(18.65560457581544, function.calculateAverageSetDistance(3, 5), roundingError);
		assertEquals(17.464647454513713, function.calculateAverageSetDistance(4, 5), roundingError);
	}

	@Test
	public void testClusterDiameter() {
		assertEquals(0.0, function.calculateClusterDiameter(0));
		assertEquals(Math.sqrt(233), function.calculateClusterDiameter(1));
		assertEquals(Math.sqrt(226), function.calculateClusterDiameter(2));
		assertEquals(Math.sqrt(34), function.calculateClusterDiameter(3));
		assertEquals(Math.sqrt(32), function.calculateClusterDiameter(4));
		assertEquals(Math.sqrt(153), function.calculateClusterDiameter(5));
	}

	@Test
	public void testIntraClusterDistance() {
		assertEquals(427.037141232211, function.calculateIntraClusterDistance(), roundingError);
	}

	@Test
	public void testAverageIntraClusterDistance() {
		assertEquals(427.037141232211 / 93, function.calculateAverageIntraClusterDistance(), roundingError);
	}
}
