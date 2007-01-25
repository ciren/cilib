package net.sourceforge.cilib.problem.dataset;

import net.sourceforge.cilib.type.types.MixedVector;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Vector;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class AssociatedPairDataSetBuilderTest {
	
	private static AssociatedPairDataSetBuilder dataSetBuilder = null;
	private static Vector centroids = null;
	
	@BeforeClass
	public static void intialise() {
		dataSetBuilder = new AssociatedPairDataSetBuilder();
		dataSetBuilder.numberOfClusters = 4;
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
	public void testMiscStuff() {
		assertEquals(dataSetBuilder.getNumberOfClusters(), 4);
		assertEquals(dataSetBuilder.getNumberOfPatterns(), 16);
		assertEquals(centroids.size(), 8);
		for(int i = 0; i < 16; i++)
			assertEquals(dataSetBuilder.getPattern(i).size(), 2);
	}
	
	@Test
	public void testAssign() {
		dataSetBuilder.assign(centroids);
		assertEquals(dataSetBuilder.getKey(0).getInt(), 3);
		assertEquals(dataSetBuilder.getKey(1).getInt(), 3);
		assertEquals(dataSetBuilder.getKey(2).getInt(), 3);
		assertEquals(dataSetBuilder.getKey(3).getInt(), 3);
		assertEquals(dataSetBuilder.getKey(4).getInt(), 3);
		assertEquals(dataSetBuilder.getKey(5).getInt(), 1);
		assertEquals(dataSetBuilder.getKey(6).getInt(), 1);
		assertEquals(dataSetBuilder.getKey(7).getInt(), 0);
		assertEquals(dataSetBuilder.getKey(8).getInt(), 3);
		assertEquals(dataSetBuilder.getKey(9).getInt(), 1);
		assertEquals(dataSetBuilder.getKey(10).getInt(), 2);
		assertEquals(dataSetBuilder.getKey(11).getInt(), 2);
		assertEquals(dataSetBuilder.getKey(12).getInt(), 1);
		assertEquals(dataSetBuilder.getKey(13).getInt(), 3);
		assertEquals(dataSetBuilder.getKey(14).getInt(), 3);
		assertEquals(dataSetBuilder.getKey(15).getInt(), 3);
	}
	
	@Test
	public void testPatternsInCluster() {
		assertTrue(((Vector)dataSetBuilder.patternsInCluster(new Int(0)).get(0)).equals(dataSetBuilder.getPattern(7)));
		assertTrue(((Vector)dataSetBuilder.patternsInCluster(new Int(1)).get(0)).equals(dataSetBuilder.getPattern(5)));
		assertTrue(((Vector)dataSetBuilder.patternsInCluster(new Int(1)).get(1)).equals(dataSetBuilder.getPattern(6)));
		assertTrue(((Vector)dataSetBuilder.patternsInCluster(new Int(1)).get(2)).equals(dataSetBuilder.getPattern(9)));
		assertTrue(((Vector)dataSetBuilder.patternsInCluster(new Int(1)).get(3)).equals(dataSetBuilder.getPattern(12)));
		assertTrue(((Vector)dataSetBuilder.patternsInCluster(new Int(2)).get(0)).equals(dataSetBuilder.getPattern(10)));
		assertTrue(((Vector)dataSetBuilder.patternsInCluster(new Int(2)).get(1)).equals(dataSetBuilder.getPattern(11)));
		assertTrue(((Vector)dataSetBuilder.patternsInCluster(new Int(3)).get(0)).equals(dataSetBuilder.getPattern(0)));
		assertTrue(((Vector)dataSetBuilder.patternsInCluster(new Int(3)).get(1)).equals(dataSetBuilder.getPattern(1)));
		assertTrue(((Vector)dataSetBuilder.patternsInCluster(new Int(3)).get(2)).equals(dataSetBuilder.getPattern(2)));
		assertTrue(((Vector)dataSetBuilder.patternsInCluster(new Int(3)).get(3)).equals(dataSetBuilder.getPattern(3)));
		assertTrue(((Vector)dataSetBuilder.patternsInCluster(new Int(3)).get(4)).equals(dataSetBuilder.getPattern(4)));
		assertTrue(((Vector)dataSetBuilder.patternsInCluster(new Int(3)).get(5)).equals(dataSetBuilder.getPattern(8)));
		assertTrue(((Vector)dataSetBuilder.patternsInCluster(new Int(3)).get(6)).equals(dataSetBuilder.getPattern(13)));
		assertTrue(((Vector)dataSetBuilder.patternsInCluster(new Int(3)).get(7)).equals(dataSetBuilder.getPattern(14)));
		assertTrue(((Vector)dataSetBuilder.patternsInCluster(new Int(3)).get(8)).equals(dataSetBuilder.getPattern(15)));
	}
	
	@Test
	public void testGetSubCentroid() {
		assertTrue(dataSetBuilder.getSubCentroid(centroids, 0).equals(centroids.subVector(0, 1)));
		assertTrue(dataSetBuilder.getSubCentroid(centroids, 1).equals(centroids.subVector(2, 3)));
		assertTrue(dataSetBuilder.getSubCentroid(centroids, 2).equals(centroids.subVector(4, 5)));
		assertTrue(dataSetBuilder.getSubCentroid(centroids, 3).equals(centroids.subVector(6, 7)));
	}
}
