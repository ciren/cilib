package net.sourceforge.cilib.functions.continuous;

import org.junit.Test;

import net.sourceforge.cilib.functions.Function;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.MixedVector;
import net.sourceforge.cilib.type.types.container.Vector;

import static org.junit.Assert.*;

public class QuarticTest {

	@Test
	public void evaluationTest() {
		Function function = new Quartic();
		
		Vector v = new MixedVector();
		v.add(new Real(0.0));
		v.add(new Real(1.0));
		v.add(new Real(2.0));
		
		assertEquals(33.0, function.evaluate(v), 0);
	}
	
}
