package net.sourceforge.cilib.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.type.types.MixedVector;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Vector;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Olusegun
 */
public class ChebyshevDistanceMeasureTest {
	
	private static DistanceMeasure distanceMeasure;
	
	@BeforeClass
	public static void setUp() {
		distanceMeasure = new ChebyshevDistanceMeasure();
	}
	
	@Test
	public void testVectorDistance() {
		Vector v1 = new MixedVector();
		Vector v2 = new MixedVector();
		
		v1.add(new Real(5.0));
		v1.add(new Real(3.0));
		v1.add(new Real(1.0));
		
		v2.add(new Real(1.0));
		v2.add(new Real(3.0));
		v2.add(new Real(5.5));
		
		assertEquals(4.5, distanceMeasure.distance(v1, v2));
		
		v1.add(new Real(22.0));
		
		try {
			distanceMeasure.distance(v1, v2);
			fail("Exception is not thrown!!!");
		} catch(IllegalArgumentException exc) {}
	}
	
	@Test
	public void testCollectionDistance() {
		List<Double> l1 = new ArrayList<Double>();
		List<Double> l2 = new ArrayList<Double>();
		
		l1.add(5.0);
		l1.add(3.0);
		l1.add(1.0);
		
		l2.add(1.0);
		l2.add(3.0);
		l2.add(5.0);
		
		assertEquals(4.0, distanceMeasure.distance(l1, l2));
		
		l1.add(11.0);
		
		try {
			distanceMeasure.distance(l1, l2);
			fail("Exception is not thrown!!!");
		} catch (IllegalArgumentException exc) {}
	}
	
	@Test
	public void testSingleDimension() {
		List<Double> list1 = new ArrayList<Double>(1);
		List<Double> list2 = new ArrayList<Double>(1);
		
		list1.add(0.0);
		list2.add(1.0);
		
		assertEquals(1.0, distanceMeasure.distance(list1, list2));
	}
}
