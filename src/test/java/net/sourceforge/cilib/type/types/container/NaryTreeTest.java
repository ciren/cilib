package net.sourceforge.cilib.type.types.container;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

public class NaryTreeTest {
	
	private NaryTree<Double> doubleTree;

	@Before
	public void setupNaryTree() {
		doubleTree = new NaryTree<Double>(3, 1.0);
		doubleTree.addSubTree(new NaryTree<Double>(3, 2.0));
		doubleTree.addSubTree(new NaryTree<Double>(3, 3.0));
		doubleTree.addSubTree(new NaryTree<Double>(3, 4.0));
	}
	
	@Test
	public void getSubtreeWithinTree() {
		Tree<Double> result = doubleTree.getSubTree(3.0);
		assertEquals(3.0, result.getKey(), 0);
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
		Tree<Double> removed = doubleTree.removeSubTree(2.0);
		assertNotNull(removed);
		assertEquals(2.0, removed.getKey(), 0);
		assertTrue(doubleTree.getSubTree(2.0).isEmpty());
	}
	
	@Test
	public void removeSubTreeWithIndex() {
		assertNotNull(doubleTree.removeSubTree(0));
	}
	
	@Test
	public void itereator() {
		Iterator<Double> i = doubleTree.iterator();
		
		assertEquals(1.0, i.next().doubleValue(), 0);
		assertEquals(2.0, i.next().doubleValue(), 0);
		assertEquals(3.0, i.next().doubleValue(), 0);
		assertEquals(4.0, i.next().doubleValue(), 0);
	}

}
