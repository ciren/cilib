package net.sourceforge.cilib.entity;

import static org.junit.Assert.assertEquals;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.container.Vector;

import org.junit.Test;

public class IndividualTest {
	
	@Test
	public void testClone() {
		Vector genes = new Vector(5);
		genes.add(new Real(1.0));
		genes.add(new Real(2.0));
		genes.add(new Real(3.0));
		genes.add(new Real(4.0));
		genes.add(new Real(5.0));
		
		Individual i = new Individual();
		i.setContents(genes);
		
		Individual clone = i.getClone();
		
		assertEquals(5, i.getDimension());
		assertEquals(5, clone.getDimension());
		assertEquals(i.getDimension(), clone.getDimension());
		
		Vector cloneVector = (Vector) clone.getContents();
		
		for (int k = 0; k < cloneVector.getDimension(); k++) {
			assertEquals(genes.get(k), cloneVector.get(k));
		}
	}

}
