package net.sourceforge.cilib.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import net.sourceforge.cilib.ec.Individual;
import net.sourceforge.cilib.type.types.Blackboard;
import net.sourceforge.cilib.type.types.Real;
import net.sourceforge.cilib.type.types.Type;
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
		i.setCandidateSolution(genes);
		
		Individual clone = i.getClone();
		
		assertEquals(5, i.getDimension());
		assertEquals(5, clone.getDimension());
		assertEquals(i.getDimension(), clone.getDimension());
		
		Vector cloneVector = (Vector) clone.getCandidateSolution();
		
		for (int k = 0; k < cloneVector.getDimension(); k++) {
			assertEquals(genes.get(k), cloneVector.get(k));
		}
	}
	
	@Test
	public void equals() {
		Individual i1 = new Individual();
		Individual i2 = new Individual();
		
		assertTrue(i1.equals(i2));
		assertTrue(i2.equals(i1));
		assertTrue(i1.equals(i1));
		
		assertFalse(i1.equals(null));
	}
	
	@Test
	public void hashCodes() {
		Individual i1 = new Individual();
		Individual i2 = new Individual();
		
		assertTrue(i1.hashCode() == i2.hashCode());
		
		i1.setProperties(new Blackboard<Enum<?>, Type>());
		assertFalse(i1.hashCode() == i2.hashCode());
	}

}
