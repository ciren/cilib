package net.sourceforge.cilib.type.types.container;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import net.sourceforge.cilib.type.types.Real;

import org.junit.Before;
import org.junit.Test;

public class NaryTreeTest {
	
	private NaryTree<Real> doubleTree;

	@Before
	public void setupNaryTree() {
		doubleTree = new NaryTree<Real>(3, new Real(1.0));
		doubleTree.addSubTree(new NaryTree<Real>(3, new Real(2.0)));
		doubleTree.addSubTree(new NaryTree<Real>(3, new Real(3.0)));
		doubleTree.addSubTree(new NaryTree<Real>(3, new Real(4.0)));
	}
	
	@Test
	public void getSubtreeWithinTree() {
		Tree<Real> result = doubleTree.getSubTree(new Real(3.0));
		assertEquals(3.0, result.getKey().getReal(), 0);
	}
	
	@Test
	public void getSubtreeWithValidIndex() {
		assertNotNull(doubleTree.getSubTree(0));
		assertNotNull(doubleTree.getSubTree(1));
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void getSubtreeWithInvalidIndex() {
		doubleTree.getSubTree(-1);
		doubleTree.getSubTree(3);
	}
	
	@Test
	public void removalOfSubtreeByKey() {
		Tree<Real> removed = doubleTree.removeSubTree(new Real(2.0));
		assertNotNull(removed);
		assertEquals(2.0, removed.getKey().getReal(), 0);
		assertTrue(doubleTree.getSubTree(new Real(2.0)).isEmpty());
	}
	
	@Test
	public void removeSubTreeWithIndex() {
		assertNotNull(doubleTree.removeSubTree(0));
	}
	
	@Test
	public void itereator() {
		Iterator<Real> i = doubleTree.iterator();
		
		assertEquals(1.0, i.next().getReal(), 0);
		assertEquals(2.0, i.next().getReal(), 0);
		assertEquals(3.0, i.next().getReal(), 0);
		assertEquals(4.0, i.next().getReal(), 0);
	}
}
