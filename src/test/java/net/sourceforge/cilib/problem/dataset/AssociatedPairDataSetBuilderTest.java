package net.sourceforge.cilib.problem.dataset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import net.sourceforge.cilib.functions.continuous.ClusteringFitnessFunction;
import net.sourceforge.cilib.functions.continuous.QuantisationErrorFunction;
import net.sourceforge.cilib.problem.FunctionMinimisationProblem;
import net.sourceforge.cilib.problem.FunctionOptimisationProblem;
import net.sourceforge.cilib.problem.dataset.ClusterableDataSet.Pattern;
import net.sourceforge.cilib.type.types.Int;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.MixedVector;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class AssociatedPairDataSetBuilderTest {
	private static ClusteringFitnessFunction function = null;
	private static AssociatedPairDataSetBuilder dataSetBuilder = null;
	private static ArrayList<ArrayList<Pattern>> arrangedClusters = null;
	private static ArrayList<Vector> arrangedCentroids = null;
	private static Vector centroids = null;
	private static FunctionOptimisationProblem problem = null;

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
		dataSetBuilder.arrangeClustersAndCentroids(centroids);
		arrangedClusters = dataSetBuilder.getArrangedClusters();
		arrangedCentroids = dataSetBuilder.getArrangedCentroids();
	}

	@AfterClass
	public static void destroy() {
		dataSetBuilder = null;
		centroids = null;
	}

	@Test
	public void testArrangeClustersAndCentroids() {
		assertEquals(6, arrangedCentroids.size());
		assertEquals(6, arrangedClusters.size());
		assertEquals(arrangedCentroids.size(), arrangedClusters.size());

		assertEquals(1, arrangedClusters.get(0).size());
		assertEquals(26, arrangedClusters.get(1).size());
		assertEquals(29, arrangedClusters.get(2).size());
		assertEquals(18, arrangedClusters.get(3).size());
		assertEquals(6, arrangedClusters.get(4).size());
		assertEquals(13, arrangedClusters.get(5).size());
	}

	@Test
	public void testMiscStuff() {
		assertEquals(7, dataSetBuilder.getNumberOfClusters());
		assertEquals(93, dataSetBuilder.getNumberOfPatterns());
		assertEquals(centroids.size(), 14);

		for (ArrayList<Pattern> cluster : arrangedClusters) {
			for (Pattern pattern : cluster) {
				assertEquals(2, pattern.data.size());
			}
		}

		for (Vector centroid : arrangedCentroids) {
			assertEquals(2, centroid.size());
		}
	}

	@Test
	public void testPatternAssignments() {
		assertEquals(0, dataSetBuilder.getPattern(0).clas);

		assertEquals(1, dataSetBuilder.getPattern(1).clas);
		assertEquals(1, dataSetBuilder.getPattern(2).clas);
		assertEquals(1, dataSetBuilder.getPattern(3).clas);
		assertEquals(1, dataSetBuilder.getPattern(4).clas);
		assertEquals(1, dataSetBuilder.getPattern(5).clas);
		assertEquals(1, dataSetBuilder.getPattern(6).clas);
		assertEquals(1, dataSetBuilder.getPattern(7).clas);
		assertEquals(1, dataSetBuilder.getPattern(8).clas);
		assertEquals(1, dataSetBuilder.getPattern(9).clas);
		assertEquals(1, dataSetBuilder.getPattern(10).clas);
		assertEquals(1, dataSetBuilder.getPattern(11).clas);
		assertEquals(1, dataSetBuilder.getPattern(12).clas);
		assertEquals(1, dataSetBuilder.getPattern(13).clas);
		assertEquals(1, dataSetBuilder.getPattern(14).clas);
		assertEquals(1, dataSetBuilder.getPattern(15).clas);
		assertEquals(1, dataSetBuilder.getPattern(16).clas);
		assertEquals(1, dataSetBuilder.getPattern(17).clas);
		assertEquals(1, dataSetBuilder.getPattern(18).clas);
		assertEquals(1, dataSetBuilder.getPattern(19).clas);
		assertEquals(1, dataSetBuilder.getPattern(20).clas);
		assertEquals(1, dataSetBuilder.getPattern(21).clas);
		assertEquals(1, dataSetBuilder.getPattern(22).clas);
		assertEquals(1, dataSetBuilder.getPattern(23).clas);
		assertEquals(1, dataSetBuilder.getPattern(24).clas);
		assertEquals(1, dataSetBuilder.getPattern(25).clas);
		assertEquals(1, dataSetBuilder.getPattern(26).clas);

		assertEquals(2, dataSetBuilder.getPattern(27).clas);
		assertEquals(2, dataSetBuilder.getPattern(28).clas);
		assertEquals(2, dataSetBuilder.getPattern(29).clas);
		assertEquals(2, dataSetBuilder.getPattern(30).clas);
		assertEquals(2, dataSetBuilder.getPattern(31).clas);
		assertEquals(2, dataSetBuilder.getPattern(32).clas);
		assertEquals(2, dataSetBuilder.getPattern(33).clas);
		assertEquals(2, dataSetBuilder.getPattern(34).clas);
		assertEquals(2, dataSetBuilder.getPattern(35).clas);
		assertEquals(2, dataSetBuilder.getPattern(36).clas);
		assertEquals(2, dataSetBuilder.getPattern(37).clas);
		assertEquals(2, dataSetBuilder.getPattern(38).clas);
		assertEquals(2, dataSetBuilder.getPattern(39).clas);
		assertEquals(2, dataSetBuilder.getPattern(40).clas);

		assertEquals(3, dataSetBuilder.getPattern(41).clas);

		assertEquals(2, dataSetBuilder.getPattern(42).clas);	// this pattern can belong to either 2 or 3, depending on the order of the centroids
		assertEquals(2, dataSetBuilder.getPattern(43).clas);
		assertEquals(2, dataSetBuilder.getPattern(44).clas);
		assertEquals(2, dataSetBuilder.getPattern(45).clas);
		assertEquals(2, dataSetBuilder.getPattern(46).clas);
		assertEquals(2, dataSetBuilder.getPattern(47).clas);
		assertEquals(2, dataSetBuilder.getPattern(48).clas);

		assertEquals(3, dataSetBuilder.getPattern(49).clas);
		assertEquals(3, dataSetBuilder.getPattern(50).clas);
		assertEquals(3, dataSetBuilder.getPattern(51).clas);

		assertEquals(2, dataSetBuilder.getPattern(52).clas);

		assertEquals(3, dataSetBuilder.getPattern(53).clas);
		assertEquals(3, dataSetBuilder.getPattern(54).clas);
		assertEquals(3, dataSetBuilder.getPattern(55).clas);
		assertEquals(3, dataSetBuilder.getPattern(56).clas);

		assertEquals(2, dataSetBuilder.getPattern(57).clas);
		assertEquals(2, dataSetBuilder.getPattern(58).clas);
		assertEquals(2, dataSetBuilder.getPattern(59).clas);
		assertEquals(2, dataSetBuilder.getPattern(60).clas);

		assertEquals(4, dataSetBuilder.getPattern(61).clas);

		assertEquals(3, dataSetBuilder.getPattern(62).clas);
		assertEquals(3, dataSetBuilder.getPattern(63).clas);
		assertEquals(3, dataSetBuilder.getPattern(64).clas);
		assertEquals(3, dataSetBuilder.getPattern(65).clas);
		assertEquals(3, dataSetBuilder.getPattern(66).clas);
		assertEquals(3, dataSetBuilder.getPattern(67).clas);
		assertEquals(3, dataSetBuilder.getPattern(68).clas);
		assertEquals(3, dataSetBuilder.getPattern(69).clas);

		assertEquals(2, dataSetBuilder.getPattern(70).clas);
		assertEquals(2, dataSetBuilder.getPattern(71).clas);

		assertEquals(4, dataSetBuilder.getPattern(72).clas);
		assertEquals(4, dataSetBuilder.getPattern(73).clas);

		assertEquals(3, dataSetBuilder.getPattern(74).clas);
		assertEquals(3, dataSetBuilder.getPattern(75).clas);

		assertEquals(2, dataSetBuilder.getPattern(76).clas);

		assertEquals(4, dataSetBuilder.getPattern(77).clas);
		assertEquals(4, dataSetBuilder.getPattern(78).clas);
		assertEquals(4, dataSetBuilder.getPattern(79).clas);

		assertEquals(5, dataSetBuilder.getPattern(80).clas);
		assertEquals(5, dataSetBuilder.getPattern(81).clas);
		assertEquals(5, dataSetBuilder.getPattern(82).clas);
		assertEquals(5, dataSetBuilder.getPattern(83).clas);
		assertEquals(5, dataSetBuilder.getPattern(84).clas);
		assertEquals(5, dataSetBuilder.getPattern(85).clas);
		assertEquals(5, dataSetBuilder.getPattern(86).clas);
		assertEquals(5, dataSetBuilder.getPattern(87).clas);
		assertEquals(5, dataSetBuilder.getPattern(88).clas);
		assertEquals(5, dataSetBuilder.getPattern(89).clas);
		assertEquals(5, dataSetBuilder.getPattern(90).clas);
		assertEquals(5, dataSetBuilder.getPattern(91).clas);
		assertEquals(5, dataSetBuilder.getPattern(92).clas);
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void testArrangedClusters() {
		assertTrue(arrangedClusters.get(0).get(0).equals(dataSetBuilder.getPattern(0)));

		assertTrue(arrangedClusters.get(1).get(0).equals(dataSetBuilder.getPattern(1)));
		assertTrue(arrangedClusters.get(1).get(1).equals(dataSetBuilder.getPattern(2)));
		assertTrue(arrangedClusters.get(1).get(2).equals(dataSetBuilder.getPattern(3)));
		assertTrue(arrangedClusters.get(1).get(3).equals(dataSetBuilder.getPattern(4)));
		assertTrue(arrangedClusters.get(1).get(4).equals(dataSetBuilder.getPattern(5)));
		assertTrue(arrangedClusters.get(1).get(5).equals(dataSetBuilder.getPattern(6)));
		assertTrue(arrangedClusters.get(1).get(6).equals(dataSetBuilder.getPattern(7)));
		assertTrue(arrangedClusters.get(1).get(7).equals(dataSetBuilder.getPattern(8)));
		assertTrue(arrangedClusters.get(1).get(8).equals(dataSetBuilder.getPattern(9)));
		assertTrue(arrangedClusters.get(1).get(9).equals(dataSetBuilder.getPattern(10)));
		assertTrue(arrangedClusters.get(1).get(10).equals(dataSetBuilder.getPattern(11)));
		assertTrue(arrangedClusters.get(1).get(11).equals(dataSetBuilder.getPattern(12)));
		assertTrue(arrangedClusters.get(1).get(12).equals(dataSetBuilder.getPattern(13)));
		assertTrue(arrangedClusters.get(1).get(13).equals(dataSetBuilder.getPattern(14)));
		assertTrue(arrangedClusters.get(1).get(14).equals(dataSetBuilder.getPattern(15)));
		assertTrue(arrangedClusters.get(1).get(15).equals(dataSetBuilder.getPattern(16)));
		assertTrue(arrangedClusters.get(1).get(16).equals(dataSetBuilder.getPattern(17)));
		assertTrue(arrangedClusters.get(1).get(17).equals(dataSetBuilder.getPattern(18)));
		assertTrue(arrangedClusters.get(1).get(18).equals(dataSetBuilder.getPattern(19)));
		assertTrue(arrangedClusters.get(1).get(19).equals(dataSetBuilder.getPattern(20)));
		assertTrue(arrangedClusters.get(1).get(20).equals(dataSetBuilder.getPattern(21)));
		assertTrue(arrangedClusters.get(1).get(21).equals(dataSetBuilder.getPattern(22)));
		assertTrue(arrangedClusters.get(1).get(22).equals(dataSetBuilder.getPattern(23)));
		assertTrue(arrangedClusters.get(1).get(23).equals(dataSetBuilder.getPattern(24)));
		assertTrue(arrangedClusters.get(1).get(24).equals(dataSetBuilder.getPattern(25)));
		assertTrue(arrangedClusters.get(1).get(25).equals(dataSetBuilder.getPattern(26)));

		assertTrue(arrangedClusters.get(2).get(0).equals(dataSetBuilder.getPattern(27)));
		assertTrue(arrangedClusters.get(2).get(1).equals(dataSetBuilder.getPattern(28)));
		assertTrue(arrangedClusters.get(2).get(2).equals(dataSetBuilder.getPattern(29)));
		assertTrue(arrangedClusters.get(2).get(3).equals(dataSetBuilder.getPattern(30)));
		assertTrue(arrangedClusters.get(2).get(4).equals(dataSetBuilder.getPattern(31)));
		assertTrue(arrangedClusters.get(2).get(5).equals(dataSetBuilder.getPattern(32)));
		assertTrue(arrangedClusters.get(2).get(6).equals(dataSetBuilder.getPattern(33)));
		assertTrue(arrangedClusters.get(2).get(7).equals(dataSetBuilder.getPattern(34)));
		assertTrue(arrangedClusters.get(2).get(8).equals(dataSetBuilder.getPattern(35)));
		assertTrue(arrangedClusters.get(2).get(9).equals(dataSetBuilder.getPattern(36)));
		assertTrue(arrangedClusters.get(2).get(10).equals(dataSetBuilder.getPattern(37)));
		assertTrue(arrangedClusters.get(2).get(11).equals(dataSetBuilder.getPattern(38)));
		assertTrue(arrangedClusters.get(2).get(12).equals(dataSetBuilder.getPattern(39)));
		assertTrue(arrangedClusters.get(2).get(13).equals(dataSetBuilder.getPattern(40)));
		assertTrue(arrangedClusters.get(2).get(14).equals(dataSetBuilder.getPattern(42)));
		assertTrue(arrangedClusters.get(2).get(15).equals(dataSetBuilder.getPattern(43)));
		assertTrue(arrangedClusters.get(2).get(16).equals(dataSetBuilder.getPattern(44)));
		assertTrue(arrangedClusters.get(2).get(17).equals(dataSetBuilder.getPattern(45)));
		assertTrue(arrangedClusters.get(2).get(18).equals(dataSetBuilder.getPattern(46)));
		assertTrue(arrangedClusters.get(2).get(19).equals(dataSetBuilder.getPattern(47)));
		assertTrue(arrangedClusters.get(2).get(20).equals(dataSetBuilder.getPattern(48)));
		assertTrue(arrangedClusters.get(2).get(21).equals(dataSetBuilder.getPattern(52)));
		assertTrue(arrangedClusters.get(2).get(22).equals(dataSetBuilder.getPattern(57)));
		assertTrue(arrangedClusters.get(2).get(23).equals(dataSetBuilder.getPattern(58)));
		assertTrue(arrangedClusters.get(2).get(24).equals(dataSetBuilder.getPattern(59)));
		assertTrue(arrangedClusters.get(2).get(25).equals(dataSetBuilder.getPattern(60)));
		assertTrue(arrangedClusters.get(2).get(26).equals(dataSetBuilder.getPattern(70)));
		assertTrue(arrangedClusters.get(2).get(27).equals(dataSetBuilder.getPattern(71)));
		assertTrue(arrangedClusters.get(2).get(28).equals(dataSetBuilder.getPattern(76)));

		assertTrue(arrangedClusters.get(3).get(0).equals(dataSetBuilder.getPattern(41)));
		assertTrue(arrangedClusters.get(3).get(1).equals(dataSetBuilder.getPattern(49)));
		assertTrue(arrangedClusters.get(3).get(2).equals(dataSetBuilder.getPattern(50)));
		assertTrue(arrangedClusters.get(3).get(3).equals(dataSetBuilder.getPattern(51)));
		assertTrue(arrangedClusters.get(3).get(4).equals(dataSetBuilder.getPattern(53)));
		assertTrue(arrangedClusters.get(3).get(5).equals(dataSetBuilder.getPattern(54)));
		assertTrue(arrangedClusters.get(3).get(6).equals(dataSetBuilder.getPattern(55)));
		assertTrue(arrangedClusters.get(3).get(7).equals(dataSetBuilder.getPattern(56)));
		assertTrue(arrangedClusters.get(3).get(8).equals(dataSetBuilder.getPattern(62)));
		assertTrue(arrangedClusters.get(3).get(9).equals(dataSetBuilder.getPattern(63)));
		assertTrue(arrangedClusters.get(3).get(10).equals(dataSetBuilder.getPattern(64)));
		assertTrue(arrangedClusters.get(3).get(11).equals(dataSetBuilder.getPattern(65)));
		assertTrue(arrangedClusters.get(3).get(12).equals(dataSetBuilder.getPattern(66)));
		assertTrue(arrangedClusters.get(3).get(13).equals(dataSetBuilder.getPattern(67)));
		assertTrue(arrangedClusters.get(3).get(14).equals(dataSetBuilder.getPattern(68)));
		assertTrue(arrangedClusters.get(3).get(15).equals(dataSetBuilder.getPattern(69)));
		assertTrue(arrangedClusters.get(3).get(16).equals(dataSetBuilder.getPattern(74)));
		assertTrue(arrangedClusters.get(3).get(17).equals(dataSetBuilder.getPattern(75)));

		assertTrue(arrangedClusters.get(4).get(0).equals(dataSetBuilder.getPattern(61)));
		assertTrue(arrangedClusters.get(4).get(1).equals(dataSetBuilder.getPattern(72)));
		assertTrue(arrangedClusters.get(4).get(2).equals(dataSetBuilder.getPattern(73)));
		assertTrue(arrangedClusters.get(4).get(3).equals(dataSetBuilder.getPattern(77)));
		assertTrue(arrangedClusters.get(4).get(4).equals(dataSetBuilder.getPattern(78)));
		assertTrue(arrangedClusters.get(4).get(5).equals(dataSetBuilder.getPattern(79)));

		assertTrue(arrangedClusters.get(5).get(0).equals(dataSetBuilder.getPattern(80)));
		assertTrue(arrangedClusters.get(5).get(1).equals(dataSetBuilder.getPattern(81)));
		assertTrue(arrangedClusters.get(5).get(2).equals(dataSetBuilder.getPattern(82)));
		assertTrue(arrangedClusters.get(5).get(3).equals(dataSetBuilder.getPattern(83)));
		assertTrue(arrangedClusters.get(5).get(4).equals(dataSetBuilder.getPattern(84)));
		assertTrue(arrangedClusters.get(5).get(5).equals(dataSetBuilder.getPattern(85)));
		assertTrue(arrangedClusters.get(5).get(6).equals(dataSetBuilder.getPattern(86)));
		assertTrue(arrangedClusters.get(5).get(7).equals(dataSetBuilder.getPattern(87)));
		assertTrue(arrangedClusters.get(5).get(8).equals(dataSetBuilder.getPattern(88)));
		assertTrue(arrangedClusters.get(5).get(9).equals(dataSetBuilder.getPattern(89)));
		assertTrue(arrangedClusters.get(5).get(10).equals(dataSetBuilder.getPattern(90)));
		assertTrue(arrangedClusters.get(5).get(11).equals(dataSetBuilder.getPattern(91)));
		assertTrue(arrangedClusters.get(5).get(12).equals(dataSetBuilder.getPattern(92)));

		// the last cluster is empty
		// should throw an exception (no such element / index out of bounds)
		assertTrue(arrangedClusters.get(6).get(0).equals(dataSetBuilder.getPattern(100)));
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void testArrangedCentroids() {
		assertTrue(arrangedCentroids.get(0).equals(centroids.subVector(0, 1)));

		assertTrue(arrangedCentroids.get(1).equals(centroids.subVector(2, 3)));

		assertTrue(arrangedCentroids.get(2).equals(centroids.subVector(4, 5)));

		assertTrue(arrangedCentroids.get(3).equals(centroids.subVector(6, 7)));

		assertTrue(arrangedCentroids.get(4).equals(centroids.subVector(8, 9)));

		assertTrue(arrangedCentroids.get(5).equals(centroids.subVector(10, 11)));

		// the last centroid is useless, i.e. no pattern "belongs" to it
		// should throw an exception (no such element / index out of bounds)
		assertTrue(arrangedCentroids.get(6).equals(centroids.subVector(12, 13)));
	}

	@Test
	public void testSetMean() {
		Vector calculatedMean = dataSetBuilder.getSetMean(arrangedClusters.get(0));
		Vector handMean = new MixedVector();
		handMean.add(new Int(5));
		handMean.add(new Int(1));
		for (int i = 0; i < handMean.size(); i++)
			assertEquals(handMean.getInt(i), calculatedMean.getInt(i));

		calculatedMean = dataSetBuilder.getSetMean(arrangedClusters.get(1));
		handMean.set(0, new Real(873 / 26.0));
		handMean.set(1, new Real(84 / 26.0));
		for (int i = 0; i < handMean.size(); i++)
			assertEquals(handMean.getInt(i), calculatedMean.getInt(i));

		calculatedMean = dataSetBuilder.getSetMean(arrangedClusters.get(2));
		handMean.set(0, new Real(629 / 29.0));
		handMean.set(1, new Real(491 / 29.0));
		for (int i = 0; i < handMean.size(); i++)
			assertEquals(handMean.getInt(i), calculatedMean.getInt(i));

		calculatedMean = dataSetBuilder.getSetMean(arrangedClusters.get(3));
		handMean.set(0, new Real(237 / 18.0));
		handMean.set(1, new Real(391 / 18.0));
		for (int i = 0; i < handMean.size(); i++)
			assertEquals(handMean.getInt(i), calculatedMean.getInt(i));

		calculatedMean = dataSetBuilder.getSetMean(arrangedClusters.get(4));
		handMean.set(0, new Real(160 / 6.0));
		handMean.set(1, new Real(142 / 6.0));
		for (int i = 0; i < handMean.size(); i++)
			assertEquals(handMean.getInt(i), calculatedMean.getInt(i));

		calculatedMean = dataSetBuilder.getSetMean(arrangedClusters.get(5));
		handMean.set(0, new Real(248 / 13.0));
		handMean.set(1, new Real(510 / 13.0));
		for (int i = 0; i < handMean.size(); i++)
			assertEquals(handMean.getInt(i), calculatedMean.getInt(i));
	}

	@Test
	public void testMean() {
		Vector calculatedMean = dataSetBuilder.getMean();
		Vector handMean = new MixedVector();

		handMean.add(new Real(2147 / 93.0));
		handMean.add(new Real(1618 / 93.0));
		for (int i = 0; i < handMean.size(); i++)
			assertEquals(handMean.getInt(i), calculatedMean.getInt(i));
	}

	@Test
	public void testSetVariance() {
		Vector calculatedVariance = dataSetBuilder.getSetVariance(arrangedClusters.get(0));
		Vector handVariance = new MixedVector();
		handVariance.add(new Int(0));
		handVariance.add(new Int(0));
		for (int i = 0; i < handVariance.size(); i++)
			assertEquals(handVariance.getInt(i), calculatedVariance.getInt(i));

		calculatedVariance = dataSetBuilder.getSetVariance(arrangedClusters.get(1));
		handVariance.set(0, new Real(6.73076923076923));
		handVariance.set(1, new Real(3.30769230769231));
		for (int i = 0; i < handVariance.size(); i++)
			assertEquals(handVariance.getInt(i), calculatedVariance.getInt(i));

		calculatedVariance = dataSetBuilder.getSetVariance(arrangedClusters.get(2));
		handVariance.set(0, new Real(12.1379310344828));
		handVariance.set(1, new Real(19.2068965517241));
		for (int i = 0; i < handVariance.size(); i++)
			assertEquals(handVariance.getInt(i), calculatedVariance.getInt(i));

		calculatedVariance = dataSetBuilder.getSetVariance(arrangedClusters.get(3));
		handVariance.set(0, new Real(1.61111111111111));
		handVariance.set(1, new Real(2.5));
		for (int i = 0; i < handVariance.size(); i++)
			assertEquals(handVariance.getInt(i), calculatedVariance.getInt(i));

		calculatedVariance = dataSetBuilder.getSetVariance(arrangedClusters.get(4));
		handVariance.set(0, new Real(2.66666666666667));
		handVariance.set(1, new Real(2.66666666666667));
		for (int i = 0; i < handVariance.size(); i++)
			assertEquals(handVariance.getInt(i), calculatedVariance.getInt(i));

		calculatedVariance = dataSetBuilder.getSetVariance(arrangedClusters.get(5));
		handVariance.set(0, new Real(2.23076923076923));
		handVariance.set(1, new Real(9.30769230769231));
		for (int i = 0; i < handVariance.size(); i++)
			assertEquals(handVariance.getInt(i), calculatedVariance.getInt(i));
	}

	@Test
	public void testVariance() {
		Vector calculatedVariance = dataSetBuilder.getVariance();
		Vector handVariance = new MixedVector();

		handVariance.add(new Real(63.2150537634409));
		handVariance.add(new Real(140.47311827957));
		for (int i = 0; i < handVariance.size(); i++)
			assertEquals(handVariance.getInt(i), calculatedVariance.getInt(i));
	}
}
