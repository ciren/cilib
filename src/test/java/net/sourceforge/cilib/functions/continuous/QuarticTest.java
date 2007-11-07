package net.sourceforge.cilib.functions.continuous;

import static org.junit.Assert.assertEquals;
import net.sourceforge.cilib.functions.Function;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.Test;

public class QuarticTest {

	@Test
	public void evaluationTest() {
		Function function = new Quartic();
		
		Vector v = new Vector();
		v.add(new Real(0.0));
		v.add(new Real(1.0));
		v.add(new Real(2.0));
		
		assertEquals(33.0, function.evaluate(v), 0);
	}
	
}
