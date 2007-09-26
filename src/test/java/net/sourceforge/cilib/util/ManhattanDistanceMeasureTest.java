package net.sourceforge.cilib.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.MixedVector;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Theuns Cloete
 */
public class ManhattanDistanceMeasureTest {
	private static DistanceMeasure distanceMeasure;

	@BeforeClass
	public static void setUp() {
		distanceMeasure = new ManhattanDistanceMeasure();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testVectorDistance() {
		Vector v1 = new MixedVector();
		Vector v2 = new MixedVector();
		
		v1.add(new Real(4.0));
		v1.add(new Real(3.0));
		v1.add(new Real(2.0));
		
		v2.add(new Real(2.0));
		v2.add(new Real(3.0));
		v2.add(new Real(4.0));
		
		assertEquals(4.0, distanceMeasure.distance(v1, v2), Double.MIN_NORMAL);
		
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
		
		assertEquals(4.0, distanceMeasure.distance(l1, l2), Double.MIN_NORMAL);
		
		l1.add(11.0);
		
		distanceMeasure.distance(l1, l2);
	}
	
	@Test
	public void testSingleDimension() {
		List<Double> list1 = new ArrayList<Double>(1);
		List<Double> list2 = new ArrayList<Double>(1);
		
		list1.add(0.0);
		list2.add(1.0);
		
		assertEquals(1.0, distanceMeasure.distance(list1, list2), Double.MIN_NORMAL);
	}
}
