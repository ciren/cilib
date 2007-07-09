package net.sourceforge.cilib.functions.continuous;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.problem.dataset.AssociatedPairDataSetBuilder;
import net.sourceforge.cilib.problem.dataset.CachedDistanceDataSetBuilder;
import net.sourceforge.cilib.problem.dataset.MockClusteringStringDataSet;
import net.sourceforge.cilib.problem.dataset.ClusterableDataSet.Pattern;
import net.sourceforge.cilib.type.types.Real;
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
		function = null;
		dataSetBuilder = null;
		centroids = null;
	}
	
	@Test
	public void testQuantisationError() {
		assertEquals(1.88489999498783, function.evaluate(centroids), 0.000000000001);
	}
	
	@Test
	public void testMinimumDistanceBetweenCentroidPairs() {
		assertEquals(Math.sqrt(5), function.calculateInterClusterDistance(centroids));
	}
	
	@Test
	public void testClusterDiameter() {
		ArrayList<ArrayList<Pattern>> clusters = dataSetBuilder.arrangedClusters();
		assertEquals(0.0, function.calculateClusterDiameter(clusters.get(0)));
		assertEquals(Math.sqrt(13), function.calculateClusterDiameter(clusters.get(1)));
		assertEquals(Math.sqrt(0.5), function.calculateClusterDiameter(clusters.get(2)));
		assertEquals(Math.sqrt(29.25), function.calculateClusterDiameter(clusters.get(3)));
	}
	
	@Test
	public void testClusterDissimilarity() {
		ArrayList<ArrayList<Pattern>> clusters = dataSetBuilder.arrangedClusters();
		assertEquals(Math.sqrt(2), function.calculateClusterDissimilarity(clusters.get(0), clusters.get(1)));
		assertEquals(Math.sqrt(2), function.calculateClusterDissimilarity(clusters.get(0), clusters.get(2)));
		assertEquals(Math.sqrt(13), function.calculateClusterDissimilarity(clusters.get(0), clusters.get(3)));
		assertEquals(Math.sqrt(2.25), function.calculateClusterDissimilarity(clusters.get(1), clusters.get(2)));
		assertEquals(Math.sqrt(3.25), function.calculateClusterDissimilarity(clusters.get(1), clusters.get(3)));
		assertEquals(Math.sqrt(8.5), function.calculateClusterDissimilarity(clusters.get(2), clusters.get(3)));
	}
}
