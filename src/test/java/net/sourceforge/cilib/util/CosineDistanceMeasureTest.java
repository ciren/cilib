package net.sourceforge.cilib.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Theuns Cloete
 */
public class CosineDistanceMeasureTest {
	private static DistanceMeasure distanceMeasure = null;

	@BeforeClass
	public static void setUp() {
		distanceMeasure = new CosineDistanceMeasure();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testVectorDistance() {
		Vector v1 = new Vector();
		Vector v2 = new Vector();
		
		v1.add(new Real(4.0));
		v1.add(new Real(3.0));
		v1.add(new Real(2.0));
		
		v2.add(new Real(2.0));
		v2.add(new Real(3.0));
		v2.add(new Real(4.0));
		
		double distance = distanceMeasure.distance(v1, v2);
		assertTrue(distance >= -1 && distance <= 1);
		assertEquals(1 - (25.0 / 29.0), distance, 0.000000000000001);
		
		v1.add(new Real(22.0));
		
		distanceMeasure.distance(v1, v2);
	}
	
	
	@Test(expected = IllegalArgumentException.class)
	public void testCollectionDistance() {
		List<Double> l1 = new ArrayList<Double>();
		List<Double> l2 = new ArrayList<Double>();
		
		l1.add(4.0);
		l1.add(3.0);
		l1.add(2.0);
		
		l2.add(2.0);
		l2.add(3.0);
		l2.add(4.0);
		
		double distance = distanceMeasure.distance(l1, l2);
		assertTrue(distance >= -1 && distance <= 1);
		assertEquals(1 - (25.0 / 29.0), distance, 0.000000000000001);
		
		l1.add(11.0);
		
		distanceMeasure.distance(l1, l2);
	}
	
	@Test
	public void testSingleDimension() {
		List<Double> list1 = new ArrayList<Double>(1);
		List<Double> list2 = new ArrayList<Double>(1);
		
		list1.add(0.0);
		list2.add(1.0);
		
		try {
			distanceMeasure.distance(list1, list2);
			fail("Exception is not thrown????");
		}
		catch(ArithmeticException a) {
		}

		list1.set(0, 3.0);
		double distance = distanceMeasure.distance(list1, list2);
		assertTrue(distance >= -1 && distance <= 1);
		assertEquals(0.0, distance, Double.MIN_NORMAL);
	}
}
