package net.sourceforge.cilib.functions.continuous;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import net.sourceforge.cilib.problem.dataset.AssociatedPairDataSetBuilder;
import net.sourceforge.cilib.problem.dataset.CachedDistanceDataSetBuilder;
import net.sourceforge.cilib.problem.dataset.MockClusteringStringDataSet;
import net.sourceforge.cilib.problem.dataset.ClusterableDataSet.Pattern;
import net.sourceforge.cilib.type.types.MixedVector;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Vector;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ClusteringFitnessFunctionTest {
	private static ClusteringFitnessFunction function = null;
	private static AssociatedPairDataSetBuilder dataSetBuilder = null;
	private static Vector centroids = null;
	
	@BeforeClass
	public static void intialise() {
//		dataSetBuilder = new AssociatedPairDataSetBuilder();
		dataSetBuilder = new CachedDistanceDataSetBuilder();
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
		function = null;
		dataSetBuilder = null;
		centroids = null;
	}
	
	@Test
	public void testQuantisationError() {
		function = new QuantisationErrorFunction();
		function.setDomain("R(0.0, 8.0)^8");
		function.setDataSet(dataSetBuilder);
		assertEquals(1.88489999498783, function.evaluate(centroids), 0.000000000001);
	}
	
	@Test
	public void testMinimumDistanceBetweenCentroidPairs() {
		function = new ParametricClusteringFunction();
		function.setDomain("R(0.0, 8.0)^8");
		function.setDataSet(dataSetBuilder);
		assertEquals(Math.sqrt(5), function.calculateInterClusterDistance(centroids));
	}
	
	@Test
	public void testClusterDiameter() {
		function = new DunnIndex();
		function.setDomain("R(0.0, 8.0)^8");
		function.setDataSet(dataSetBuilder);
		ArrayList<ArrayList<Pattern>> clusters = dataSetBuilder.arrangedClusters();
		assertEquals(0.0, function.calculateClusterDiameter(clusters.get(0)));
		assertEquals(Math.sqrt(13), function.calculateClusterDiameter(clusters.get(1)));
		assertEquals(Math.sqrt(0.5), function.calculateClusterDiameter(clusters.get(2)));
		assertEquals(Math.sqrt(29.25), function.calculateClusterDiameter(clusters.get(3)));
	}
	
	@Test
	public void testClusterDissimilarity() {
		function = new DunnIndex();
		function.setDomain("R(0.0, 8.0)^8");
		function.setDataSet(dataSetBuilder);
		ArrayList<ArrayList<Pattern>> clusters = dataSetBuilder.arrangedClusters();
		assertEquals(Math.sqrt(2), function.calculateClusterDissimilarity(clusters.get(0), clusters.get(1)));
		assertEquals(Math.sqrt(2), function.calculateClusterDissimilarity(clusters.get(0), clusters.get(2)));
		assertEquals(Math.sqrt(13), function.calculateClusterDissimilarity(clusters.get(0), clusters.get(3)));
		assertEquals(Math.sqrt(2.25), function.calculateClusterDissimilarity(clusters.get(1), clusters.get(2)));
		assertEquals(Math.sqrt(3.25), function.calculateClusterDissimilarity(clusters.get(1), clusters.get(3)));
		assertEquals(Math.sqrt(8.5), function.calculateClusterDissimilarity(clusters.get(2), clusters.get(3)));
	}
}
