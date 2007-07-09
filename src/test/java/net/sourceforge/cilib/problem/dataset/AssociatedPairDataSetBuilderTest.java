package net.sourceforge.cilib.problem.dataset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import net.sourceforge.cilib.functions.continuous.ClusteringFitnessFunction;
import net.sourceforge.cilib.functions.continuous.QuantisationErrorFunction;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.problem.dataset.ClusterableDataSet.Pattern;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.MixedVector;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class AssociatedPairDataSetBuilderTest {
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
	public void testMiscStuff() {
		assertEquals(dataSetBuilder.getNumberOfClusters(), 4);
		assertEquals(dataSetBuilder.getNumberOfPatterns(), 16);
		assertEquals(centroids.size(), 8);
		for(int i = 0; i < 16; i++)
			assertEquals(dataSetBuilder.getPattern(i).data.size(), 2);
	}
	
	@Test
	public void testAssign() {
		dataSetBuilder.assign(centroids);
		assertEquals(dataSetBuilder.getPattern(0).clas, 3);
		assertEquals(dataSetBuilder.getPattern(1).clas, 3);
		assertEquals(dataSetBuilder.getPattern(2).clas, 3);
		assertEquals(dataSetBuilder.getPattern(3).clas, 3);
		assertEquals(dataSetBuilder.getPattern(4).clas, 3);
		assertEquals(dataSetBuilder.getPattern(5).clas, 1);
		assertEquals(dataSetBuilder.getPattern(6).clas, 1);
		assertEquals(dataSetBuilder.getPattern(7).clas, 0);
		assertEquals(dataSetBuilder.getPattern(8).clas, 3);
		assertEquals(dataSetBuilder.getPattern(9).clas, 1);
		assertEquals(dataSetBuilder.getPattern(10).clas, 2);
		assertEquals(dataSetBuilder.getPattern(11).clas, 2);
		assertEquals(dataSetBuilder.getPattern(12).clas, 1);
		assertEquals(dataSetBuilder.getPattern(13).clas, 3);
		assertEquals(dataSetBuilder.getPattern(14).clas, 3);
		assertEquals(dataSetBuilder.getPattern(15).clas, 3);
	}
	
	@Test
	public void testPatternsInCluster() {
		ArrayList<ArrayList<Pattern>> clusters = dataSetBuilder.arrangedClusters();
		assertTrue(clusters.get(0).get(0).equals(dataSetBuilder.getPattern(7)));
		assertTrue(clusters.get(1).get(0).equals(dataSetBuilder.getPattern(5)));
		assertTrue(clusters.get(1).get(1).equals(dataSetBuilder.getPattern(6)));
		assertTrue(clusters.get(1).get(2).equals(dataSetBuilder.getPattern(9)));
		assertTrue(clusters.get(1).get(3).equals(dataSetBuilder.getPattern(12)));
		assertTrue(clusters.get(2).get(0).equals(dataSetBuilder.getPattern(10)));
		assertTrue(clusters.get(2).get(1).equals(dataSetBuilder.getPattern(11)));
		assertTrue(clusters.get(3).get(0).equals(dataSetBuilder.getPattern(0)));
		assertTrue(clusters.get(3).get(1).equals(dataSetBuilder.getPattern(1)));
		assertTrue(clusters.get(3).get(2).equals(dataSetBuilder.getPattern(2)));
		assertTrue(clusters.get(3).get(3).equals(dataSetBuilder.getPattern(3)));
		assertTrue(clusters.get(3).get(4).equals(dataSetBuilder.getPattern(4)));
		assertTrue(clusters.get(3).get(5).equals(dataSetBuilder.getPattern(8)));
		assertTrue(clusters.get(3).get(6).equals(dataSetBuilder.getPattern(13)));
		assertTrue(clusters.get(3).get(7).equals(dataSetBuilder.getPattern(14)));
		assertTrue(clusters.get(3).get(8).equals(dataSetBuilder.getPattern(15)));
	}
	
	@Test
	public void testGetSubCentroid() {
		assertTrue(dataSetBuilder.getSubCentroid(centroids, 0).equals(centroids.subVector(0, 1)));
		assertTrue(dataSetBuilder.getSubCentroid(centroids, 1).equals(centroids.subVector(2, 3)));
		assertTrue(dataSetBuilder.getSubCentroid(centroids, 2).equals(centroids.subVector(4, 5)));
		assertTrue(dataSetBuilder.getSubCentroid(centroids, 3).equals(centroids.subVector(6, 7)));
	}
}
