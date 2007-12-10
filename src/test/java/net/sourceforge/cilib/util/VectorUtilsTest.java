package net.sourceforge.cilib.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.Before;
import org.junit.Test;

public class VectorUtilsTest {
	
	private Vector vector;
	
	@Before
	public void initialise() {
		vector = new Vector();

		for(int i = 1; i < 5; i++) {
			Numeric element = new Real(i);
			element.setUpperBound(i * 2);
			element.setLowerBound(i * -2);
			vector.append(element);
		}
	}

	@Test
	public void testUpperBounds() {
		int i = 1;
		for (Type element : VectorUtils.createUpperBoundVector(vector)) {
			Numeric numeric = (Numeric) element;
			assertFalse(numeric.isInsideBounds());
			assertEquals(i++ * 2, numeric.getReal(), 0.0);
		}
	}

	@Test(expected = UnsupportedOperationException.class)
	public void createUpperBoundVector() {
		vector.add(new Vector());
		VectorUtils.createUpperBoundVector(vector);
	}
	
	@Test
	public void testLowerBounds() {
		int i = 1;
		for (Type element : VectorUtils.createLowerBoundVector(vector)) {
			Numeric numeric = (Numeric) element;
			assertTrue(numeric.isInsideBounds());
			assertEquals(i++ * -2, numeric.getReal(), 0.0);
		}
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void createLowerBoundVector() {
		vector.add(new Vector());
		VectorUtils.createLowerBoundVector(vector);
	}


}
